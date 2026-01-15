package org.omnimc.trix.trait.sequence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.omnimc.trix.api.trait.clazz.ClassTrait;
import org.omnimc.trix.api.trait.sequence.MutableTraitSequence;
import org.omnimc.trix.api.trait.sequence.TraitSequence;

import java.util.Arrays;

/**
 * @author <a href=https://github.com/CadenCCC>Caden</a>
 * @since 2.0.0
 */
public class MutableSequenceTest {

    @Test
    public void testMutablePriority() {
        TraitSequence<ClassTrait> sequence = TraitSequence.createMutableTraitSequence(SequenceTestHelp.unsortedClassTraits);

        int[] actualPriorities = Arrays.stream(sequence.getSequence())
                .mapToInt(trait -> trait.getTarget().getPriority())
                .toArray();

        int[] expectedPriorities = Arrays.stream(SequenceTestHelp.sortedClassTraits)
                .mapToInt(trait -> trait.getTarget().getPriority())
                .toArray();

        Assertions.assertArrayEquals(expectedPriorities, actualPriorities,
                                     "Priorities should be sorted from highest to lowest.");
    }

    @Test
    public void testAddAndMutablePriority() {
        ClassTrait newClassTrait = SequenceTestHelp.createStupidClassTrait(28);

        MutableTraitSequence<ClassTrait> sequence = TraitSequence.createMutableTraitSequence(SequenceTestHelp.unsortedClassTraits);

        sequence.add(newClassTrait);

        Assertions.assertEquals(SequenceTestHelp.unsortedClassTraits.length + 1, sequence.getSize(),
                                "sequence 'add' is not updating the list.");
    }

    @Test
    public void testClear() {
        MutableTraitSequence<ClassTrait> sequence = TraitSequence.createMutableTraitSequence(SequenceTestHelp.unsortedClassTraits);
        
        sequence.clear();

        ClassTrait[] traits = sequence.getSequence();

        Assertions.assertNull(traits, "sequence 'clear' is not null.");
    }
}