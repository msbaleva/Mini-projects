package bg.sofia.uni.fmi.mjt.sentiment;

import org.junit.jupiter.api.BeforeAll;

import java.io.*;
import java.nio.file.Files;

public class MovieReviewSentimentAnalyzerTest {

    private static final String DATASET_PATH = "D:\\JavaProjects2023\\TextFiles\\moviewReviews.txt";
    private static final String STOPWORDS_DATASET_PATH = "D:\\JavaProjects2023\\TextFiles\\stopwords.txt";
    public static SentimentAnalyzer analyzer;

    @BeforeAll
    public static void setup() {
        try (Reader reviewsIn = new FileReader(DATASET_PATH);
             Reader stopwordsIn = new FileReader(DATASET_PATH);
             Writer reviewsOut = new FileWriter(DATASET_PATH)) {
            analyzer = new MovieReviewSentimentAnalyzer(reviewsIn, stopwordsIn, reviewsOut);
        } catch (IOException ioExc) {
            throw new RuntimeException("Problem occurred while opening reader.", ioExc);
        }
    }
}
