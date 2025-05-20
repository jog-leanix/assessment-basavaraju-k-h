package com.game.grid.service;

import com.game.grid.exception.GridNotExistException;
import com.game.grid.exception.GridRangeOutOfBoundException;
import com.game.grid.model.Cell;
import com.game.grid.model.dto.GridCreationRequest;
import com.game.grid.model.dto.GridUpdateRequest;
import com.game.grid.model.dto.GridUpdateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GridServiceTest {
    @Mock
    private GridUpdateService gridUpdateService;
    @InjectMocks
    private GridService gridService;

    @Test
    public void shouldGetGridOfRequestedRowAndColumnSize() throws GridNotExistException {
        GridCreationRequest gridCreationRequest = GridCreationRequest.builder().size(50).build();
        this.gridService.createGrid(gridCreationRequest);
        List<List<Cell>> grid = this.gridService.getGrid();

        assertThat(grid.size(), is(50));
        assertThat(grid.get(0).size(), is(50));
    }

    @Test
    public void shouldReturnOptionalEmptyIfGridIsNotCreated() {
        assertThrows(GridNotExistException.class, () -> this.gridService.getGrid());

    }

    @Test
    public void shouldCreateGridOfRequestedSize() {
        GridCreationRequest gridCreationRequest = GridCreationRequest.builder().size(50).build();
        List<List<Cell>> grid = this.gridService.createGrid(gridCreationRequest);

        assertThat(grid.size(), is(50));
        assertThat(grid.get(0).size(), is(50));
    }

    @Test
    void shouldUpdateGridCells() throws GridNotExistException, GridRangeOutOfBoundException {
        ArrayList<List<Cell>> grid = new ArrayList<>();
        ReflectionTestUtils.setField(this.gridService, "grid", grid);
        GridUpdateRequest updateRequest = GridUpdateRequest.builder().row(0).column(0).build();
        GridUpdateResponse expectedResponse = GridUpdateResponse.builder().build();
        when(this.gridUpdateService.updateGrid(updateRequest, grid)).thenReturn(expectedResponse);

        GridUpdateResponse actualResponse = this.gridService.updateGrid(updateRequest);

        verify(this.gridUpdateService, times(1)).updateGrid(updateRequest, grid);
        assertThat(expectedResponse, is(actualResponse));
    }

    @Test
    void shouldThrowGridNotExistExceptionOnAttemptToUpdateEmptyGrid() {
        GridUpdateRequest updateRequest = GridUpdateRequest.builder().row(0).column(0).build();

        assertThrows(GridNotExistException.class, () -> this.gridService.updateGrid(updateRequest));

        verifyNoInteractions(this.gridUpdateService);
    }

    @Test
    void shouldThrowGridRangeOutOfBoundExceptionOnUpdatingGridWithInvalidCellRowOrColumn() {
        ArrayList<List<Cell>> grid = new ArrayList<>();
        grid.add(List.of(Cell.builder().value(1).row(0).column(0).build(), Cell.builder().value(1).row(0).column(1).build()));
        ReflectionTestUtils.setField(this.gridService, "grid", grid);
        GridUpdateRequest updateRequest = GridUpdateRequest.builder().row(5).column(0).build();

        assertThrows(GridRangeOutOfBoundException.class, () -> this.gridService.updateGrid(updateRequest));

        verifyNoInteractions(this.gridUpdateService);
    }


    @Test
    void shouldResetGrid() {
        this.gridService.createGrid(GridCreationRequest.builder().size(5).build());

        GridUpdateResponse gridUpdateResponse = this.gridService.resetGrid();

        gridUpdateResponse.getGrid().forEach(row -> row.forEach(cell ->
                assertThat(cell.getValue(), is(0))
        ));
    }
}