package lt.jonas.playground.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.apachecommons.CommonsLog;
import lt.jonas.playground.exception.ConditionsForActionNotMetException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
@CommonsLog
public class GeneralExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getAllErrors().stream().reduce("", (result, error) -> result + error.getDefaultMessage() + "\r\n", (result1, result2) -> result1 + result2);
        LOG.error(errorMessage);
        return buildResponseEntity(errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException ex) {
        LOG.error(ex.getMessage());
        return buildResponseEntity(ex.getMessage());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> handleException(InvalidFormatException ex) {
        LOG.error(ex.getOriginalMessage());
        return buildResponseEntity(ex.getOriginalMessage());
    }

    @ExceptionHandler(ConditionsForActionNotMetException.class)
    public ResponseEntity<String> handleException(ConditionsForActionNotMetException ex) {
        LOG.error(ex.getMessage());
        return buildResponseEntity(ex.getMessage());
    }

    private ResponseEntity<String> buildResponseEntity(String errorMessage) {
        return new ResponseEntity<>(
                errorMessage,
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleException(EntityNotFoundException ex) {
        LOG.error(ex.getMessage());
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleException(DataIntegrityViolationException ex) {
        LOG.error(ex.getMostSpecificCause().getMessage());
        return new ResponseEntity<>(
                ex.getMostSpecificCause().getMessage(),
                HttpStatus.NOT_FOUND);
    }
}
