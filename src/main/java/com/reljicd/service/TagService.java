package com.reljicd.service;

import com.reljicd.model.Link;
import com.reljicd.model.Tag;

import java.util.Collection;

/**
 * Service class for {@link Post} domain objects
 * Delegates calls to {@link com.reljicd.repository.PostRepository}
 *
 * @author Dusan
 */
public interface TagService {

    /**
     * Find {@link Tag} by tag string
     *
     * @param tag
     * @return
     */
    Tag findByTag(String tag);

    /**
     * get {@link Tag}'s for provided link from other users links with same url
     *
     * @param link
     * @return
     */
    Collection<Tag> getTagsFromOtherUsersForLink(Link link);

    /**
     * Get {@link Tag}s from provided's url web page analysis
     *
     * @param link
     * @return
     */
    Collection<Tag> getTagsFromWebPageAnalysis(Link link);

    /**
     * Return collection of {@link Tag}s from provided String
     *
     * @param string
     * @return
     */
    Collection<Tag> getTagsFromString(String string);
}
