package bg.sofia.uni.fmi.mjt.sentiment;

public enum ReviewSentiment {
    NEGATIVE(0, "negative"),
    SOMEWHAT_NEGATIVE(1, "somewhat_negative"),
    NEUTRAL(2, "neutral"),
    SOMEWHAT_POSITIVE(3, "somewhat_positive"),
    POSITIVE(4, "positive"),
    UNKNOWN(-1, "unknown");

    private final int rating;
    private final String semantic;

    ReviewSentiment(int rating, String semantic) {
        this.rating = rating;
        this.semantic = semantic;
    }

    public static ReviewSentiment of(int rating) {
        return rating == UNKNOWN.rating ? UNKNOWN : ReviewSentiment.values()[rating];
    }

    public int rating() {
        return rating;
    }

    public String semantic() {
        return semantic;
    }

}
