package searchengine.dto.statistics;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.repository.SiteRepository;
import searchengine.services.IndexingService;
import searchengine.services.StatisticsService;

import java.util.List;

@Setter
@Getter
public class SearchResults {
    private boolean result;
    private int count;
    private List<StatisticsSearch> data;


    public SearchResults(boolean result, int count, List<StatisticsSearch> data) {
        this.result = result;
        this.count = count;
        this.data = data;
    }
}


