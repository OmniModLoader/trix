/*
 * MIT License
 *
 * Copyright (c) 2024-2025 OmniMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.omnimc.trix;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;
import org.omnimc.lumina.Mappings;
import org.omnimc.trix.chain.ChainedClassVisitor;
import org.omnimc.trix.chain.ChainedClassVisitorWrapper;
import org.omnimc.trix.internal.TrixRemapper;
import org.omnimc.trix.internal.mapping.MappingClassVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A utility class that facilitates the modification of a {@linkplain ClassNode}
 * using a pipeline of {@linkplain ClassVisitor} instances.
 * <p>
 * Visitors can be added to the pipeline, and the pipeline is executed in sequence,
 * allowing transformations to be applied to the provided class node.
 * <p>
 * This class also supports mapping transformations via {@linkplain MappingClassVisitor},
 * which requires setting up mappings first.
 *
 * @author <b><a href="https://github.com/CadenCCC">Caden</a></b>
 * @since 1.0.0
 */
public class ClassModifier {

    private final List<ClassVisitor> visitors = new ArrayList<>();
    private final ClassNode plainNode = new ClassNode();

    private Remapper trixRemapper = null;

    private boolean mappingRequired = false;

    /**
     * Sets mappings and creates a remapper for use in the modification pipeline.
     * <p>
     * Mappings are used to rename, remap, or transform elements of the {@linkplain ClassNode}
     * and are applied automatically when {@linkplain #addMappingVisitor()} is called.
     *
     * @param mappings The non-null mapping definitions to use for transformation.
     * @return This {@linkplain ClassModifier} instance for method chaining.
     * @throws NullPointerException If the provided mappings are null.
     */
    public ClassModifier setMappings(@NotNull Mappings mappings) {
        Objects.requireNonNull(mappings, "Mappings cannot be null.");
        this.trixRemapper = new TrixRemapper(mappings);
        return this;
    }

    /**
     * Sets the remapper to be used in the modification pipeline.
     * The remapper is responsible for transforming class, field, or method names
     * based on predefined mapping rules.
     *
     * @param remapper The non-null remapper instance to set for the modification process.
     * @return This {@code ClassModifier} instance for method chaining.
     */
    public ClassModifier setRemapper(Remapper remapper) {
        this.trixRemapper = remapper;
        return this;
    }

    /**
     * Adds a {@linkplain ClassVisitor} instance to the modification pipeline.
     * <p>
     * Visitors are executed in the order they are added.
     *
     * @param visitor The non-null visitor to add to the processing pipeline.
     * @return This {@linkplain ClassModifier} instance for method chaining.
     * @throws NullPointerException If the provided visitor is null.
     */
    public ClassModifier addVisitor(@NotNull ClassVisitor visitor) {
        Objects.requireNonNull(visitor, "Visitor cannot be null.");

        this.visitors.add(visitor);
        return this;
    }

    /**
     * Enables mapping transformations by adding a {@linkplain MappingClassVisitor} to
     * the pipeline. Only applicable if mappings are set using {@linkplain #setMappings(Mappings)}.
     *
     * @return This {@linkplain ClassModifier} instance for method chaining.
     * @throws IllegalStateException If mappings have not been defined using {@linkplain #setMappings(Mappings)}.
     */
    public ClassModifier addMappingVisitor() {
        if (trixRemapper == null) {
            throw new IllegalStateException("Mappings must be set before adding a mapping visitor. Call setMappings() first.");
        }
        this.mappingRequired = true;
        return this;
    }

    /**
     * Processes the provided {@linkplain ClassNode} through all visitors in the pipeline.
     * <p>
     * A copy of the `plainNode` is returned with any transformations applied.
     *
     * @param classNode The non-null class node to modify.
     * @return The modified {@linkplain ClassNode}.
     * @throws NullPointerException If the class node is null.
     * @throws IllegalStateException If no visitors are added and mapping is not required.
     */
    public ClassNode modify(@NotNull ClassNode classNode) {
        return modify(classNode, true);
    }

    /**
     * Processes the provided {@linkplain ClassNode}, optionally updating and returning a clone
     * of the `plainNode`.
     *
     * @param classNode    The non-null class node to modify.
     * @param usePlainNode If true, the `plainNode` is cloned and modified.
     * @return The modified {@linkplain ClassNode}.
     * @throws NullPointerException If the class node is null.
     * @throws IllegalStateException If no visitors are added and mapping is not required.
     */
    public ClassNode modify(@NotNull ClassNode classNode, boolean usePlainNode) {
        Objects.requireNonNull(classNode, "ClassNode cannot be null.");
        if (visitors.isEmpty() && !mappingRequired) {
            throw new IllegalStateException("No visitors have been added. Use addVisitor() to add visitors.");
        }

        if (mappingRequired) {
            this.addVisitor(new MappingClassVisitor(usePlainNode ? getPlainNodeCopy() : classNode, trixRemapper));
        }

        ClassVisitor chainedVisitor = chainVisitors();
        classNode.accept(chainedVisitor);

        return usePlainNode ? getPlainNodeCopy() : classNode;
    }

    /**
     * @return A copy of the current `plainNode` for safe mutation.
     */
    private ClassNode getPlainNodeCopy() {
        ClassNode copy = new ClassNode();
        plainNode.accept(copy);
        return copy;
    }

    /**
     * Indicates if the class modifier contains sufficient visitors
     * or has mappings enabled to perform transformations.
     *
     * @return True if the modifier is ready to process a class node, false otherwise.
     */
    public boolean isReady() {
        return !visitors.isEmpty() || mappingRequired;
    }

    /**
     * Chains all visitors in the pipeline using {@linkplain ChainedClassVisitorWrapper}.
     * <p>
     * Visitors are chained in reverse order such that the first visitor in the list
     * becomes the last executed in the pipeline.
     *
     * @return A {@linkplain ClassVisitor} representing the start of the visitor chain.
     */
    private ClassVisitor chainVisitors() {
        ClassVisitor current = null;

        for (int i = visitors.size() - 1; i >= 0; i--) {
            ClassVisitor visitor = visitors.get(i);

            ChainedClassVisitor wrapped = new ChainedClassVisitorWrapper(visitor);
            current = wrapped.withNext(current);
        }

        return current;
    }

}