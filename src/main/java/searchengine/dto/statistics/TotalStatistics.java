package searchengine.dto.statistics;

import lombok.Data;
import lombok.Value;

@Data
@Value
public class TotalStatistics {
    private long sites;
    private long pages;
    private long lemmas;
    private boolean indexing;
}
