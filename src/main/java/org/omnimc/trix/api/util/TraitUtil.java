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

package org.omnimc.trix.api.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;
import org.omnimc.trix.api.trait.Target;
import org.omnimc.trix.api.trait.TargetType;
import org.omnimc.trix.api.trait.Trait;
import org.omnimc.trix.api.trait.clazz.ClassTrait;
import org.omnimc.trix.api.trait.resource.FileTrait;
import org.omnimc.trix.api.trait.sequence.TraitSequence;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
public final class TraitUtil {

    public static byte[] applyFileTraits(String fileName, byte[] fileBytes, Remapper remapper, TraitSequence<? extends FileTrait> sequence) {
        return modifyTrait(fileName, sequence, fileBytes, remapper);
    }

    public static byte[] applyClassTraits(String className, URL classURL, Remapper remapper, TraitSequence<? extends ClassTrait> sequence) {
        try {
            ClassReader classReader = new ClassReader(classURL.openStream());
            ClassNode masterNode = new ClassNode();
            classReader.accept(masterNode, 0);

            ClassNode classNode = modifyTrait(className, sequence, masterNode, remapper);

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to apply Trait!!", e);
        }
    }

    private static <E, T extends Trait<E>> E modifyTrait(String objectName, TraitSequence<T> sequence, E modify, Remapper remapper) {
        for (T traitObject : sequence.getSequence()) {
            Target target = traitObject.getTarget();
            TargetType targetType = target.getTargetType();
            if (targetType.equals(TargetType.DIRECT) && !Objects.equals(targetType.getTarget(), objectName)) {
                continue;
            }

            modify = traitObject.modify(modify, remapper);
        }
        return modify;
    }

}