package com.game.grid.controller;

import com.game.grid.exception.GridNotExistException;
import com.game.grid.exception.GridRangeOutOfBoundException;
import com.game.grid.model.Cell;
import com.game.grid.model.dto.GridCreationRequest;
import com.game.grid.model.dto.GridUpdateRequest;
import com.game.grid.model.dto.GridUpdateResponse;
import com.game.grid.service.GridService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GridControllerTest {
    @Mock
    private GridService gridService;
    @InjectMocks
    private GridController gridController;

    @Test
    void shouldReturnGridWhenCreated() throws GridNotExistException {
        ArrayList<List<Cell>> expectedGrid = new ArrayList<>();
        Cell cell = new Cell(0, 0, 0);
        expectedGrid.add(List.of(cell));
        when(this.gridService.getGrid()).thenReturn(expectedGrid);

        ResponseEntity<List<List<Cell>>> response = this.gridController.getGrid();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(expectedGrid));
        verify(this.gridService, times(1)).getGrid();
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenGridIsNotCreated() throws GridNotExistException {
        when(this.gridService.getGrid()).thenThrow(new GridNotExistException("grid not created."));

        GridNotExistException gridNotExistException = assertThrows(GridNotExistException.class, () -> this.gridController.getGrid());

        assertThat(gridNotExistException.getMessage(), is("grid not created."));
        verify(this.gridService, times(1)).getGrid();
    }

    @Test
    void shouldCreateGridOfSpecifiedValidSize() {
        ArrayList<List<Cell>> expectedGrid = new ArrayList<>();
        Cell cell = new Cell(0, 0, 0);
        expectedGrid.add(List.of(cell));

        GridCreationRequest gridCreationRequest = GridCreationRequest.builder().size(1).build();
        when(this.gridService.createGrid(gridCreationRequest)).thenReturn(expectedGrid);

        ResponseEntity<List<List<Cell>>> response = this.gridController.createGrid(gridCreationRequest);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), is(expectedGrid));
        verify(this.gridService, times(1)).createGrid(gridCreationRequest);
    }

    @Test
    void shouldUpdateRelevantCellsOfGridOnUpdateRequest() throws GridNotExistException, GridRangeOutOfBoundException {
        GridUpdateRequest updateRequest = GridUpdateRequest.builder().row(1).column(2).build();
        GridUpdateResponse updateResponse = GridUpdateResponse.builder().grid(new ArrayList<>()).updatedCell(new ArrayList<>()).resetCell(new HashSet<>()).build();
        when(this.gridService.updateGrid(updateRequest)).thenReturn(updateResponse);

        ResponseEntity<GridUpdateResponse> response = this.gridController.updateGrid(updateRequest);

        verify(this.gridService, times(1)).updateGrid(updateRequest);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(updateResponse));
    }

    @Test
    void shouldThrowResponseStatusExceptionOnUpdateGridWhenGridIsNotCreated() throws GridNotExistException, GridRangeOutOfBoundException {
        GridUpdateRequest updateRequest = GridUpdateRequest.builder().row(1).column(2).build();
        when(this.gridService.updateGrid(updateRequest)).thenThrow(new GridNotExistException("grid not created."));

        GridNotExistException gridNotExistException = assertThrows(GridNotExistException.class, () -> this.gridController.updateGrid(updateRequest));

        assertThat(gridNotExistException.getMessage(), is("grid not created."));
        verify(this.gridService, times(1)).updateGrid(updateRequest);
    }

    @Test
    void shouldResetTheGrid() throws GridNotExistException {
        GridUpdateResponse updateResponse = GridUpdateResponse.builder().grid(new ArrayList<>()).updatedCell(new ArrayList<>()).resetCell(new HashSet<>()).build();
        when(this.gridService.resetGrid()).thenReturn(updateResponse);

        ResponseEntity<GridUpdateResponse> response = this.gridController.resetGrid();

        verify(this.gridService, times(1)).resetGrid();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(updateResponse));
    }
}
