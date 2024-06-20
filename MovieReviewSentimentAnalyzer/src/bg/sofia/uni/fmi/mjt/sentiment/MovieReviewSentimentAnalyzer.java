package bg.sofia.uni.fmi.mjt.sentiment;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

public class MovieReviewSentimentAnalyzer implements SentimentAnalyzer {

    private static final String N_NEGATIVE_MESSAGE = "N must be a positive integer.";
    private static final String INVALID_REVIEW_MESSAGE = "Review cannot be null, empty or blank.";
    private static final String INVALID_SENTIMENT_MESSAGE = "Sentiment is out of bounds [0.0, 4.0].";
    private static final String NON_WORD_REGEX = "[^a-zA-Z0-9']";
    private static final double MIN_SENTIMENT = 0;
    private static final double MAX_SENTIMENT = 4;
    private static final String SEPARATOR = " ";
    Set<String> stopwords;
    List<Review> reviews;
    Map<String, Word> words = new HashMap<>();
    private final Writer writer;

    public MovieReviewSentimentAnalyzer(Reader stopwordsIn, Reader reviewsIn, Writer reviewsOut) {
        BufferedReader stopwordsReader = new BufferedReader(stopwordsIn);
        this.stopwords = stopwordsReader.lines().collect(Collectors.toSet());
        BufferedReader reviewsReader = new BufferedReader(reviewsIn);
        this.reviews = reviewsReader.lines()
                .map(this::generateReview)
                .collect(Collectors.toList());
        learnFromData();
        this.writer = reviewsOut;
    }

    private String addWordToDictionary(String wordContent) {
        Word word = words.get(wordContent);
        if (word == null) {
            word = Word.of(wordContent);
        } else {
            word.updateFrequency();
        }

        words.put(wordContent, word);
        return wordContent;
    }

    private Review generateReview(String line) {
        String[] review = line.split(SEPARATOR, 1);
        return generateReview(Integer.parseInt(review[0]), review[1]);
    }

    private Review generateReview(int sentiment, String review) {
        Set<String> wordsInReview = Arrays.stream(review.toLowerCase().split(NON_WORD_REGEX))
                .filter(w -> !isStopWord(w))
                .map(this::addWordToDictionary)
                .collect(Collectors.toSet());
        return Review.of(sentiment, wordsInReview);
    }

    private void learnFromData() {
        words.values().forEach(w -> {
                    OptionalDouble optional = reviews.stream()
                            .filter(r -> r.containsWord(w))
                            .map(Review::sentiment)
                            .mapToDouble(ReviewSentiment::rating)
                            .average();
                    if (optional.isPresent()) {
                        w.updateSentiment(optional.getAsDouble());
                    }
                });
    }

    @Override
    public double getReviewSentiment(String review) {
        OptionalDouble sentiment = Arrays.stream(review.toLowerCase().split("[^a-zA-Z0-9']"))
                .filter(w -> !isStopWord(w) && isKnownWord(w))
                .mapToDouble(this::getWordSentiment)
                .average();

        return sentiment.isPresent() ? sentiment.getAsDouble() : ReviewSentiment.UNKNOWN.rating();
    }

    @Override
    public String getReviewSentimentAsName(String review) {
        return ReviewSentiment.of((int) Math.round(getReviewSentiment(review))).semantic();
    }

    @Override
    public double getWordSentiment(String word) {
        return words.get(word).sentiment();
    }

    @Override
    public int getWordFrequency(String word) {
        return words.get(word).frequency();
    }

    private boolean isKnownWord(String word) {
        return words.containsKey(word);
    }

    private List<String> getWordsByComparator(Comparator<Word> comparator, int n) {
        if (n < 0) {
            throw new IllegalArgumentException(N_NEGATIVE_MESSAGE);
        }

        return words.values().stream()
                .sorted(comparator)
                .limit(n)
                .map(Word::toString)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getMostFrequentWords(int n) {
        return getWordsByComparator((w1, w2) -> Integer.compare(w2.frequency(), w1.frequency()), n);
    }

    @Override
    public List<String> getMostPositiveWords(int n) {
        return getWordsByComparator((w1, w2) -> Double.compare(w2.sentiment(), w1.sentiment()), n);
    }

    @Override
    public List<String> getMostNegativeWords(int n) {
        return getWordsByComparator(Comparator.comparingDouble(Word::sentiment), n);
    }

    @Override
    public boolean appendReview(String review, int sentiment) {
        if (review == null || review.isEmpty() || review.isBlank()) {
            throw new IllegalArgumentException(INVALID_REVIEW_MESSAGE);
        }

        if (sentiment < MIN_SENTIMENT || sentiment > MAX_SENTIMENT) {
            throw new IllegalArgumentException(INVALID_SENTIMENT_MESSAGE);
        }

        PrintWriter out = new PrintWriter(writer);
        out.println(String.format("%d %s", sentiment, review));
        reviews.add(generateReview(sentiment, review));
        return true;
    }

    @Override
    public int getSentimentDictionarySize() {
        return words.size();
    }

    @Override
    public boolean isStopWord(String word) {
        return stopwords.contains(word.toLowerCase());
    }
}
