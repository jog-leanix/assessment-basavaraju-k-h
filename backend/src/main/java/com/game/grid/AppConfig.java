package com.game.grid;

import com.game.grid.dectectors.sequence.FibonacciSequenceDetector;
import com.game.grid.dectectors.sequence.OrderedFibonacciSequenceDetector;
import com.game.grid.dectectors.sequence.SequenceDetector;
import com.game.grid.update_handler.ColumnIncrementHandler;
import com.game.grid.update_handler.GridUpdateHandler;
import com.game.grid.update_handler.RowIncrementHandler;
import com.game.grid.validator.sequence.ColumnSequenceValidator;
import com.game.grid.validator.sequence.RowSequenceValidator;
import com.game.grid.validator.sequence.SequenceValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.List;

@Configuration
public class AppConfig {
    @Value("${sequence.fibonacci.length}")
    private int sequenceLength;

    @Bean
    public GridUpdateHandler rowIncrementHandler() {
        return new RowIncrementHandler();
    }

    @Bean
    public GridUpdateHandler columnIncrementHandler() {
        return new ColumnIncrementHandler();
    }

    @Bean
    @DependsOn({"rowIncrementHandler", "columnIncrementHandler"})
    public GridUpdateHandler gridUpdateHandlerChain(
            @Qualifier("rowIncrementHandler") GridUpdateHandler rowIncrementHandler,
            @Qualifier("columnIncrementHandler") GridUpdateHandler columnIncrementHandler
    ) {
        rowIncrementHandler.setNext(columnIncrementHandler);
        return rowIncrementHandler;
    }

    @Bean
    public SequenceValidator rowSequenceValidator() {
        return new RowSequenceValidator(sequenceLength);
    }

    @Bean
    public SequenceValidator columnSequenceValidator() {
        return new ColumnSequenceValidator(sequenceLength);
    }

    @Bean
    @DependsOn({"rowSequenceValidator", "columnSequenceValidator"})
    public List<SequenceValidator> sequenceOfFixedLengthIdentifiers(
            @Qualifier("rowSequenceValidator") SequenceValidator rowSequenceValidator,
            @Qualifier("columnSequenceValidator") SequenceValidator columnSequenceValidator
    ) {
        return List.of(rowSequenceValidator, columnSequenceValidator);
    }

    @Bean
    public SequenceDetector fibonacciSequenceDetector() {
        return new FibonacciSequenceDetector(10000);
    }

    @Bean
    public SequenceDetector orderedFibonacciSequenceDetector() {
        return new OrderedFibonacciSequenceDetector(10000);
    }
}
