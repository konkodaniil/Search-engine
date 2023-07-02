package searchengine.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import searchengine.dto.statistics.StatisticsIndex;
import searchengine.model.Lemma;
import searchengine.model.DBPage;
import searchengine.model.DBSite;
import searchengine.morphology.Morphology;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.utils.CleanHtmlCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class IndexSearch implements IndexParser {
    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final Morphology morphology;
    private List<StatisticsIndex> statisticsIndexList;

    @Override
    public List<StatisticsIndex> getIndexList() {
        return statisticsIndexList;
    }

    @Override
    public void run(DBSite dbSite) {
        Iterable<DBPage> pageList = pageRepository.findBySiteId(dbSite);
        List<Lemma> lemmaList = lemmaRepository.findBySitePageId(dbSite);
        statisticsIndexList = new ArrayList<>();

        for (DBPage page : pageList) {
            if (page.getCode() < 400) {
                long pageId = page.getId();
                String content = page.getContent();
                String title = CleanHtmlCode.clear(content, "title");
                String body = CleanHtmlCode.clear(content, "body");
                HashMap<String, Integer> titleList = morphology.getLemmaList(title);
                HashMap<String, Integer> bodyList = morphology.getLemmaList(body);

                for (Lemma lemma : lemmaList) {
                    Long lemmaId = lemma.getId();
                    String theExactLemma = lemma.getLemma();
                    if (titleList.containsKey(theExactLemma) || bodyList.containsKey(theExactLemma)) {
                        float wholeRank = 0.0F;
                        if (titleList.get(theExactLemma) != null) {
                            Float titleRank = Float.valueOf(titleList.get(theExactLemma));
                            wholeRank += titleRank;
                        }
                        if (bodyList.get(theExactLemma) != null) {
                            float bodyRank = (float) (bodyList.get(theExactLemma) * 0.8);
                            wholeRank += bodyRank;
                        }
                        statisticsIndexList.add(new StatisticsIndex(pageId, lemmaId, wholeRank));
                    } else {
                        log.debug("Lemma not found");
                    }
                }
            } else {
                log.debug("Bad status code - " + page.getCode());
            }
        }
    }
}

