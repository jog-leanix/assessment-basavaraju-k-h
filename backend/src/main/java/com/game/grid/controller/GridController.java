package com.game.grid.controller;

import com.game.grid.exception.GridNotExistException;
import com.game.grid.exception.GridRangeOutOfBoundException;
import com.game.grid.model.Cell;
import com.game.grid.model.dto.GridCreationRequest;
import com.game.grid.model.dto.GridUpdateRequest;
import com.game.grid.model.dto.GridUpdateResponse;
import com.game.grid.service.GridService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/grid")
@RequiredArgsConstructor
public class GridController {
    private final GridService gridService;

    @GetMapping
    public ResponseEntity<List<List<Cell>>> getGrid() throws GridNotExistException {
        List<List<Cell>> grid = this.gridService.getGrid();
        return ResponseEntity.ok(grid);
    }

    @PostMapping
    public ResponseEntity<List<List<Cell>>> createGrid(@Valid @RequestBody GridCreationRequest gridCreationRequest) {
        List<List<Cell>> grid = this.gridService.createGrid(gridCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(grid);
    }

    @PutMapping
    public ResponseEntity<GridUpdateResponse> updateGrid(@Valid @RequestBody GridUpdateRequest gridUpdateRequest) throws GridNotExistException {
        try {
            return ResponseEntity.ok(this.gridService.updateGrid(gridUpdateRequest));
        } catch (GridRangeOutOfBoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping(headers = "x-type=reset")
    public ResponseEntity<GridUpdateResponse> resetGrid() throws GridNotExistException {
        this.gridService.getGrid();
        return ResponseEntity.ok(this.gridService.resetGrid());
    }
}
