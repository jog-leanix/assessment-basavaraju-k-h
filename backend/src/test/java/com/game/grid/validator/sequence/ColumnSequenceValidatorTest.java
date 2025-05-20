package com.game.grid.validator.sequence;

import com.game.grid.dectectors.sequence.FibonacciSequenceDetector;
import com.game.grid.model.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ColumnSequenceValidatorTest {
    @Mock
    private FibonacciSequenceDetector fibonacciSequenceDetector;
    ColumnSequenceValidator columnSequenceValidator;
    private List<List<Cell>> grid;


    @BeforeEach
    void beforeEachTest() {
        this.grid = List.of(
                List.of(Cell.builder().row(0).column(0).value(1).build(), Cell.builder().row(0).column(1).value(1).build(), Cell.builder().row(0).column(2).value(1).build(), Cell.builder().row(0).column(3).value(1).build(), Cell.builder().row(0).column(4).value(1).build()),
                List.of(Cell.builder().row(1).column(0).value(2).build(), Cell.builder().row(1).column(1).value(2).build(), Cell.builder().row(1).column(2).value(2).build(), Cell.builder().row(1).column(3).value(2).build(), Cell.builder().row(1).column(4).value(2).build()),
                List.of(Cell.builder().row(2).column(0).value(3).build(), Cell.builder().row(2).column(1).value(3).build(), Cell.builder().row(2).column(2).value(3).build(), Cell.builder().row(2).column(3).value(3).build(), Cell.builder().row(2).column(4).value(3).build()),
                List.of(Cell.builder().row(3).column(0).value(4).build(), Cell.builder().row(3).column(1).value(4).build(), Cell.builder().row(3).column(2).value(4).build(), Cell.builder().row(3).column(3).value(4).build(), Cell.builder().row(3).column(4).value(4).build()),
                List.of(Cell.builder().row(4).column(0).value(5).build(), Cell.builder().row(4).column(1).value(5).build(), Cell.builder().row(4).column(2).value(5).build(), Cell.builder().row(4).column(3).value(5).build(), Cell.builder().row(4).column(4).value(5).build())
        );
        columnSequenceValidator = new ColumnSequenceValidator(2);
    }

    @Test
    void shouldFindTheSequenceOfSpecifiedLengthAndResetTheCellValuesWhenFound() {
        HashSet<Cell> initialChanges = new HashSet<>();
        List<List<Cell>> expectedGrid = List.of(
                List.of(Cell.builder().row(0).column(0).value(0).build(), Cell.builder().row(0).column(1).value(0).build(), Cell.builder().row(0).column(2).value(0).build(), Cell.builder().row(0).column(3).value(0).build(), Cell.builder().row(0).column(4).value(0).build()),
                List.of(Cell.builder().row(1).column(0).value(0).build(), Cell.builder().row(1).column(1).value(0).build(), Cell.builder().row(1).column(2).value(0).build(), Cell.builder().row(1).column(3).value(0).build(), Cell.builder().row(1).column(4).value(0).build()),
                List.of(Cell.builder().row(2).column(0).value(3).build(), Cell.builder().row(2).column(1).value(3).build(), Cell.builder().row(2).column(2).value(3).build(), Cell.builder().row(2).column(3).value(3).build(), Cell.builder().row(2).column(4).value(3).build()),
                List.of(Cell.builder().row(3).column(0).value(4).build(), Cell.builder().row(3).column(1).value(4).build(), Cell.builder().row(3).column(2).value(4).build(), Cell.builder().row(3).column(3).value(4).build(), Cell.builder().row(3).column(4).value(4).build()),
                List.of(Cell.builder().row(4).column(0).value(5).build(), Cell.builder().row(4).column(1).value(5).build(), Cell.builder().row(4).column(2).value(5).build(), Cell.builder().row(4).column(3).value(5).build(), Cell.builder().row(4).column(4).value(5).build())
        );
        List<Cell> expectedChanges = List.of(
                Cell.builder().row(0).column(0).value(1).build(), Cell.builder().row(1).column(0).value(2).build(),
                Cell.builder().row(0).column(1).value(1).build(), Cell.builder().row(1).column(1).value(2).build(),
                Cell.builder().row(0).column(2).value(1).build(), Cell.builder().row(1).column(2).value(2).build(),
                Cell.builder().row(0).column(3).value(1).build(), Cell.builder().row(1).column(3).value(2).build(),
                Cell.builder().row(0).column(4).value(1).build(), Cell.builder().row(1).column(4).value(2).build()
        );
        when(fibonacciSequenceDetector.isSequence(anyList()))
                .thenReturn(true).thenReturn(false).thenReturn(false).thenReturn(false)
                .thenReturn(true).thenReturn(false).thenReturn(false).thenReturn(false)
                .thenReturn(true).thenReturn(false).thenReturn(false).thenReturn(false)
                .thenReturn(true).thenReturn(false).thenReturn(false).thenReturn(false)
                .thenReturn(true).thenReturn(false).thenReturn(false).thenReturn(false);

        this.columnSequenceValidator.validate(this.grid, fibonacciSequenceDetector, initialChanges);

        assertTrue(this.grid.containsAll(expectedGrid));
        assertTrue(initialChanges.containsAll(expectedChanges));
    }

}