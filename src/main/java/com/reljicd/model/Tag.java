package com.reljicd.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * @author Dusan
 */
@Entity
@Table(name = "tag")
public class Tag {

    private Long id;
    private String tag;
    private Collection<Link> links;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tag_id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "tag", unique = true)
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "tags")
    public Collection<Link> getLinks() {
        return links;
    }

    public void setLinks(Collection<Link> links) {
        this.links = links;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag1 = (Tag) o;

        return tag.equals(tag1.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }
}
