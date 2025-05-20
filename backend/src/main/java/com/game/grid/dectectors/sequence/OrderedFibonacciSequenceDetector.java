package com.game.grid.dectectors.sequence;

import java.util.ArrayList;
import java.util.List;

public class OrderedFibonacciSequenceDetector implements SequenceDetector{
    private final List<Integer> fibonacciNumbers;

    public OrderedFibonacciSequenceDetector(int max) {
        this.fibonacciNumbers = generateFibonacciUpTo(max);
    }

    private List<Integer> generateFibonacciUpTo(int max) {
        List<Integer> fibonacciList = new ArrayList<>();
        int a = 0, b = 1;
        fibonacciList.add(a);
        fibonacciList.add(b);
        while (b <= max) {
            int next = a + b;
            fibonacciList.add(next);
            a = b;
            b = next;
        }
        return fibonacciList;
    }

    @Override
    public boolean isSequence(List<Integer> sequence) {
        for (int i = 0; i <= fibonacciNumbers.size() - sequence.size(); i++) {
            if (fibonacciNumbers.subList(i, i + sequence.size()).equals(sequence)) {
                return true;
            }
        }
        return false;
    }
}
