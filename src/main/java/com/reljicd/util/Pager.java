package com.reljicd.util;

import com.reljicd.model.Link;
import org.springframework.data.domain.Page;

/**
 * @author Dusan Raljic
 */
public class Pager {

    private final Page<Link> links;

    public Pager(Page<Link> links) {
        this.links = links;
    }

    public int getPageIndex() {
        return links.getNumber() + 1;
    }

    public int getPageSize() {
        return links.getSize();
    }

    public boolean hasNext() {
        return links.hasNext();
    }

    public boolean hasPrevious() {
        return links.hasPrevious();
    }

    public int getTotalPages() {
        return links.getTotalPages();
    }

    public long getTotalElements() {
        return links.getTotalElements();
    }

    public boolean indexOutOfBounds() {
        return this.getPageIndex() < 0 || this.getPageIndex() > this.getTotalElements();
    }

}
