package bg.sofia.uni.fmi.mjt.sentiment;

import java.util.Set;

public class Review {
    ReviewSentiment sentiment;
    Set<String> words;

    private Review(ReviewSentiment sentiment, Set<String> words) {
        this.sentiment = sentiment;
        this.words = words;
    }


    public static Review of(int sentiment, Set<String> words) {
        return new Review(ReviewSentiment.of(sentiment), words);
    }

    public ReviewSentiment sentiment() {
        return sentiment;
    }


    public boolean containsWord(Word w) {
        return words.contains(w.toString());
    }
}
