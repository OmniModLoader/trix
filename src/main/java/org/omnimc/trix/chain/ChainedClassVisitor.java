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

package org.omnimc.trix.chain;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;

/**
 * Defines an interface for chaining {@linkplain  ClassVisitor} instances together
 * in a sequence. Each visitor can delegate its processing to the next one
 * in the chain.
 * <p>
 * This interface is primarily used in the context of `ClassModifier` to
 * dynamically build a pipeline of visitors that process a {@linkplain  ClassNode}.
 *
 * @author <b><a href="https://github.com/CadenCCC">Caden</a></b>
 * @since 1.0.0
 */
@FunctionalInterface
public interface ChainedClassVisitor {

    /**
     * Chains the current visitor to the next visitor in the pipeline.
     *
     * @param next The next {@linkplain ClassVisitor} in the chain. May be null if this is the last visitor.
     * @return A {@linkplain ClassVisitor} that represents the current visitor, now linked to the next visitor.
     */
    ClassVisitor withNext(ClassVisitor next);

}