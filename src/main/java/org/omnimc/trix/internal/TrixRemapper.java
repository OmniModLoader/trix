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

package org.omnimc.trix.internal;

import org.objectweb.asm.commons.Remapper;
import org.omnimc.lumina.Mappings;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class TrixRemapper extends Remapper {

    private final Mappings mappings;

    public TrixRemapper(Mappings mappings) {
        this.mappings = mappings;
    }

    @Override
    public String map(String internalName) {
        return mapType(internalName);
    }

    @Override
    public String mapType(String internalName) {
        return mappings.getClassName(internalName);
    }

    @Override
    public String mapMethodName(String owner, String name, String descriptor) {
        if (descriptor != null) {
            descriptor = mapMethodDesc(descriptor);
        }

        return mappings.getMethodName(owner, name + descriptor);
    }

    @Override
    public String mapFieldName(String owner, String name, String descriptor) {
        return mappings.getFieldName(owner, name);
    }
}