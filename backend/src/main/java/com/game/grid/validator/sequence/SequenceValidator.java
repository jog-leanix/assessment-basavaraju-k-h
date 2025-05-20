package com.game.grid.validator.sequence;

import com.game.grid.dectectors.sequence.SequenceDetector;
import com.game.grid.model.Cell;

import java.util.List;
import java.util.Set;

public interface SequenceValidator {
    void validate(List<List<Cell>> grid, SequenceDetector detector, Set<Cell> matchedCells);
}
