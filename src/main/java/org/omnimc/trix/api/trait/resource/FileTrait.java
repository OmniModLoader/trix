/*
 * MIT License
 *
 * Copyright (c) 2025 OmniMC
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
 *
 */

package org.omnimc.trix.api.trait.resource;

import org.objectweb.asm.commons.Remapper;
import org.omnimc.trix.api.trait.Target;
import org.omnimc.trix.api.trait.Trait;

import java.util.function.BiFunction;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 1.0.0
 */
public interface FileTrait extends Trait<byte[]> {

    static FileTrait of(BiFunction<byte[], Remapper, byte[]> modify) {
        return of(modify, Target.defaultTarget());
    }

    static FileTrait of(BiFunction<byte[], Remapper, byte[]> modify, Target target) {
        return new InnerFileTrait(modify, target);
    }

    @Override
    byte[] modify(byte[] nodeToChange, Remapper remapper);

    @SuppressWarnings("ClassCanBeRecord")
    final class InnerFileTrait implements FileTrait {

        private final BiFunction<byte[], Remapper, byte[]> modify;
        private final Target target;

        public InnerFileTrait(BiFunction<byte[], Remapper, byte[]> modify, Target target) {
            this.modify = modify;
            this.target = target;
        }

        @Override
        public byte[] modify(byte[] nodeToChange, Remapper remapper) {
            return modify.apply(nodeToChange, remapper);
        }

        @Override
        public Target getTarget() {
            return target;
        }
    }
}