package com.reljicd.service;

import com.reljicd.model.Link;
import com.reljicd.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LinkService {

    Optional<Link> findLinkForId(Long id);

    Link saveLink(Link link);

    /**
     * Finds a {@link Page) of {@link Link } of provided user ordered by date
     */
    Page<Link> findByUserOrderedByDatePageable(User user, Pageable pageable);

    /**
     * Finds a {@link Page) of all {@link Link } ordered by date
     */
    Page<Link> findAllOrderedByDatePageable(Pageable pageable);

    void delete(Link link);
}
