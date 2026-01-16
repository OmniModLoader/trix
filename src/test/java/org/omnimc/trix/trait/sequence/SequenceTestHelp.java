/*
 * MIT License
 *
 * Copyright (c) 2026 OmniMC
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

package org.omnimc.trix.trait.sequence;

import org.omnimc.trix.api.trait.Target;
import org.omnimc.trix.api.trait.TargetType;
import org.omnimc.trix.api.trait.clazz.ClassTrait;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
public final class SequenceTestHelp {

    public static ClassTrait[] unsortedClassTraits =
            new ClassTrait[]{createStupidClassTrait(1),
                    createStupidClassTrait(10),
                    createStupidClassTrait(0),
                    createStupidClassTrait(100),
                    createStupidClassTrait(Integer.MAX_VALUE)};

    public static ClassTrait[] sortedClassTraits =
            new ClassTrait[]{createStupidClassTrait(Integer.MAX_VALUE),
                    createStupidClassTrait(100),
                    createStupidClassTrait(10),
                    createStupidClassTrait(1),
                    createStupidClassTrait(0)};

    public static ClassTrait createStupidClassTrait(int priority) {
        return ClassTrait.of((classNode, remapper) -> classNode, Target.of(priority, TargetType.ALL));
    }
}