package agafonova.com.popularmovies.model;

import java.util.ArrayList;
import java.util.List;

public class DiscoverMovie {

    String page;
    String totalResults;
    String totalPages;
    List<Result> results = null;

    public DiscoverMovie() {

    }

    public DiscoverMovie(String iPage, String iTotalResults, String iTotalPages, ArrayList<Result> iResults) {
        this.page = iPage;
        this.totalResults = iTotalResults;
        this.totalPages = iTotalPages;
        this.results = iResults;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
