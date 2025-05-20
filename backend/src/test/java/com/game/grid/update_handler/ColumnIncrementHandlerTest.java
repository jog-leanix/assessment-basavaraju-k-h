package com.game.grid.update_handler;

import com.game.grid.model.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ColumnIncrementHandlerTest {
    @Mock
    private RowIncrementHandler rowIncrementHandler;
    @InjectMocks
    private ColumnIncrementHandler columnIncrementHandler;
    private List<List<Cell>> grid;

    @BeforeEach
    void beforeEachTest() {
        this.grid = List.of(
                List.of(Cell.builder().row(0).column(0).value(0).build(), Cell.builder().row(0).column(1).value(0).build(), Cell.builder().row(0).column(2).value(0).build()),
                List.of(Cell.builder().row(1).column(0).value(1).build(), Cell.builder().row(1).column(1).value(1).build(), Cell.builder().row(1).column(2).value(1).build()),
                List.of(Cell.builder().row(2).column(0).value(2).build(), Cell.builder().row(2).column(1).value(2).build(), Cell.builder().row(2).column(2).value(2).build())
        );
    }

    @Test
    void shouldUpdateAllCorrespondingColumn() {
        ArrayList<Cell> updatedCells = new ArrayList<>();
        List<List<Cell>> updatedGrid = List.of(
                List.of(Cell.builder().row(0).column(0).value(0).build(), Cell.builder().row(0).column(1).value(1).build(), Cell.builder().row(0).column(2).value(0).build()),
                List.of(Cell.builder().row(1).column(0).value(1).build(), Cell.builder().row(1).column(1).value(1).build(), Cell.builder().row(1).column(2).value(1).build()),
                List.of(Cell.builder().row(2).column(0).value(2).build(), Cell.builder().row(2).column(1).value(3).build(), Cell.builder().row(2).column(2).value(2).build())
        );
        List<Cell> expectedUpdatesCells = List.of(Cell.builder().row(0).column(1).value(1).build(), Cell.builder().row(2).column(1).value(3).build());

        this.columnIncrementHandler.handle(1, 1, this.grid, updatedCells);

        assertTrue(this.grid.containsAll(updatedGrid));
        assertThat(updatedCells, is(expectedUpdatesCells));
    }

    @Test
    void shouldInvokeNextRegisteredGridUpdaterWithUpdatedGridAndCells() {
        this.columnIncrementHandler.setNext(rowIncrementHandler);
        List<Cell> expectedUpdatesCells = List.of(Cell.builder().row(0).column(1).value(1).build(), Cell.builder().row(2).column(1).value(3).build());
        List<List<Cell>> updatedGrid = List.of(
                List.of(Cell.builder().row(0).column(0).value(0).build(), Cell.builder().row(0).column(1).value(1).build(), Cell.builder().row(0).column(2).value(0).build()),
                List.of(Cell.builder().row(1).column(0).value(1).build(), Cell.builder().row(1).column(1).value(1).build(), Cell.builder().row(1).column(2).value(1).build()),
                List.of(Cell.builder().row(2).column(0).value(2).build(), Cell.builder().row(2).column(1).value(3).build(), Cell.builder().row(2).column(2).value(2).build())
        );

        this.columnIncrementHandler.handle(1, 1, this.grid, new ArrayList<>());

        assertTrue(this.grid.containsAll(updatedGrid));
        verify(rowIncrementHandler, times(1)).handle(1, 1, updatedGrid, expectedUpdatesCells);
    }
}