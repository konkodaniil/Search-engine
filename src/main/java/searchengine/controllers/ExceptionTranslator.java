package searchengine.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import searchengine.exception.EmptyRequestException;
import searchengine.exception.ErrMessage;
import searchengine.exception.IndexingAlreadyStartedException;

@RestControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrMessage> nullPointerException(NullPointerException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrMessage("Search info is not in database"
                        + exception.getMessage()));
    }

    @ExceptionHandler({IndexingAlreadyStartedException.class, EmptyRequestException.class})
    public ResponseEntity<ErrMessage> indexingAlreadyStartedException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrMessage(exception.getMessage()));
    }
}
