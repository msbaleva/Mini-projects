package bg.sofia.uni.fmi.mjt.sentiment;

public class Word {
    private String content;
    private int frequency;
    private double sentiment;


    private Word(String content) {
        this.content = content;
    }

    private Word(String content, int frequency, double sentiment) {
        this.content = content;
        this.frequency = frequency;
        this.sentiment = sentiment;
    }

    public static Word of(String content) {
        return new Word(content);
    }

    public static Word of(String content, int frequency, double sentiment) {
        return new Word(content, frequency, sentiment);
    }

    public double sentiment() {
        return sentiment;
    }

    public int frequency() {
        return frequency;
    }

    public void updateFrequency() {
        frequency++;
    }

    @Override
    public String toString() {
        return content;
    }

    public void updateSentiment(double newSentiment) {
        this.sentiment = newSentiment;
    }
}
