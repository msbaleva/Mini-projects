package bg.sofia.uni.fmi.mjt.escaperoom.team;

import bg.sofia.uni.fmi.mjt.escaperoom.rating.Ratable;

public class Team implements Ratable {

    private String name;
    private TeamMember[] members;
    private int rating;

    private Team(String name, TeamMember[] members) {
        this.name = name;
        this.members = members;
    }
    public static Team of(String name, TeamMember[] members) {
        return new Team(name, members);
    }

    /**
     * Updates the team rating by adding the specified points to it.
     *
     * @param points the points to be added to the team rating.
     * @throws IllegalArgumentException if the points are negative.
     */
    public void updateRating(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Invalid points.");
        }

        rating += points;
    }

    /**
     * Returns the team name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the rating of a ratable object.
     *
     * @return the rating
     */
    @Override
    public double getRating() {
        return rating;
    }
}
