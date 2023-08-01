package org.api.payload.response.pageResponse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonSerialize
public class PageResponse<R> implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Integer totalPage;
    protected Integer currentPage;
    protected Integer noRecordInPage;
    protected long totalRecords;
    protected List<R> results;

    public PageResponse() {
    }

    public PageResponse<R> pageInfo(Integer currentPage, Integer itemsPerPage, long totalRecords) {
        if (currentPage == null || currentPage < 1) {
            this.currentPage = 1;
        } else {
            this.currentPage = currentPage;
        }
        this.totalRecords = totalRecords;
        if (Objects.nonNull(itemsPerPage)) {
            this.totalPage = (int) Math.ceil((float) totalRecords / itemsPerPage);
        } else {
            this.totalPage = 1;
        }
        return this;
    }

    public PageResponse<R> pageInfo(int totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public PageResponse<R> rawResults(List rawResults) {
        this.results = parseResult(rawResults);
        this.noRecordInPage = results.size();
        return this;
    }

    protected List<R> parseResult(List<R> rawResults) {
        return rawResults;
    }

    public boolean hasOneRecord() {
        return CollectionUtils.isNotEmpty(this.results) && this.results.size() == 1;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.results);
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public Integer getNoRecordInPage() {
        return noRecordInPage;
    }

    public List<R> getResults() {
        return results;
    }

    public void setNoRecordInPage(Integer noRecordInPage) {
        this.noRecordInPage = noRecordInPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public void setResults(List<R> results) {
        this.results = results;
    }
}
