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

package org.omnimc.trix.impl;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.omnimc.trix.api.visitor.ChainedClassVisitor;
import org.omnimc.trix.api.visitor.ChainedClassVisitorWrapper;
import org.omnimc.trix.impl.visitors.mappings.MappingClassVisitor;

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
@Deprecated
public class ClassModifier {

    private final List<ClassVisitor> visitors = new ArrayList<>();
    private final ClassNode plainNode = new ClassNode();

    public ClassModifier addVisitor(@NotNull ClassVisitor visitor) {
        Objects.requireNonNull(visitor, "Visitor cannot be null.");

        this.visitors.add(visitor);
        return this;
    }

    public ClassNode modify(@NotNull ClassNode classNode) {
        return modify(classNode, true);
    }

    public ClassNode modify(@NotNull ClassNode classNode, boolean usePlainNode) {
        Objects.requireNonNull(classNode, "ClassNode cannot be null.");
        if (visitors.isEmpty()) {
            throw new IllegalStateException("No visitors have been added. Use addVisitor() to add visitors.");
        }

/*        if (mappingRequired) {
            this.addVisitor(new MappingClassVisitor(usePlainNode ? getPlainNodeCopy() : classNode, remapper));
        }*/

        ClassVisitor chainedVisitor = chainVisitors();
        classNode.accept(chainedVisitor);

        return usePlainNode ? getPlainNodeCopy() : classNode;
    }

    /**
     * @return A copy of the current `plainNode` for safe mutation.
     */
    public ClassNode getPlainNodeCopy() {
        ClassNode copy = new ClassNode();
        plainNode.accept(copy);
        return copy;
    }

    public boolean isReady() {
        return !visitors.isEmpty();
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