package com.game.grid.service;

import com.game.grid.dectectors.sequence.SequenceDetector;
import com.game.grid.model.Cell;
import com.game.grid.model.dto.GridUpdateRequest;
import com.game.grid.model.dto.GridUpdateResponse;
import com.game.grid.update_handler.GridUpdateHandler;
import com.game.grid.validator.sequence.ColumnSequenceValidator;
import com.game.grid.validator.sequence.RowSequenceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GridUpdateServiceTest {
    private GridUpdateService gridUpdateService;
    @Mock
    private GridUpdateHandler gridUpdateHandlerChain;
    @Mock
    private SequenceDetector fibonacciSequenceDetector;
    @Mock
    private RowSequenceValidator rowSequenceValidator;
    @Mock
    private ColumnSequenceValidator columnSequenceValidator;

    @BeforeEach
    void beforeEachTest() {
        gridUpdateService = new GridUpdateService(
                gridUpdateHandlerChain,
                fibonacciSequenceDetector,
                List.of(rowSequenceValidator, columnSequenceValidator)
        );
    }

    @Test
    public void shouldUpdateGridByUpdateHandlerAndResetIfSequenceDetected() {
        GridUpdateRequest updateRequest = GridUpdateRequest.builder().row(0).column(0).build();
        List<List<Cell>> grid = List.of(
                List.of(Cell.builder().row(0).column(0).value(0).build(), Cell.builder().row(0).column(1).value(1).build()),
                List.of(Cell.builder().row(1).column(0).value(2).build(), Cell.builder().row(1).column(1).value(3).build())
        );
        ArrayList<Cell> updatedCells = new ArrayList<>();
        HashSet<Cell> matchedCells = new HashSet<>();
        GridUpdateResponse expectedResponse = GridUpdateResponse.builder().grid(grid).updatedCell(updatedCells).resetCell(matchedCells).build();

        GridUpdateResponse actualResponse = this.gridUpdateService.updateGrid(updateRequest, grid);

        verify(this.gridUpdateHandlerChain, times(1)).handle(updateRequest.getRow(), updateRequest.getColumn(), grid, updatedCells);
        verify(this.rowSequenceValidator, times(1)).validate(grid, fibonacciSequenceDetector, matchedCells);
        verify(this.columnSequenceValidator, times(1)).validate(grid, fibonacciSequenceDetector, matchedCells);
        assertThat(expectedResponse, is(actualResponse));
    }
}