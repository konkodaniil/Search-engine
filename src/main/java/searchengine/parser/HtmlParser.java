package searchengine.parser;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.dto.statistics.PageStatistics;
import searchengine.model.DBPage;
import searchengine.model.DBSite;
import searchengine.repository.PageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class HtmlParser extends RecursiveTask<List<PageStatistics>> {
    private final String address;
    private final List<String> addressList;
    private final List<PageStatistics> statisticsPageList;

    public HtmlParser(String address, List<PageStatistics> statisticsPageList, List<String> addressList) {
        this.address = address;
        this.statisticsPageList = statisticsPageList;
        this.addressList = addressList;
    }

    public Document getConnect(String url) {
        Document doc = null;
        try {
            Thread.sleep(150);
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
        } catch (Exception e) {
            log.debug("Can't get connected to the site" + url);
        }
        return doc;
    }

    @Override
    protected List<PageStatistics> compute() {
        try {
            Thread.sleep(500);
            Document document = getConnect(address);
            String html = document.outerHtml();
            Connection.Response response = document.connection().response();
            int statusCode = response.statusCode();
            PageStatistics pageStatistics = new PageStatistics(address, html, statusCode);
            statisticsPageList.add(pageStatistics);
            Elements elements = document.getElementsByTag("a").select("[href^=http], [href^=/]");
            List<HtmlParser> taskList = new ArrayList<>();
            for (Element el : elements) {
                String link = el.attr("abs:href");
                if (link.startsWith(el.baseUri()) && !link.equals(el.baseUri()) && !link.contains("#") && !link.contains(".pdf") && !link.contains(".jpg") && !link.contains(".JPG") && !link.contains(".png") && !addressList.contains(link)) {
                    HtmlParser task = new HtmlParser(link, statisticsPageList, addressList);
                    task.fork();
                    taskList.add(task);
                }
            }
            taskList.forEach(ForkJoinTask::join);
        } catch (Exception e) {
            log.debug("Parsing error - " + address);
            PageStatistics pageStatistics = new PageStatistics(address, "", 500);
            statisticsPageList.add(pageStatistics);
        }
        return statisticsPageList;
    }
}
