package com.reljicd.service;

import com.reljicd.model.Link;
import com.reljicd.model.Tag;

import java.util.Collection;
import java.util.Optional;

public interface TagService {

    Optional<Tag> findByTag(String tag);

    Collection<Tag> getTagsFromOtherUsersForLink(Link link);

    Collection<Tag> getTagsFromWebPageAnalysis(Link link);

    Collection<Tag> getTagsFromString(String string);
}
