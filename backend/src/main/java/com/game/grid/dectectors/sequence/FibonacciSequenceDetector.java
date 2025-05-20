package com.game.grid.dectectors.sequence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FibonacciSequenceDetector implements SequenceDetector {
    private final Set<Integer> fibonacciNumbers;

    public FibonacciSequenceDetector(int max) {
        fibonacciNumbers = generateFibonacciUpTo(max);
    }

    private Set<Integer> generateFibonacciUpTo(int max) {
        Set<Integer> fibonacciSet = new HashSet<>();
        int a = 0, b = 1;
        fibonacciSet.add(a);
        fibonacciSet.add(b);
        while (b <= max) {
            int next = a + b;
            fibonacciSet.add(next);
            a = b;
            b = next;
        }
        return fibonacciSet;
    }

    @Override
    public boolean isSequence(List<Integer> sequence) {
        return fibonacciNumbers.containsAll(sequence);
    }
}
