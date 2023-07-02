package searchengine.parser;

import org.springframework.stereotype.Component;
import searchengine.dto.statistics.StatisticsLemma;
import searchengine.model.DBSite;

import java.util.List;
@Component
public interface LemmaParser {
    void run(DBSite site);
    List<StatisticsLemma> getLemmaDtoList();
}
