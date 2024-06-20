package bg.sofia.uni.fmi.mjt.netflix;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class NetflixRecommenderTest {

    public static final int DATASET_SIZE = 5_440;
    public static final int N = 10;
    public static final Path DATASET_PATH = Path.of("D:\\JavaProjects2023\\dataset.csv");
    public static NetflixRecommender recommender;
    public static NetflixRecommender emptyRecommender;
    @BeforeAll
    static void setup() {
        emptyRecommender = new NetflixRecommender(new StringReader(""));
        try {
            recommender = new NetflixRecommender(new FileReader(DATASET_PATH.toFile()));
        } catch (FileNotFoundException ioExc) {
            throw new RuntimeException("File not found.", ioExc);
        }

    }

    @Test
    void testGetAllContent() {
        List<Content> contents = recommender.getAllContent();
        long actual = contents.size();
        assertEquals(DATASET_SIZE, actual, "Dataset not loaded correctly.");
    }

    @Test
    void testGetAllGenres() {
        Set<String> expected = Set.of("action", "animation", "comedy", "crime", "documentation", "drama",  "european", "fantasy", "family", "history", "horror", "music", "reality", "romance", "war", "scifi","sport", "thriller", "western");
        Set<String> actual = new HashSet<>(recommender.getAllGenres());
        assertTrue(actual.containsAll(expected), "Not all genres are present.");
    }

    @Test
    void testGetLongestMovie() {
        String expected = "tm469911";
        String actual = recommender.getTheLongestMovie().id();
        assertEquals(expected, actual, "Get longest movie not working properly.");
    }

    @Test
    void testGroupContentByShow() {
        Set<Content> actualShowsById = recommender.groupContentByType().get(ContentType.SHOW);
        long expected = 1_963;
        long actual = actualShowsById.size();
        assertEquals(expected, actual, "Group shows by show type not working properly.");

    }

    @Test
    void testGroupContentByMovie() {
        Set<Content> actualMoviesById = recommender.groupContentByType().get(ContentType.MOVIE);
        long expected = 3_477;
        long actual = actualMoviesById.size();
        assertEquals(expected, actual, "Group shows by movie type not working properly.");
    }

    @Test
    void testGetLongestMovieThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> emptyRecommender.getTheLongestMovie(), "Doesn't throw exception when dataset is empty.");
    }

    @Test
    void testGetTopNRatedContentWhenNegative() {
        assertThrows(IllegalArgumentException.class, () -> recommender.getTopNRatedContent(-N), "Doesn't throw exception when n is negative.");
    }

    @Test
    void testGetTopNRatedContent() {
        List<String> expected = List.of(
                "ts4",
                "ts3371",
                "ts20682",
                "ts11313",
                "ts81120",
                "ts222333",
                "ts20681",
                "tm92641",
                "tm122434",
                "ts90621"
        );
        List<String> actual = recommender.getTopNRatedContent(N).stream().map(Content::id).toList();
        Iterator<String> iterActual = actual.listIterator();
        for (String s : expected) {
            if (iterActual.hasNext()) {
                assertEquals(iterActual.next(), s, s + " not contained.");
            }

        }
    }

    @Test
    void testGetSimilarContent() {
        Content content = Content.of("ts98340", "Metallica: Some Kind of Monster", "SHOW",
                "This collection includes the acclaimed rock documentary about Metallica; " +
                        "plus a film checking in with the still-thriving group 10 years later.", "2004", "84",
                "['western']",	"1",	"tt6954486",	"-1",	"0");

        List<String> expected = List.of("ts38710", "ts57439");
        List<Content> actual = recommender.getSimilarContent(content);
        Iterator<Content> iterActual = actual.listIterator();
        for (String c : expected) {
            if (iterActual.hasNext()) {
                assertEquals(c, iterActual.next().id(), "Get similiar content not working properly");
            }
        }
    }

    @Test
    void testGetContentByKeywords() {
        Set<String> expected = Set.of("tm230524", "tm367264");
        Set<String> actual = recommender.getContentByKeywords("1930s", "story").stream().map(Content::id).collect(Collectors.toSet());
        assertTrue(actual.containsAll(expected), "Get content by keywords not working properly.");
    }

}
