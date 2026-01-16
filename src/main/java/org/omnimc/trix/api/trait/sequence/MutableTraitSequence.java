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

import org.omnimc.trix.api.trait.Trait;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
public class MutableTraitSequence<T extends Trait<?>> implements TraitSequence<T> {

    private final List<T> traits = new ArrayList<>();
    private boolean sorted = true;

    @SafeVarargs
    public final void addAll(T... traitsToAdd) {
        traits.addAll(Arrays.asList(traitsToAdd));
        sorted = false;
    }

    public void add(T trait) {
        traits.add(trait);
        sorted = false;
    }

    /**
     * This method returns a sortedArray (with {@link Comparator#naturalOrder()}) of {@code T}.
     * <p>
     * We could do this better, but you can't cast whatever {@code T} is because it's most likely a child of {@code T}.
     * <p>
     * The decision was made to use {@link System#arraycopy(Object, int, Object, int, int)} because we can trick it to be whatever {@code T} is.
     *
     * @return A sorted raw array of {@code T}, or {@code null} if it's empty.
     */
    @Override
    public T[] getSequence() {
        if (!sorted) {
            traits.sort(Comparator.naturalOrder());
            sorted = true;
        }

        if (traits.isEmpty()) {
            return null;
        }

        // This is ugly I know, but we can't really help it.
        T firstTrait = traits.getFirst();

        @SuppressWarnings("unchecked")
        T[] newArray = (T[]) Array.newInstance(firstTrait.getClass(), traits.size());

        //noinspection SuspiciousSystemArraycopy
        System.arraycopy(traits.toArray(), 0, newArray, 0, traits.size());
        return newArray;
    }

    public int getSize() {
        return traits.size();
    }

    public void clear() {
        traits.clear();
        sorted = true;
    }
}