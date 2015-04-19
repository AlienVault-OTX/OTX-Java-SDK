package com.alienvault.otx.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.net.URI;
import java.util.List;

/**
 * A Page of results
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Page {
    private Integer count;
    private URI nextURI;
    private URI previousURI;
    private List<Pulse> results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public URI getNextURI() {
        return nextURI;
    }

    public void setNextURI(URI nextURI) {
        this.nextURI = nextURI;
    }

    public URI getPreviousURI() {
        return previousURI;
    }

    public void setPreviousURI(URI previousURI) {
        this.previousURI = previousURI;
    }

    public List<Pulse> getResults() {
        return results;
    }

    public void setResults(List<Pulse> results) {
        this.results = results;
    }
}
