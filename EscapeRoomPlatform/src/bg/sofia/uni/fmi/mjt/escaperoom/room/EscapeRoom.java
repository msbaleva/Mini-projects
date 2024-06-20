package bg.sofia.uni.fmi.mjt.escaperoom.room;

import bg.sofia.uni.fmi.mjt.escaperoom.rating.Ratable;

import java.util.Objects;

public class EscapeRoom implements Ratable {

    private final String name;
    private final Theme theme;
    private final Difficulty difficulty;
    private final int maxTimeToEscape;
    private final double priceToPlay;
    private Review[] reviews;
    private final int maxReviewsCount;
    private int currentReviewCount;
    private int sumRatings;

    public EscapeRoom(String name, Theme theme, Difficulty difficulty, int maxTimeToEscape, double priceToPlay,
                      int maxReviewsCount) {
        this.name = name;
        this.theme = theme;
        this.difficulty = difficulty;
        this.maxTimeToEscape = maxTimeToEscape;
        this.priceToPlay = priceToPlay;
        this.maxReviewsCount = maxReviewsCount;
        this.reviews = new Review[maxReviewsCount];
    }

    public EscapeRoom(EscapeRoom other) {
        this.name = other.name;
        this.theme = other.theme;
        this.difficulty = other.difficulty;
        this.maxTimeToEscape = other.maxTimeToEscape;
        this.priceToPlay = other.priceToPlay;
        this.maxReviewsCount = other.maxReviewsCount;
        this.reviews = other.reviews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EscapeRoom that = (EscapeRoom) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Returns the name of the escape room.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the difficulty of the escape room.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the maximum time to escape the room.
     */
    public int getMaxTimeToEscape() {
        return maxTimeToEscape;
    }

    /**
     * Returns all user reviews stored for this escape room, in the order they have been added.
     */
    public Review[] getReviews() {
        return reviews;
    }

    /**
     * Adds a user review for this escape room.
     * The platform keeps just the latest up to {@code maxReviewsCount} reviews and in case the capacity is full,
     * a newly added review would overwrite the oldest added one, so the platform contains
     * {@code maxReviewsCount} at maximum, at any given time. Note that, despite older reviews may have been
     * overwritten, the rating of the room averages all submitted review ratings, regardless of whether all reviews
     * themselves are still stored in the platform.
     *
     * @param review the user review to add.
     */
    public void addReview(Review review) {
        reviews[currentReviewCount % maxReviewsCount] = review;
        sumRatings += review.getRating();
    }

    /**
     * Returns the rating of a ratable object.
     *
     * @return the rating
     */
    @Override
    public double getRating() {
        return (double) sumRatings / (currentReviewCount + 1);
    }
}
