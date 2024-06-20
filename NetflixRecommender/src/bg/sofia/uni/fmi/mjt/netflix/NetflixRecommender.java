package bg.sofia.uni.fmi.mjt.netflix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

public class NetflixRecommender {

    private List<Content> contents;
    public static final int THRESHOLD = 10_000;
    public static final String CONTENT_SEPARATOR = ",";
    public static final String EMPTY = " ";

    /**
     * Loads the dataset from the given {@code reader}.
     *
     * @param reader Reader from which the dataset can be read.
     */
    public NetflixRecommender(Reader reader) {
        try (var br = new BufferedReader(reader)) {
            this.contents = br.lines()
                    .skip(1)
                    .map(s -> s.split(CONTENT_SEPARATOR))
                    .map(Content::of)
                    .toList();
        } catch (IOException ioExc) {
            throw new RuntimeException("Could not load dataset", ioExc);
        }
    }

    /**
     * Returns all movies and shows from the dataset in undefined order as an unmodifiable List.
     * If the dataset is empty, returns an empty List.
     *
     * @return the list of all movies and shows.
     */
    public List<Content> getAllContent() {
        return List.copyOf(contents);
    }

    /**
     * Returns a list of all unique genres of movies and shows in the dataset in undefined order.
     * If the dataset is empty, returns an empty List.
     *
     * @return the list of all genres
     */
    public List<String> getAllGenres() {
        return contents.stream()
                .flatMap(c -> c.genres().stream())
                .distinct()
                .toList();
    }

    /**
     * Returns the movie with the longest duration / run time. If there are two or more movies
     * with equal maximum run time, returns any of them. Shows in the dataset are not considered by this method.
     *
     * @return the movie with the longest run time
     * @throws NoSuchElementException in case there are no movies in the dataset.
     */
    public Content getTheLongestMovie() {
        return contents.stream()
                .filter(c -> c.type() == ContentType.MOVIE)
                .max(Comparator.comparingInt(Content::runtime))
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Returns a breakdown of content by type (movie or show).
     *
     * @return a Map with key: a ContentType and value: the set of movies or shows on the dataset, in undefined order.
     */
    public Map<ContentType, Set<Content>> groupContentByType() {
        return contents.stream()
                .collect(Collectors.groupingBy(Content::type, Collectors.toSet()));
    }

    /**
     * Returns the top N movies and shows sorted by weighed IMDB rating in descending order.
     * If there are fewer movies and shows than {@code n} in the dataset, return all of them.
     * If {@code n} is zero, returns an empty list.
     *
     * The weighed rating is calculated by the following formula:
     * Weighted Rating (WR) = (v ÷ (v + m)) × R + (m ÷ (v + m)) × C
     * where
     * R is the content's own average rating across all votes. If it has no votes, its R is 0.
     * C is the average rating of content across the dataset
     * v is the number of votes for a content
     * m is a tunable parameter: sensitivity threshold. In our algorithm, it's a constant equal to 10_000.
     *
     * Check https://stackoverflow.com/questions/1411199/what-is-a-better-way-to-sort-by-a-5-star-rating for details.
     *
     * @param n the number of the top-rated movies and shows to return
     * @return the list of the top-rated movies and shows
     * @throws IllegalArgumentException if {@code n} is negative.
     */
    public List<Content> getTopNRatedContent(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("N cannot be negative.");
        }

        double avgRating =  contents.isEmpty() ? 0.0 : contents.stream()
                .map(Content::imdbScore)
                .reduce(Double::sum)
                .get() / contents.size();

        return contents.stream()
                .sorted((c1, c2) -> Double.compare(weightedRating(c2, avgRating), weightedRating(c1, avgRating)))
                .limit(n)
                .toList();
    }

    private double weightedRating(Content content, double avgRating) {
        double r = content.imdbScore();
        double v = content.imdbVotes();
        return (v / (v + THRESHOLD)) * r + (THRESHOLD / (v + THRESHOLD)) * avgRating;
    }

    /**
     * Returns a list of content similar to the specified one sorted by similarity is descending order.
     * Two contents are considered similar, only if they are of the same type (movie or show).
     * The used measure of similarity is the number of genres two contents share.
     * If two contents have equal number of common genres with the specified one, their mutual order
     * in the result is undefined.
     *
     * @param content the specified movie or show.
     * @return the sorted list of content similar to the specified one.
     */
    public List<Content> getSimilarContent(Content content) {
        return contents.stream()
                .filter(c -> content.type() == c.type())
                .sorted((c1, c2) ->{
                    Set<String> c1Genres = new HashSet<>(c1.genres());
                    Set<String> c2Genres = new HashSet<>(c2.genres());
                    c1Genres.retainAll(content.genres());
                    c2Genres.retainAll(content.genres());
                    return Integer.compare(c2Genres.size(), c1Genres.size());
                }).toList();
    }

    /**
     * Searches content by keywords in the description (case-insensitive).
     *
     * @param keywords the keywords to search for
     * @return an unmodifiable set of movies and shows whose description contains all specified keywords.
     */
    public Set<Content> getContentByKeywords(String... keywords) {
        Set<String> descKeywords = Arrays.stream(keywords)
                .map(String::toLowerCase)
                .map(String::strip)
                .collect(Collectors.toSet());
        return contents.stream()
                .filter(c -> {
                    Set<String> contentDesc = Arrays.stream(c.description().toLowerCase().split(EMPTY))
                            .map(String::strip)
                            .collect(Collectors.toSet());
                    return contentDesc.containsAll(descKeywords);
                }).collect(Collectors.toUnmodifiableSet());
    }

}

