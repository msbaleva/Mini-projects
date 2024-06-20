package bg.sofia.uni.fmi.mjt.escaperoom.room;

import bg.sofia.uni.fmi.mjt.escaperoom.rating.Ratable;

public class Review  {

    private int rating;
    private String reviewText;
    private static final int MIN_RATING = 0;
    private static final int MAX_RATING = 10;
    private static final int MAX_TEXT_LENGTH = 200;
    public Review(int rating, String reviewText) {
        if (rating < MIN_RATING || rating > MAX_RATING) {
            throw new IllegalArgumentException("Rating out of bounds [0,10].");
        }

        if (reviewText == null || reviewText.length() > MAX_TEXT_LENGTH) {
            throw new IllegalArgumentException("Invalid review.");
        }

        this.rating = rating;
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }


}

/*
  public record Review(int rating, String reviewText) {

      public Review {

          if (rating < 0 || rating > 10) {
              throw new IllegalArgumentException("Rating must be between 0 and 10");
          }

          if (reviewText == null) {
              throw new IllegalArgumentException("Review text cannot be null");
          }

          if (reviewText.length() > 200) {
              throw new IllegalArgumentException("Review text exceeds 200 characters");
          }

      }

  }
 */
