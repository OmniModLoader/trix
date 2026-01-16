/*
 * MIT License
 *
 * Copyright (c) 2025-2026 OmniMC
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

import java.util.Objects;

/**
 * This class is meant to provide targeting to {@link Trait}.
 * <p>
 * The way we succeed at this is that we need a {@code priority} and {@link TargetType}.
 *
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 1.0.0
 */
public interface Target extends Comparable<Target> {

    /**
     * A default {@link TargetType} is ALL, which has zero specified target name.
     */
    TargetType DEFAULT_TARGET_TYPE = TargetType.ALL;

    /**
     * A default priority type is going to be {@code 0}.
     */
    int DEFAULT_PRIORITY = 0;

    /**
     * This is a utility method that provides the default values for a {@link Target}.
     *
     * @return An instance of {@link Target} with both {@code priority} & {@code TargetType} to their present defaults.
     */
    static Target defaultTarget() {
        return of(DEFAULT_PRIORITY, DEFAULT_TARGET_TYPE);
    }

    /**
     * This is a utility method that lets you easily create a new instance of {@link Target} with a provided {@code priority} & {@code TargetType}.
     *
     * @param priority This is the priority the Target will have, the default is {@link Target#DEFAULT_PRIORITY}.
     * @param type     This type is either going to be {@link TargetType#DIRECT} or {@link TargetType#ALL}.
     *                 However, default is {@link Target#DEFAULT_TARGET_TYPE}.
     * @return An instance of {@link ProxyTarget} filled in with the provided data.
     */
    static Target of(int priority, TargetType type) {
        return new ProxyTarget(priority, type);
    }

    /**
     * This is the implementing method for getting a priority, the default is {@link Target#DEFAULT_PRIORITY}.
     *
     * @return An integer from negative to positive integer limit.
     */
    int getPriority();

    /**
     * This is the implementing method for getting targetType, the default is {@link Target#DEFAULT_TARGET_TYPE}.
     *
     * @return An instance of {@link TargetType}.
     */
    TargetType getTargetType();

    @Override
    default int compareTo(@NotNull Target o) {
        return Integer.compare(o.getPriority(), getPriority());
    }

    /**
     * This is a utility class that provides an instance for the {@link Target#of(int, TargetType)} method.
     */
    @SuppressWarnings("ClassCanBeRecord")
    final class ProxyTarget implements Target {

        private final int priority;
        private final TargetType targetType;

        public ProxyTarget(int priority, TargetType targetType) {
            this.priority = priority;
            this.targetType = targetType;
        }

        @Override
        public int getPriority() {
            return priority;
        }

        @Override
        public TargetType getTargetType() {
            return targetType;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            ProxyTarget that = (ProxyTarget) o;
            return priority == that.priority && targetType == that.targetType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(priority, targetType);
        }
    }
}