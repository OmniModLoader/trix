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

package org.omnimc.trix.api.trait.sequence;

import org.omnimc.trix.api.trait.Target;
import org.omnimc.trix.api.trait.Trait;

import java.util.Arrays;
import java.util.Comparator;

/**
 * This class is used to hold a list of {@link Trait}'s, usually it is sorted by {@link Target#getPriority()} with {@link Comparator#naturalOrder()}.
 * <p>
 * There are two types of {@link TraitSequence}'s:
 * Immutable and Mutable.
 * <p>
 * By default {@link TraitSequence}'s are Immutable. However, you can use {@link TraitSequence#createMutableTraitSequence(Trait[])}
 * to return an instance of {@link MutableTraitSequence} which will provide you the ability to add {@link Trait}s of your choice.
 *
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
@FunctionalInterface
public interface TraitSequence<T extends Trait<?>> {

    @SafeVarargs
    static <T extends Trait<?>> T[] sortTraitsByPriority(T... traits) {
        T[] array = traits.clone();
        Arrays.sort(array, Comparator.naturalOrder());
        return array;
    }

    @SafeVarargs
    static <T extends Trait<?>> TraitSequence<T> createImmutableTraitSequence(T... traits) {
        T[] array = sortTraitsByPriority(traits);
        return () -> (T[]) array;
    }

    @SafeVarargs
    static <T extends Trait<?>> MutableTraitSequence<T> createMutableTraitSequence(T... traits) {
        MutableTraitSequence<T> mutableTraits = new MutableTraitSequence<>();
        mutableTraits.addAll(traits);
        return mutableTraits;
    }

    /**
     * We recommend using {@link TraitSequence#createImmutableTraitSequence(Trait[])} or {@link  TraitSequence#createMutableTraitSequence(Trait[])}
     * when creating an instance of {@link TraitSequence}.
     * <p>
     * If you want to implement this your self for custom functionality all we require is that it is sorted correctly with {@link Comparator#naturalOrder()}.
     *
     * @return An instance of {@code T}.
     */
    T[] getSequence();

}