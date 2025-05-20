package com.game.grid.model.dto;

import com.game.grid.model.Cell;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GridUpdateResponse {
    private List<List<Cell>> grid;
    private List<Cell> updatedCell;
    private Set<Cell> resetCell;
}
