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

package org.omnimc.trix.api.trait;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.commons.Remapper;

/**
 * A `trait` is a modification for a class or a file.
 * <p>
 * We can target all instances or just specific ones with {@link Target}. This is comparable so we can get a priority from the {@link Target} interface.
 *
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 1.0.0
 */
public interface Trait<T> extends Comparable<Trait<T>> {

    /**
     * This method is the modification method ran by the loader.
     *
     * @param object   An object that is going to be modified, this is provided by the loader.
     * @param remapper An instance of {@link Remapper} provided to hopefully support you.
     * @return You should return the provided {@code object} with modifications, even if you don't modify it you should return said {@code object}.
     */
    T modify(T object, Remapper remapper);

    /**
     * An instance of {@link Target} meant to provide the loader with a way to add priority and name targets.
     *
     * @return By default, it will return {@link Target#defaultTarget()}.
     */
    default Target getTarget() {
        return Target.defaultTarget();
    }

    @Override
    default int compareTo(@NotNull Trait<T> o) {
        return getTarget().compareTo(o.getTarget());
    }
}