package pattern.creater;


public class Room extends MapSite {
    private int roomNumber;
    private MapSite[] sides = new MapSite[4];

    MapSite getSide(Direction direction) {
        return sides[direction.ordinal()];
    }

    void setSide(Direction direction, MapSite mapSite) {
        sides[direction.ordinal()] = mapSite;
    }

    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Override
    public void enter() {
        System.out.println("Room enter");

    }
}
