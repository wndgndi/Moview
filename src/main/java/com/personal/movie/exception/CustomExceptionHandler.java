package com.personal.movie.exception;

import static com.personal.movie.domain.constants.ErrorCode.INTERNAL_SERVER_ERROR;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return new ResponseEntity<>(
            new ErrorResponse(e.getErrorCode().getStatus(), e.getErrorCode().getDescription()),
            HttpStatus.valueOf(e.getErrorCode().getStatus())
        );
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ErrorResponse> handleServerException(Exception ex) {
        return new ResponseEntity<>(
            new ErrorResponse(INTERNAL_SERVER_ERROR.getStatus(), ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
