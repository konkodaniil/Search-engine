package searchengine.services.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.model.DBSite;
import searchengine.model.Status;
import searchengine.parser.IndexParser;
import searchengine.parser.LemmaParser;
import searchengine.parser.SiteIndexing;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.IndexingService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class IndexingServiceImpl implements IndexingService {

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;
    private final SitesList sitesList;
    private final IndexParser indexParser;
    private final LemmaParser lemmaParser;
    private ExecutorService executorService;

    public IndexingServiceImpl(SiteRepository siteRepository, PageRepository pageRepository, LemmaRepository lemmaRepository, IndexRepository indexRepository, SitesList sitesList, IndexParser indexParser, LemmaParser lemmaParser) {
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;
        this.lemmaRepository = lemmaRepository;
        this.sitesList = sitesList;
        this.indexRepository = indexRepository;
        this.indexParser = indexParser;
        this.lemmaParser = lemmaParser;
    }
    @Override
    public boolean urlIndexing(String url) {
        if (urlCheck(url)) {
            log.info("Start reindexing site - " + url);
            executorService = Executors.newFixedThreadPool(10);
            executorService.submit(new SiteIndexing(pageRepository, siteRepository, lemmaRepository, indexRepository, lemmaParser, indexParser, url, sitesList));
            executorService.shutdown();

            return true;
        } else {
            return false;
        }
    }
    @Override
    @Autowired
    public boolean startIndexing() {
        if (isIndexingActive()) {
            log.debug("Indexing already started");
            return false;
        } else {
            List<Site> siteList = sitesList.getSites();
            executorService = Executors.newFixedThreadPool(10);
            for (Site site : siteList) {
                String url = site.getUrl();
                DBSite dbSite = new DBSite();
                dbSite.setName(site.getName());
                log.info("Parsing site: " + site.getName());
                executorService.submit(new SiteIndexing(pageRepository, siteRepository, lemmaRepository, indexRepository, lemmaParser, indexParser, url, sitesList));
            }
            executorService.shutdown();
        }
        return true;
    }

    @Override
    public boolean stopIndexing() {
        if (isIndexingActive()) {
            log.info("Indexing was stopped");
            executorService.shutdownNow();
            return true;
        } else {
            log.info("Indexing was not stopped because it was not started");
            return false;
        }
    }

    private boolean isIndexingActive() {
        siteRepository.flush();
        Iterable<DBSite> siteList = siteRepository.findAll();
        for (DBSite site : siteList) {
            if (site.getStatus() == Status.INDEXING) {
                return true;
            }
        }
        return false;
    }

    private boolean urlCheck(String url) {
        List<Site> urlList = sitesList.getSites();
        for (Site site : urlList) {
            if (site.getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }
}