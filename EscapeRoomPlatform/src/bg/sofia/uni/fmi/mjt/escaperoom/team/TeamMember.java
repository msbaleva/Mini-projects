package bg.sofia.uni.fmi.mjt.escaperoom.team;

import java.time.LocalDateTime;

public class TeamMember {

    private final String name;
    private final LocalDateTime birthday;

    public TeamMember(String name, LocalDateTime birthday) {
        this.name = name;
        this.birthday = birthday;
    }

}


/*
public record TeamMember(String name, LocalDateTime birthday) {
}
 */