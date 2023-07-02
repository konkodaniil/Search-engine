package searchengine.parser;


import searchengine.dto.statistics.StatisticsIndex;
import searchengine.model.DBSite;

import java.util.List;

public interface IndexParser {
    void run(DBSite dbSite);
    List<StatisticsIndex> getIndexList();
}
