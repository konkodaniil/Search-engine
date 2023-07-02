package searchengine.services;


public interface IndexingService {
    boolean urlIndexing(String url);
    boolean startIndexing();
    boolean stopIndexing();
}