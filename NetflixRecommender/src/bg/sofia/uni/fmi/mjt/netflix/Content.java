package bg.sofia.uni.fmi.mjt.netflix;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record Content (String id, String title, ContentType type, String description, int releaseYear, int runtime,
                       List<String> genres, int seasons, String imdbId, double imdbScore, double imdbVotes)
{
    public static final String GENRE_SEPERATOR = ";";

    public static Content of(String... attributes) {
        String id = attributes[ContentAttribute.ID.ordinal()];
        String title = attributes[ContentAttribute.TITLE.ordinal()];
        ContentType type = ContentType.valueOf(attributes[ContentAttribute.TYPE.ordinal()]);
        String description = attributes[ContentAttribute.DESCRIPTION.ordinal()];
        int releaseYear = Integer.parseInt(attributes[ContentAttribute.RELEASE_YEAR.ordinal()]);
        int runtime = Integer.parseInt(attributes[ContentAttribute.RUNTIME.ordinal()]);
        List<String> genres = attributes[ContentAttribute.GENRES.ordinal()].equals("[]") ? List.of() :
                Arrays.stream(attributes[ContentAttribute.GENRES.ordinal()]
                .substring(1, attributes[ContentAttribute.GENRES.ordinal()].length() - 1)
                .replaceAll("'", "")
                .replaceAll(" ", "")
                .split(GENRE_SEPERATOR))
                .toList();
        int seasons = Integer.parseInt(attributes[ContentAttribute.SEASONS.ordinal()]);
        String imdbId = attributes[ContentAttribute.IMDB_ID.ordinal()];
        double imdbScore = Double.parseDouble(attributes[ContentAttribute.IMDB_SCORE.ordinal()]);
        double imdbVotes = Double.parseDouble(attributes[ContentAttribute.IMDB_VOTES.ordinal()]);

        return new Content(id, title, type, description, releaseYear, runtime, genres, seasons, imdbId, imdbScore, imdbVotes);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return id.equals(content.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
