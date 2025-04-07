package app.web;

import app.exceptions.EmailAlreadyExistException;
import app.exceptions.InvalidInputException;
import app.exceptions.NotFoundException;
import app.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> emailAlreadyExist (EmailAlreadyExistException exception) {

        ErrorResponse errorResponse = new ErrorResponse(400, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> invalidInput (InvalidInputException exception) {

        ErrorResponse errorResponse = new ErrorResponse(403, exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound (NotFoundException exception) {

        ErrorResponse errorResponse = new ErrorResponse(404, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
