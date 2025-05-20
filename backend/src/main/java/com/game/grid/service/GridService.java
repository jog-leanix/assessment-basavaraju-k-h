package com.game.grid.service;

import com.game.grid.exception.GridNotExistException;
import com.game.grid.exception.GridRangeOutOfBoundException;
import com.game.grid.model.Cell;
import com.game.grid.model.dto.GridCreationRequest;
import com.game.grid.model.dto.GridUpdateRequest;
import com.game.grid.model.dto.GridUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GridService {
    private List<List<Cell>> grid;
    private final GridUpdateService gridUpdateService;

    public List<List<Cell>> createGrid(GridCreationRequest gridCreationRequest) {
        grid = new ArrayList<>();
        for (int row = 0; row < gridCreationRequest.getSize(); row++) {
            List<Cell> rowList = new ArrayList<>();
            for (int column = 0; column < gridCreationRequest.getSize(); column++) {
                rowList.add(new Cell(row, column, 0));
            }
            grid.add(rowList);
        }
        return grid;
    }

    public List<List<Cell>> getGrid() throws GridNotExistException {
        if (this.grid == null) throw new GridNotExistException("Grid not created.");
        else return this.grid;
    }

    public GridUpdateResponse updateGrid(GridUpdateRequest gridUpdateRequest) throws GridNotExistException, GridRangeOutOfBoundException {
        List<List<Cell>> grid = this.getGrid();
        if(gridUpdateRequest.getRow() > grid.size() || gridUpdateRequest.getColumn() > grid.size())
            throw new GridRangeOutOfBoundException("Grid row/column is out of bound.");
        return this.gridUpdateService.updateGrid(gridUpdateRequest, grid);
    }

    public GridUpdateResponse resetGrid() {
        this.grid.forEach(row -> row.forEach(Cell::resetValue));
        return new GridUpdateResponse(this.grid, Collections.emptyList(), Collections.emptySet());
    }
}
