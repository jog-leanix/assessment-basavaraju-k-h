package com.game.grid.model;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class Cell {
    private int row;
    private int column;
    private int value;

    public void incrementValue() {
        this.value++;
    }

    public void resetValue() {
        this.value = 0;
    }
}
