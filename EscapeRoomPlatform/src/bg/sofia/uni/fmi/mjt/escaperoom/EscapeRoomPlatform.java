package bg.sofia.uni.fmi.mjt.escaperoom;

import bg.sofia.uni.fmi.mjt.escaperoom.exception.PlatformCapacityExceededException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.RoomNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.exception.TeamNotFoundException;
import bg.sofia.uni.fmi.mjt.escaperoom.room.EscapeRoom;
import bg.sofia.uni.fmi.mjt.escaperoom.room.Review;
import bg.sofia.uni.fmi.mjt.escaperoom.team.Team;

public class EscapeRoomPlatform implements EscapeRoomAdminAPI, EscapeRoomPortalAPI {

    private static final double BONUS_PERCENT_BEST = 0.5;
    private static final double BONUS_PERCENT_MID = 0.75;
    private static final int BONUS_PERCENT_BEST_POINTS = 2;
    private static final int BONUS_PERCENT_MID_POINTS = 1;
    private EscapeRoom[] escapeRooms;
    private Team[] teams;
    private int roomCount;
    private final int maxCapacity;

    public EscapeRoomPlatform(Team[] teams, int maxCapacity) {
        this.teams = teams;
        escapeRooms = new EscapeRoom[maxCapacity];
        this.maxCapacity = maxCapacity;
    }

    private boolean isInvalid(String string) {
        return string == null || string.isBlank() || string.isEmpty();
    }
    @Override
    public void addEscapeRoom(EscapeRoom room) throws RoomAlreadyExistsException {
        if (room == null) {
            throw new IllegalArgumentException("Invalid room.");
        }

        if (roomCount == maxCapacity) {
            throw new PlatformCapacityExceededException("Platform capacity is exceeded.");
        }

        for (EscapeRoom e : escapeRooms) {
            if (e.equals(room)) {
                throw new RoomAlreadyExistsException("This room already exists.");
            }
        }

        escapeRooms[roomCount++] = room;

    }

    @Override
    public void removeEscapeRoom(String roomName) throws RoomNotFoundException {
        if (isInvalid(roomName)) {
            throw new IllegalArgumentException("Invalid room name.");
        }

        /*for (int i = 0; i < roomCount; i++) {
            if (escapeRooms[i].getName().equals(roomName)) {
                escapeRooms[i] = escapeRooms[--roomCount];
                escapeRooms[roomCount] = null;
            }
        }*/

        EscapeRoom room = getEscapeRoomByName(roomName);
        //room = new EscapeRoom(escapeRooms[--roomCount]);
        room = escapeRooms[--roomCount];
        escapeRooms[roomCount] = null;

        //throw new RoomNotFoundException("This room does not exist.");
    }

    @Override
    public EscapeRoom[] getAllEscapeRooms() {
        EscapeRoom[] allRooms = new EscapeRoom[roomCount];
        /*for (int i = 0; i < roomCount; i++) {
            allRooms[i] = escapeRooms[i];
        }*/

        System.arraycopy(escapeRooms, 0, allRooms, 0, roomCount);

        return allRooms;
    }

    @Override
    public void registerAchievement(String roomName, String teamName, int escapeTime) throws RoomNotFoundException, TeamNotFoundException {
        EscapeRoom room = getEscapeRoomByName(roomName);
        Team team = getTeamByName(teamName);

        int maxTime = room.getMaxTimeToEscape();
        int points = room.getDifficulty().getRank();
        if (escapeTime <= 0 || escapeTime > maxTime) {
            throw new IllegalArgumentException("Escape time is invalid ot exceeds current max time.");
        }

        if (escapeTime <= BONUS_PERCENT_BEST * maxTime) {
            points += BONUS_PERCENT_BEST_POINTS;
        } else if (escapeTime <= BONUS_PERCENT_MID * maxTime) {
            points += BONUS_PERCENT_MID_POINTS;
        }

        team.updateRating(escapeTime);

    }

    @Override
    public EscapeRoom getEscapeRoomByName(String roomName) throws RoomNotFoundException {
       if (isInvalid(roomName)) {
           throw new IllegalArgumentException("Invalid room name.");
       }

        for (int i = 0; i < roomCount; i++) {
            if (escapeRooms[i].getName().equals(roomName)) {
                return escapeRooms[i];
            }
        }

        throw new RoomNotFoundException("This room does not exist.");
    }

    public Team getTeamByName(String teamName) throws TeamNotFoundException {
        if (isInvalid(teamName)) {
            throw new IllegalArgumentException("Invalid team name.");
        }

        for (Team team : teams) {
            if (team.getName().equals(teamName)) {
                return team;
            }
        }

        throw new TeamNotFoundException("This team does not exist.");
    }

    @Override
    public void reviewEscapeRoom(String roomName, Review review) throws RoomNotFoundException {
        if (isInvalid(roomName)) {
            throw new IllegalArgumentException("Invalid room name.");
        }

        EscapeRoom room = getEscapeRoomByName(roomName);
        room.addReview(review);
    }

    @Override
    public Review[] getReviews(String roomName) throws RoomNotFoundException {
        if (isInvalid(roomName)) {
            throw new IllegalArgumentException("Invalid room name.");
        }

        EscapeRoom room = getEscapeRoomByName(roomName);
        return room.getReviews();
    }

    @Override
    public Team getTopTeamByRating() {
        if (teams == null) { return null; }
        Team topTeam = teams[0];
        for (Team team: teams) {
            topTeam = topTeam.getRating() < team.getRating() ? team : topTeam;
        }

        return topTeam;
    }
}
