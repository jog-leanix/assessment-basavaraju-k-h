package com.game.grid.update_handler;

import com.game.grid.model.Cell;

import java.util.List;

public class ColumnIncrementHandler implements GridUpdateHandler {
    private GridUpdateHandler next;

    @Override
    public void setNext(GridUpdateHandler next) {
        this.next = next;
    }

    @Override
    public void handle(int row, int column, List<List<Cell>> grid, List<Cell> updatedCells) {
        for (int i = 0; i < grid.size(); i++) {
            if(i != row){
                Cell cell = grid.get(i).get(column);
                cell.incrementValue();
                updatedCells.add(new Cell(cell.getRow(), cell.getColumn(), cell.getValue()));
            }
        }
        if (next != null) next.handle(row, column, grid, updatedCells);
    }
}
