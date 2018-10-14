package com.antondevs.apps.githubbrowser.ui.search;

/**
 * Created by Anton.
 */
public class SearchModel {

    private SearchType searchType;
    private String searchCriteria;
    private int currentResultsCount;

    public SearchModel(SearchType type, String criteria) {
        searchType = type;
        searchCriteria = criteria;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public int getCurrentResultsCount() {
        return currentResultsCount;
    }

    public void incrementResultsCount(int resultsCount) {
        currentResultsCount += resultsCount;
    }
}
