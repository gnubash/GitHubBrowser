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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SearchModel)) return false;

        SearchModel modelToCompareTo = (SearchModel) obj;
        return  (searchType == modelToCompareTo.getSearchType() &&
                searchCriteria.equals(modelToCompareTo.getSearchCriteria()) &&
                currentResultsCount == modelToCompareTo.getCurrentResultsCount());

    }
}
