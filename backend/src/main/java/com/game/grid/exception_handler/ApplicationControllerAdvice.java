package com.game.grid.exception_handler;

import com.game.grid.exception.GridNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@ControllerAdvice
@ResponseBody
public class ApplicationControllerAdvice {

    @ExceptionHandler(GridNotExistException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleGridNotExistException(GridNotExistException exception){
        return "Grid not created yet, please use POST /api/v1/grid to create grid.";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(exception.getReason());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(MethodArgumentNotValidException exception) {
        HashMap<String, String> errorMap = new HashMap<>();
        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap.toString();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleMessageNotReadableException(HttpMessageNotReadableException exception) {
        return exception.getMessage();
    }
}
