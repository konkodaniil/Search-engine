package searchengine.dto.search;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class SearchResponse {

    private boolean result;
    private int count;
    private List<SearchData> data = new ArrayList<>();
    private String error;
}
