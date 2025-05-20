package com.game.grid.validator.sequence;

import com.game.grid.dectectors.sequence.SequenceDetector;
import com.game.grid.model.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RowSequenceValidator implements SequenceValidator {

    private final int sequenceLength;

    public RowSequenceValidator(int sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    @Override
    public void validate(List<List<Cell>> grid, SequenceDetector detector, Set<Cell> matchedCells) {
        for (int row = 0; row < grid.size(); row++) {
            for (int column = 0; column <= grid.size() - sequenceLength; column++) {
                List<Integer> rowSequence = getRowSequence(grid, row, column).stream().toList();
                if (rowSequence.size() == sequenceLength && detector.isSequence(rowSequence)) {
                    for (int k = 0; k < sequenceLength; k++) {
                        matchedCells.add(new Cell(row, column + k, grid.get(row).get(column + k).getValue()));
                        grid.get(row).get(column + k).resetValue();
                    }
                }
            }
        }
    }

    private List<Integer> getRowSequence(List<List<Cell>> grid, int row, int column) {
        return IntStream.range(0, sequenceLength)
                .mapToObj(k -> grid.get(row).get(column + k).getValue())
                .collect(Collectors.toCollection(() -> new ArrayList<>(sequenceLength)));
    }
}
