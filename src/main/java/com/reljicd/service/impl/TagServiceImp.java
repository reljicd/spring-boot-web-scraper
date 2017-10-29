package com.reljicd.service.impl;

import com.reljicd.model.Link;
import com.reljicd.model.Tag;
import com.reljicd.repository.LinkRepository;
import com.reljicd.repository.TagRepository;
import com.reljicd.service.TagService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

/**
 * Implementation of {@link TagService}
 *
 * @author Dusan
 */
@Service
public class TagServiceImp implements TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private LinkRepository linkRepository;

    @Override
    public Tag findByTag(String tag) {
        return tagRepository.findByTag(tag);
    }

    /**
     * Returns collection of {@link Tag}'s for provided link from other user's links with same url
     * Returns them in sorted order according to the number of occurrences
     *
     * @param link object
     * @return
     */
    @Override
    public Collection<Tag> getTagsFromOtherUsersForLink(Link link) {
        // Find all Links in data store with same URL
        Collection<Link> linksWithSameUrl = linkRepository.findAllByUrl(link.getUrl());

        return linksWithSameUrl.stream()
                .map(Link::getTags)
                .flatMap(Collection::stream)
                .collect(groupingBy(identity(), counting()))
                .entrySet().stream()
                .sorted(comparingByValue(reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    /**
     * Get {@link Tag}s from provided's url web page analysis
     *
     * @param link
     * @return
     */
    @Override
    public Collection<Tag> getTagsFromWebPageAnalysis(Link link) {

        // Stop words
        Set<String> stopWords = new HashSet<>(Arrays.asList("a", "about", "above", "after", "again", "against",
                "all", "am", "an", "and", "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below",
                "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does",
                "doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further",
                "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's",
                "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is",
                "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself", "no", "nor",
                "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our", "ours\tourselves", "out", "over", "own",
                "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them",
                "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're",
                "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we",
                "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where",
                "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would",
                "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves"));

        String[] scrapedWordsArray = {};
        try {
            Document doc = Jsoup.connect(link.getUrl()).get();
            scrapedWordsArray = doc.body().text().split(" ");
        } catch (IOException e) {
        }

        return Arrays.stream(scrapedWordsArray)
                // Ignore words in stop words and short words
                .filter(((Predicate<String>) stopWords::contains).negate().and(s -> s.length() > 3))
                // Count words by number of occurrences
                .collect(groupingBy(identity(), counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 2)
                .sorted(comparingByValue(reverseOrder()))
                .map(e -> {
                    Tag tag = new Tag();
                    tag.setTag(e.getKey());
                    // If there is tag with that content already, use it
                    Tag tagAlreadyExist = tagRepository.findByTag(e.getKey());
                    if (tagAlreadyExist != null) tag = tagAlreadyExist;
                    return tag;
                })
                // Should put no more that 10 tags
                .limit(10)
                // remove tags already on this link
                .filter(((Predicate<Tag>) link.getTags()::contains).negate())
                .collect(toList());
    }

    /**
     * Return collection of {@link Tag}s from provided String
     * If for some word there already exists tag in data store with same string representation, use it
     *
     * @param string
     * @return
     */
    @Override
    public Collection<Tag> getTagsFromString(String string) {
        // Parse tags string
        return Arrays.stream(string.split(" "))
                .map(s -> {
                    Tag tag = new Tag();
                    tag.setTag(s);
                    // If there is tag with that content already in data store, use it
                    Tag tagAlreadyExist = tagRepository.findByTag(s);
                    if (tagAlreadyExist != null) tag = tagAlreadyExist;
                    return tag;
                })
                .collect(toList());
    }
}
