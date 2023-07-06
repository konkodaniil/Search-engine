package searchengine.services;

public interface IndexingService {

    boolean urlIndexing(String url);

    void startIndexing();

    boolean stopIndexing();
}