package com.reljicd.service;

import com.reljicd.model.Link;
import com.reljicd.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * Service class for {@link Link} domain objects
 * Delegates calls to {@link com.reljicd.repository.LinkRepository}
 *
 * @author Dusan
 */
public interface LinkService {

    /**
     * Finds n latest {@link Link}s
     *
     * @param n
     * @return a Collection of {@link Link}s
     */
    Collection<Link> findNLatestLinks(int n);

    Collection<Link> findNLatestLinksForUser(int n, User user);

    Link findLinkForId(Long id);

    Link saveLink(Link link);

    Page<Link> findAllPageable(Pageable pageable);

    /**
     * Finds a {@link Page) of {@link Link } of provided user ordered by date
     *
     * @param user
     * @param pageable
     * @return {@link Page} instance
     */
    Page<Link> findByUserOrderedByDatePageable(User user, Pageable pageable);

    /**
     * Finds a {@link Page) of all {@link Link } ordered by date
     *
     * @param pageable
     * @return {@link Page} instance
     */
    Page<Link> findAllOrderedByDatePageable(Pageable pageable);

    /**
     * Deletes {@link Link} from data store
     *
     * @param link the {@link Link} to delete
     */
    void delete(Link link);
}
