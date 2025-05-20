package com.game.grid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.grid.exception.GridNotExistException;
import com.game.grid.exception.GridRangeOutOfBoundException;
import com.game.grid.exception_handler.ApplicationControllerAdvice;
import com.game.grid.model.Cell;
import com.game.grid.model.dto.GridCreationRequest;
import com.game.grid.model.dto.GridUpdateRequest;
import com.game.grid.model.dto.GridUpdateResponse;
import com.game.grid.service.GridService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GridController.class)
@ContextConfiguration(classes = {GridController.class, ApplicationControllerAdvice.class})
class GridControllerMvcTest {
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockitoBean
    private GridService gridService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    void shouldReturnGridOnGetRequestWhenGridIsCreated() throws Exception {
        ArrayList<List<Cell>> grid = new ArrayList<>();
        Cell cell = new Cell(0, 0, 0);
        grid.add(List.of(cell));
        when(this.gridService.getGrid()).thenReturn(grid);

        this.mockMvc.perform(get("/api/v1/grid")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("[[{\"row\":0,\"column\":0,\"value\":0}]]"));
    }

    @Test
    void shouldReturnNotFoundOnGetRequestWhenGridIsNotCreated() throws Exception {
        when(this.gridService.getGrid()).thenThrow(new GridNotExistException("grid not created"));

        this.mockMvc.perform(get("/api/v1/grid")
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Grid not created yet, please use POST /api/v1/grid to create grid."));
    }

    @ParameterizedTest
    @ValueSource(ints = {-3, 0})
    void shouldReturnBadRequestWhenGridSizeIsInvalid(int size) throws Exception {
        GridCreationRequest gridCreationRequest = GridCreationRequest.builder().size(size).build();
        this.mockMvc.perform(post("/api/v1/grid")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(gridCreationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{size=size should be positive integer number}"));

        verifyNoInteractions(this.gridService);
    }

    @Test
    void shouldReturnBadRequestWhenGridSizeIsNullOrEmptyOrInvalidIntegerValue() throws Exception {
        this.mockMvc.perform(post("/api/v1/grid")
                        .contentType("application/json")
                        .content("""
                                {"size":""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{size=size should be positive integer number}"));

        this.mockMvc.perform(post("/api/v1/grid")
                        .contentType("application/json")
                        .content("""
                                {"size":null}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{size=size should be positive integer number}"));

        this.mockMvc.perform(post("/api/v1/grid")
                        .contentType("application/json")
                        .content("""
                                {"size":"1.5"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("JSON parse error: Cannot deserialize value of type `int` from String \"1.5\": not a valid `int` value"));

        this.mockMvc.perform(post("/api/v1/grid")
                        .contentType("application/json")
                        .content("""
                                {"size":"I am not a number"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("JSON parse error: Cannot deserialize value of type `int` from String \"I am not a number\": not a valid `int` value"));

        verifyNoInteractions(this.gridService);
    }

    @Test
    void shouldReturnCreatedWithGridDetailsForRequestedValidSize() throws Exception {
        GridCreationRequest gridCreationRequest = GridCreationRequest.builder().size(1).build();
        ArrayList<List<Cell>> grid = new ArrayList<>();
        Cell cell = new Cell(0, 0, 0);
        grid.add(List.of(cell));
        when(this.gridService.createGrid(gridCreationRequest)).thenReturn(grid);

        this.mockMvc.perform(post("/api/v1/grid")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(gridCreationRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("[[{\"row\":0,\"column\":0,\"value\":0}]]"));

        verify(this.gridService, times(1)).createGrid(gridCreationRequest);
    }

    @Test
    void shouldReturnOkOnGridUpdateRequestModifiedRelevantCells() throws Exception {
        GridUpdateRequest updateRequest = GridUpdateRequest.builder().row(1).column(2).build();
        GridUpdateResponse updateResponse = GridUpdateResponse.builder().grid(new ArrayList<>()).updatedCell(new ArrayList<>()).resetCell(new HashSet<>()).build();
        when(this.gridService.updateGrid(updateRequest)).thenReturn(updateResponse);
        this.mockMvc.perform(put("/api/v1/grid")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"grid\":[],\"updatedCell\":[],\"resetCell\":[]}"));

        verify(this.gridService, times(1)).updateGrid(updateRequest);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenCellRowOrColumnValuesAreNonIntegersOnUpdateRequest() throws Exception {
        this.mockMvc.perform(put("/api/v1/grid")
                        .contentType("application/json")
                        .content("""
                                {
                                  "row" : -1,
                                  "column" : 0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{row=row number should be valid integer number}"));
    }

    @Test
    void shouldReturnGridNotExistExceptionOnAttemptToUpdateEmptyGrid() throws Exception {
        GridUpdateRequest updateRequest = GridUpdateRequest.builder().row(1).column(0).build();
        when(this.gridService.updateGrid(updateRequest)).thenThrow(new GridNotExistException("Grid not created yet, please use POST /api/v1/grid to create grid."));

        this.mockMvc.perform(put("/api/v1/grid")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Grid not created yet, please use POST /api/v1/grid to create grid."));

        verify(this.gridService, times(1)).updateGrid(updateRequest);
    }

    @Test
    void shouldReturnGridRangeOutOfBoundExceptionOnRowOrColumnOutOfRange() throws Exception {
        GridUpdateRequest updateRequest = GridUpdateRequest.builder().row(1).column(0).build();
        when(this.gridService.updateGrid(updateRequest)).thenThrow(new GridRangeOutOfBoundException("Invalid row/column"));

        this.mockMvc.perform(put("/api/v1/grid")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid row/column"));

        verify(this.gridService, times(1)).updateGrid(updateRequest);
    }

    @Test
    void shouldResetGridCellValues() throws Exception {
        ArrayList<List<Cell>> grid = new ArrayList<>();
        Cell cell = new Cell(0, 0, 0);
        grid.add(List.of(cell));
        when(this.gridService.resetGrid()).thenReturn(
                GridUpdateResponse.builder()
                        .grid(grid)
                        .resetCell(Collections.emptySet())
                        .updatedCell(Collections.emptyList())
                        .build()
        );

        this.mockMvc.perform(put("/api/v1/grid")
                        .contentType("application/json")
                        .header("X-Type", "reset")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("{\"grid\":[[{\"row\":0,\"column\":0,\"value\":0}]],\"updatedCell\":[],\"resetCell\":[]}"));
    }

    @Test
    void shouldThrowGridNotFoundExceptionOnAttemptToResetTheGrid() throws Exception {
        when(this.gridService.getGrid()).thenThrow(new GridNotExistException("Grid not created yet, please use POST /api/v1/grid to create grid."));

        this.mockMvc.perform(put("/api/v1/grid")
                        .contentType("application/json")
                        .header("X-Type", "reset")
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string("Grid not created yet, please use POST /api/v1/grid to create grid."));
    }
}