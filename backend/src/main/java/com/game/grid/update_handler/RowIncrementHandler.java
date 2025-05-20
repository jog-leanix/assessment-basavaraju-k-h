package com.game.grid.update_handler;

import com.game.grid.model.Cell;

import java.util.List;

public class RowIncrementHandler implements GridUpdateHandler {
    private GridUpdateHandler next;

    @Override
    public void setNext(GridUpdateHandler next) {
        this.next = next;
    }

    @Override
    public void handle(int row, int column, List<List<Cell>> grid, List<Cell> updatedCells) {
        List<Cell> rowCells = grid.get(row);
        for (Cell cell : rowCells) {
            cell.incrementValue();
            updatedCells.add(new Cell(cell.getRow(), cell.getColumn(), cell.getValue()));
        }
        if (next != null) next.handle(row, column, grid, updatedCells);
    }
}
