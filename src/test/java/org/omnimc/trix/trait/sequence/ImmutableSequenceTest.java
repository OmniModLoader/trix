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