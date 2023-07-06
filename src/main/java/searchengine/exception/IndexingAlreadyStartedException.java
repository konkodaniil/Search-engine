package searchengine.exception;

public class IndexingAlreadyStartedException extends RuntimeException {

    public IndexingAlreadyStartedException() {
        super("Indexing not started");
    }
}
