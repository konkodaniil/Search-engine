package searchengine.exception;

public class EmptyRequestException extends RuntimeException {

    public EmptyRequestException() {
        super("Empty request");
    }
}