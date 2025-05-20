package com.game.grid.dectectors.sequence;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class OrderedFibonacciSequenceDetectorTest {
    SequenceDetector orderedFibonacciSequenceDetector = new OrderedFibonacciSequenceDetector(25);

    @ParameterizedTest
    @MethodSource("testData")
    void shouldReturnTrueWhenAllProvidedListOfValuesAreFibonacciSequence(List<Integer> values, boolean expectedResult) {
        assertThat(orderedFibonacciSequenceDetector.isSequence(values), is(expectedResult));
    }

    static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(Arrays.asList(1, 2, 3, 5, 8), true),
                Arguments.of(List.of(1,2), true),
                Arguments.of(List.of(1,1,2), true),
                Arguments.of(Arrays.asList(1, 2, 3, 6, 8), false),
                Arguments.of(Arrays.asList(8, 5, 3, 2, 1), false)

        );
    }

}