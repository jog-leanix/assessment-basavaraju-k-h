package com.game.grid.validator.sequence;

import com.game.grid.dectectors.sequence.SequenceDetector;
import com.game.grid.model.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ColumnSequenceValidator implements SequenceValidator {

    private final int sequenceLength;

    public ColumnSequenceValidator(int sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    @Override
    public void validate(List<List<Cell>> grid, SequenceDetector detector, Set<Cell> matchedCells) {
        for (int column = 0; column < grid.size(); column++) {
            for (int row = 0; row <= grid.size() - sequenceLength; row++) {
                List<Integer> columnSequence = getColumnValues(grid, row, column).stream().toList();
                if (columnSequence.size() == sequenceLength && detector.isSequence(columnSequence)) {
                    for (int k = 0; k < sequenceLength; k++) {
                        matchedCells.add(new Cell(row + k, column, grid.get(row + k).get(column).getValue()));
                        grid.get(row + k).get(column).resetValue();
                    }
                }
            }
        }
    }

    private List<Integer> getColumnValues(List<List<Cell>> grid, int row, int column) {
        List<Integer> integers = new ArrayList<>(sequenceLength);
        for (int k = 0; k < sequenceLength; k++) {
            Integer value = grid.get(row + k).get(column).getValue();
            integers.add(value);
        }
        return integers;
    }
}
