package bg.sofia.uni.fmi.mjt.airbnb.accommodation.location;

public class Location {
	
	private final double x;
	private final double y;
	
	public Location(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public boolean isInRange(double maxDistance, Location other) {
		double distX2  = (x - other.x) * (x - other.x);
		double distY2 = (y - other.y) * (y - other.y);
		return maxDistance >= Math.sqrt(distX2 + distY2);
	}

	@Override
	public String toString() {
		return x + " " + y;
	}
}
