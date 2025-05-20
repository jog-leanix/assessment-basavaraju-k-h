package com.game.grid.service;

import com.game.grid.dectectors.sequence.SequenceDetector;
import com.game.grid.model.Cell;
import com.game.grid.model.dto.GridUpdateRequest;
import com.game.grid.model.dto.GridUpdateResponse;
import com.game.grid.update_handler.GridUpdateHandler;
import com.game.grid.validator.sequence.SequenceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GridUpdateService {
    private final GridUpdateHandler gridUpdateHandlerChain;
    private final SequenceDetector orderedFibonacciSequenceDetector;
    private final List<SequenceValidator> sequenceOfFixedLengthIdentifiers;

    public GridUpdateResponse updateGrid(GridUpdateRequest gridUpdateRequest, List<List<Cell>> grid) {
        ArrayList<Cell> updatedCells = new ArrayList<>();
        this.gridUpdateHandlerChain.handle(gridUpdateRequest.getRow(), gridUpdateRequest.getColumn(), grid, updatedCells);
        HashSet<Cell> fibonacciCells = detectAndResetFibonacciCells(grid);
        return new GridUpdateResponse(grid, updatedCells, fibonacciCells);
    }

    private HashSet<Cell> detectAndResetFibonacciCells(List<List<Cell>> grid) {
        HashSet<Cell> sequencedCells = new HashSet<>();
        sequenceOfFixedLengthIdentifiers.forEach(sequenceValidator ->
                    sequenceValidator.validate(grid, this.orderedFibonacciSequenceDetector, sequencedCells)
                );
        return sequencedCells;
    }
}
