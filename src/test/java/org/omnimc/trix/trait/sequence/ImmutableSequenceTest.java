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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.omnimc.trix.api.trait.clazz.ClassTrait;
import org.omnimc.trix.api.trait.sequence.TraitSequence;

import java.util.Arrays;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
public class ImmutableSequenceTest {

    @Test
    public void testImmutablePriority() {
        TraitSequence<ClassTrait> sequence = TraitSequence.createImmutableTraitSequence(SequenceTestHelp.unsortedClassTraits);

        int[] actualPriorities = Arrays.stream(sequence.getSequence())
                .mapToInt(trait -> trait.getTarget().getPriority())
                .toArray();

        int[] expectedPriorities = Arrays.stream(SequenceTestHelp.sortedClassTraits)
                .mapToInt(trait -> trait.getTarget().getPriority())
                .toArray();

        Assertions.assertArrayEquals(expectedPriorities, actualPriorities,
                                     "Priorities should be sorted from highest to lowest");
    }

}