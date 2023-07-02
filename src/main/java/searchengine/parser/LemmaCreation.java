package searchengine.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import searchengine.dto.statistics.StatisticsLemma;
import searchengine.model.DBPage;
import searchengine.model.DBSite;
import searchengine.morphology.Morphology;
import searchengine.parser.LemmaParser;
import searchengine.repository.PageRepository;
import searchengine.utils.CleanHtmlCode;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class LemmaCreation implements LemmaParser {
    private final PageRepository pageRepository;
    private final Morphology morphology;
    private List<StatisticsLemma> statisticsLemmaList;

    public List<StatisticsLemma> getLemmaDtoList() {
        return statisticsLemmaList;
    }

    @Override
    public void run(DBSite site) {
        statisticsLemmaList = new CopyOnWriteArrayList<>();
        Iterable<DBPage> pageList = pageRepository.findAll();
        TreeMap<String, Integer> lemmaList = new TreeMap<>();
        for (DBPage page : pageList) {
            String content = page.getContent();
            String title = CleanHtmlCode.clear(content, "title");
            String body = CleanHtmlCode.clear(content, "body");
            HashMap<String, Integer> titleList = morphology.getLemmaList(title);
            HashMap<String, Integer> bodyList = morphology.getLemmaList(body);
            Set<String> allTheWords = new HashSet<>();
            allTheWords.addAll(titleList.keySet());
            allTheWords.addAll(bodyList.keySet());
            for (String word : allTheWords) {
                int frequency = lemmaList.getOrDefault(word, 0) + 1;
                lemmaList.put(word, frequency);
            }
        }
        for (String lemma : lemmaList.keySet()) {
            Integer frequency = lemmaList.get(lemma);
            statisticsLemmaList.add(new StatisticsLemma(lemma, frequency));
        }
    }


}
