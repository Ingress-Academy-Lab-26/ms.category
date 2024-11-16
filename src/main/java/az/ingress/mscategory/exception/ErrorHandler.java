package az.ingress.mscategory.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import static az.ingress.mscategory.exception.ExceptionConstraints.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ExceptionResponse handle(Exception ex) {
        log.error("Exception: ", ex);
        return new ExceptionResponse(UNEXPECTED_EXCEPTION_CODE, UNEXPECTED_EXCEPTION_MESSAGE);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse handle(NotFoundException ex) {
        log.error("NotFoundException: ", ex);
        return new ExceptionResponse(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(CannotDeleteSubCategoryException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handle(CannotDeleteSubCategoryException ex) {
        log.error("CannotDeleteSubCategoryException: ", ex);
        return new ExceptionResponse(ex.getMessage(), ex.getCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handle(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException: ", ex);
        var errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessage.append(String.format("[%s: %s] ", error.getField(), error.getDefaultMessage()))
        );
        return new ExceptionResponse(VALIDATION_EXCEPTION_CODE, errorMessage.toString().trim());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ExceptionResponse handle(HttpRequestMethodNotSupportedException ex) {
        log.error("HttpRequestMethodNotSupportedException: ", ex);
        return new ExceptionResponse(METHOD_NOT_ALLOWED_CODE, METHOD_NOT_ALLOWED_CODE_MESSAGE);
    }

    @ExceptionHandler(CustomFeignException.class)
    public ResponseEntity<ExceptionResponse> handle(CustomFeignException ex) {
        log.error("CustomFeignException: ", ex);
        return ResponseEntity
                .status(ex.getStatus())
                .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ExceptionResponse handle(AuthException ex) {
        log.error("AuthException: ", ex);
        return new ExceptionResponse(USER_UNAUTHORIZED_CODE, USER_UNAUTHORIZED_MESSAGE);
    }

}
