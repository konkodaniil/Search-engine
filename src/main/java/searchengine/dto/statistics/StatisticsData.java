package searchengine.dto.statistics;

import java.util.List;
import lombok.Data;

public record StatisticsData(
    TotalStatistics total,
    List<DetailedStatisticsItem> detailed
) {}
