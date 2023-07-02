package searchengine.dto.statistics;

import lombok.Value;

@Value
public class PageStatistics {
    String url;
    String content;
    int code;
}
