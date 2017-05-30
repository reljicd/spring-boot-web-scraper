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

        Collection<Tag> tags = new HashSet<>();
        List<Tag> tagsList = new ArrayList<Tag>();
        for (Link linkWithSameUrl : linksWithSameUrl) {
            tags.addAll(linkWithSameUrl.getTags());
            tagsList.addAll(linkWithSameUrl.getTags());
        }
        // Remove tags already present
//        tags.removeAll(link.getTags());

        Map<Tag, Integer> tagsOccurences = new LinkedHashMap<Tag, Integer>();
        for (Tag s : tagsList) {
            if (tagsOccurences.get(s) != null) {
                tagsOccurences.put(s, tagsOccurences.get(s) + 1);
            } else {
                tagsOccurences.put(s, 1);
            }
        }

        Map<Tag, Integer> sortedTagsOccurences = sortByValue(tagsOccurences);

//        for (Map.Entry<Tag, Integer> tagsEntries : sortedTagsOccurences.entrySet())
//            System.out.println(tagsEntries.getKey().getTag() + " : " + tagsEntries.getValue());

        return sortedTagsOccurences.keySet();
//        return tags;
    }

    /**
     * Get {@link Tag}s from provided's url web page analysis
     *
     * @param link
     * @return
     */
    @Override
    public Collection<Tag> getTagsFromWebPageAnalysis(Link link) {

        Map<String, Integer> wordCountMap;
        Set<String> stopWords = new HashSet<String>(Arrays.asList("a", "about", "above", "after", "again", "against",
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

        String text = "";
        try {
            Document doc = Jsoup.connect(link.getUrl()).get();
            text = doc.body().text();
        } catch (IOException e) {
        }

        wordCountMap = wordCount(text.split(" "));

        Map<String, Integer> sortedWordCountMap = sortByValue(wordCountMap);
//        System.out.println("Sorted map: " + wordCountMap);

        // Build tags collection
        Collection<Tag> tagsCollection = new ArrayList<>();
        for (Map.Entry<String, Integer> sortedWordCountMapEntry : sortedWordCountMap.entrySet()) {
            // if word is stop word continue
            if (stopWords.contains(sortedWordCountMapEntry.getKey())) continue;

            Tag tag = new Tag();
            tag.setTag(sortedWordCountMapEntry.getKey());

            // If there is tag with that content already, use it
            Tag tagAlreadyExist = tagRepository.findByTag(sortedWordCountMapEntry.getKey());
            if (tagAlreadyExist != null) tag = tagAlreadyExist;

            // Ignore short words, and words that don't repeat more than 2 times
            if (sortedWordCountMapEntry.getKey().length() > 3 && sortedWordCountMapEntry.getValue() > 2) {
                tagsCollection.add(tag);
//                System.out.println(sortedWordCountMapEntry.getKey() + " : " + sortedWordCountMapEntry.getValue());
            }
            // Should put no more that 10 tags
            if (tagsCollection.size() == 10) break;
        }
        // remove tags already on this link
        tagsCollection.removeAll(link.getTags());
        return tagsCollection;
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
        String[] tagsString = string.split(" ");
        Collection<Tag> tags = new HashSet<Tag>();
        for (String tagString : tagsString) {
            Tag tag = new Tag();
            tag.setTag(tagString);
            // If there is tag with that content already in data store, use it
            Tag tagAlreadyExist = tagRepository.findByTag(tagString);
            if (tagAlreadyExist != null) tag = tagAlreadyExist;
            // Add tags to set
            tags.add(tag);
        }
        return tags;
    }

    /**
     * Helper method for returning Map of word counts from provided String array
     *
     * @param strings
     * @return
     */
    private Map<String, Integer> wordCount(String[] strings) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String s : strings) {

            if (!map.containsKey(s)) {  // first time we've seen this string
                map.put(s, 1);
            } else {
                int count = map.get(s);
                map.put(s, count + 1);
            }
        }
        return map;
    }

    /**
     * Helper method for sorting Map by Value
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return sorted map
     */
    private <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
