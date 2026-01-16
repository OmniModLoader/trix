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