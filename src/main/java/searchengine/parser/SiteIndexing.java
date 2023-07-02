package searchengine.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.PageStatistics;
import searchengine.model.*;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.dto.statistics.StatisticsIndex;
import searchengine.dto.statistics.StatisticsLemma;
import searchengine.repository.SiteRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;


import static searchengine.model.Status.*;

@RequiredArgsConstructor
@Slf4j
public class SiteIndexing implements Runnable {

    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;
    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;
    private final LemmaParser lemmaParser;
    private final IndexParser indexParser;
    private final String url;
    private final SitesList sitesList;

    @Override
    public void run() {
        if (siteRepository.findByUrl(url) != null) {
            log.info("Start deleting data from the site - " + url);
            deleteDataFromSite();
        }
        log.info("Indexing - " + url + " " + getName());
        saveDateSite();
        try {
            List<PageStatistics> pageStatistics = getPageList();
            saveToBase(pageStatistics);
            getLemmasPage();
            indexingWords();
        } catch (InterruptedException e) {
            log.error("Indexing stopped - " + url);
            errorSite();
        }
    }

    private void deleteDataFromSite() {
        DBSite dbSite = siteRepository.findByUrl(url);
        dbSite.setStatus(INDEXING);
        dbSite.setName(getName());
        dbSite.setStatusTime(LocalDateTime.now());
        siteRepository.save(dbSite);
        siteRepository.flush();
        siteRepository.delete(dbSite);
    }

    private String getName() {
        List<Site> sitesList_2 = sitesList.getSites();
        for (Site sites : sitesList_2) {
            if (sites.getUrl().equals(url)) {
                return sites.getName();
            }
        }
        return "";
    }

//    @Transactional
    private void saveDateSite() {
        DBSite dbSite = new DBSite();
        dbSite.setUrl(url);
        dbSite.setName(getName());
        dbSite.setStatus(Status.INDEXING);
        dbSite.setStatusTime(LocalDateTime.now());
        siteRepository.flush();
        siteRepository.save(dbSite);
    }

    private List<PageStatistics> getPageList() throws InterruptedException {
        if (!Thread.interrupted()) {
            String urlFormat = url + "/";
            List<PageStatistics> statisticsPageVector = new Vector<>();
            List<String> urlList = new Vector<>();
            ForkJoinPool forkJoinPool = new ForkJoinPool(10);
            List<PageStatistics> pages = forkJoinPool.invoke(new HtmlParser(urlFormat, statisticsPageVector, urlList));
            return new CopyOnWriteArrayList<>(pages);
        } else throw new InterruptedException();
    }

//    @Transactional
    private void saveToBase(List<PageStatistics> pages) throws InterruptedException {
        if (!Thread.interrupted()) {
            List<DBPage> dbPages = new CopyOnWriteArrayList<>();
            DBSite site = siteRepository.findByUrl(url);

            for (PageStatistics page : pages) {
                int first = page.getUrl().indexOf(url) + url.length();
                String path = page.getUrl().substring(first);
                DBPage dbPage = new DBPage();
                dbPage.setId((long) (Math.random() * 1000000));
                dbPage.setSiteId(site);
                dbPage.setPath(path);
                dbPage.setCode(page.getCode());
                dbPage.setContent(page.getContent());
                dbPages.add(dbPage);
            }
            pageRepository.flush();
            pageRepository.saveAll(dbPages);
        } else {
            throw new InterruptedException();
        }
    }

    private void errorSite() {
        DBSite sitePage = new DBSite();
        sitePage.setLastError("Indexing stopped");
        sitePage.setStatus(Status.FAILED);
        sitePage.setStatusTime(LocalDateTime.now());
        siteRepository.save(sitePage);
    }

//    @Transactional
    private void getLemmasPage() {
        if (!Thread.interrupted()) {
            DBSite siteId = siteRepository.findByUrl(url);
            siteId.setStatusTime(LocalDateTime.now());
            lemmaParser.run(siteId);
            List<StatisticsLemma> statisticsLemmaList = lemmaParser.getLemmaDtoList();
            List<Lemma> lemmaList = new CopyOnWriteArrayList<>();
            for (StatisticsLemma statisticsLemma : statisticsLemmaList) {
//                lemmaList.add(new Lemma(siteId, statisticsLemma.getLemma(), statisticsLemma.getFrequency()));
                Lemma lemma = new Lemma();
                lemma.setId((long) (Math.random() * 10000000));
                lemma.setLemma(statisticsLemma.getLemma());
                lemma.setFrequency(statisticsLemma.getFrequency());
                lemma.setSitePageId(siteId);
                lemmaList.add(lemma);
            }
            lemmaRepository.flush();
            lemmaRepository.saveAll(lemmaList);
        } else {
            throw new RuntimeException();
        }
    }

//@Transactional
    private void indexingWords() throws InterruptedException {
        if (!Thread.interrupted()) {
            DBSite dbSite = siteRepository.findByUrl(url);
            indexParser.run(dbSite);
            List<StatisticsIndex> statisticsIndexList = new CopyOnWriteArrayList<>(indexParser.getIndexList());
            List<Index> indexList = new CopyOnWriteArrayList<>();
            dbSite.setStatusTime(LocalDateTime.now());
            for (StatisticsIndex statisticsIndex : statisticsIndexList) {
                Index index = new Index();
                DBPage page = pageRepository.getById(statisticsIndex.getPageID());
                Lemma lemma = lemmaRepository.getById(statisticsIndex.getLemmaID());
//                indexList.add(new Index(page, lemma, statisticsIndex.getRank()));
                index.setId((long) (Math.random()*10000000));
                index.setPageId(page);
                index.setLemma(lemma);
                index.setRank(statisticsIndex.getRank());
                indexList.add(index);
            }
            indexRepository.flush();
            indexRepository.saveAll(indexList);
            log.info("Done indexing - " + url);
            dbSite.setStatusTime(LocalDateTime.now());
            dbSite.setStatus(Status.INDEXED);
            siteRepository.save(dbSite);
        } else {
            throw new InterruptedException();
        }
    }

}

