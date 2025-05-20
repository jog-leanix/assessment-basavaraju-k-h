package com.game.grid.update_handler;

import com.game.grid.model.Cell;

import java.util.List;

public interface GridUpdateHandler {
    void setNext(GridUpdateHandler next);

    void handle(int row, int column, List<List<Cell>> grid, List<Cell> updatedCells);
}
