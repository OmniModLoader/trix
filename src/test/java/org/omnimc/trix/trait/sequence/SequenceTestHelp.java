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