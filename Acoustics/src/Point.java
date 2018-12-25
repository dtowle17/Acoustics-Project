public class Point {
	public float x;
	public float y;
	
	Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void addX(float x) {
		this.x += x;
	}
	public void addY(float y) {
		this.y += y;
	}
	
	public float slopeWith(Point p) {
		return Geo.slope(this, p);
	}
	
	public float angleTo(Point p) {
		return Geo.angle(this, p);
	}

	public float distanceTo(Point p) {
		return Geo.distance(this, p);
	}

	public Point boost(Point p) {
		return Geo.boost(this, p);
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
