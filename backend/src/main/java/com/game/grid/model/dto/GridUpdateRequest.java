package com.game.grid.model.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GridUpdateRequest {
    @PositiveOrZero(message = "row number should be valid integer number")
    private int row;
    @PositiveOrZero(message = "column number should be valid integer number")
    private int column;
}
