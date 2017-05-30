package com.reljicd.repository;

import com.reljicd.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Dusan
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByTag(String tag);
}
