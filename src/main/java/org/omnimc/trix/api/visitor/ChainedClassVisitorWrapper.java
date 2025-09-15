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

package org.omnimc.trix.api.visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Objects;

/**
 * A wrapper that ensures compatibility for {@linkplain ClassVisitor}
 * instances that do not implement {@linkplain ChainedClassVisitor}.
 * <p>
 * This allows legacy or standard ASM visitors to be seamlessly
 * integrated into a visitor pipeline.
 * <p>
 * If the visitor already implements {@linkplain ChainedClassVisitor},
 * chaining is delegated to its implementation.
 * Otherwise, a {@linkplain DelegatingClassVisitor} is created to manage chaining.
 *
 * @author <b><a href="https://github.com/CadenCCC">Caden</a></b>
 * @since 1.0.0
 */
public class ChainedClassVisitorWrapper implements ChainedClassVisitor {

    private final ClassVisitor wrappedVisitor;

    public ChainedClassVisitorWrapper(ClassVisitor classVisitor) {
        Objects.requireNonNull(classVisitor, "Wrapped visitor cannot be null.");

        this.wrappedVisitor = classVisitor;
    }

    @Override
    public ClassVisitor withNext(ClassVisitor next) {
        if (wrappedVisitor instanceof ChainedClassVisitor) {
            return ((ChainedClassVisitor) wrappedVisitor).withNext(next);
        } else {
            return new DelegatingClassVisitor(wrappedVisitor, next);
        }
    }

    private static class DelegatingClassVisitor extends ClassVisitor {
        public DelegatingClassVisitor(ClassVisitor currentVisitor, ClassVisitor nextVisitor) {
            super(Opcodes.ASM9, currentVisitor);
            this.cv = nextVisitor;
        }
    }
}