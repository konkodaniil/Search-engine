package searchengine.dto.statistics;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Data
@Value
@RequiredArgsConstructor
public class StatisticsData {
    private TotalStatistics total;
    private List<DetailedStatisticsItem> detailed;
}
