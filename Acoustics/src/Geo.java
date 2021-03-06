public class Geo {
	private static float z = 0.001f;
	
	// A class for holding static geometric type methods
	
	public static float slope(Point p1, Point p2) {
		// Returns the slope between p1 and p2
		return (p1.y - p2.y) / (p1.x - p2.x);
	}
	
	public static float angle(Point p1, Point p2) {
		// Returns the angle from p1 to p2
		double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);
		if(angle < 0) angle += Math.PI * 2;
		return (float) angle;
	}

	public static float distance(Point p1, Point p2) {
		// Returns the distance between p1 and p2
		float xDif = p1.x - p2.x;
		float yDif = p1.y - p2.y;
		
		return (float) Math.sqrt(xDif * xDif + yDif * yDif);
	}

	public static Point boost(Point p1, Point p2) {
		// Returns the point double the distance from p1 as p2
		return new Point(p2.x * 2 - p1.x, p2.y * 2 - p1.y);
	}
	
	public static Point perpPoint(Point seg1, Point seg2, Point p) {
		// Returns the closest point from p to a line segment (seg)
		float px;
		float py;
		if(seg1.y == seg2.y) {
			// horizontal line protection
			px = p.x;
			py = seg1.y;
		} else if(seg1.x == seg2.x) {
			// vertical line protection
			px = seg1.x;
			py = p.y;
		} else {
			float m1 = seg1.slopeWith(seg2);
			float m2 = 1 / -m1;
			px = (m2 * p.x - p.y + seg1.y - m1 * seg1.x) / (m2 - m1);
			py = m1 * (px - seg1.x) + seg1.y;
		}
		return new Point(px, py);
	}
	
	public static float raySegDist(Point ray1, Point rayThru, Point seg1, Point seg2, boolean simp) {
		// Returns distance between intersection point of a ray and line segment
		Point inter = raySegIntersect(ray1, rayThru, seg1, seg2);
		float deltaX = inter.x - ray1.x;
		float deltaY = inter.y - ray1.y;
		
		float distSimp = deltaX * deltaX + deltaY * deltaY;
		if(simp) {
			// simplified distance for speed (use for comparing only)
			return distSimp;
		} else {
			// actual distance
			return (float) Math.sqrt(distSimp);
		}
	}
	public static float raySegDist(Point ray1, Point rayThru, Point seg1, Point seg2) {
		return raySegDist(ray1, rayThru, seg1, seg2, false);
	}
	
	public static Point raySegIntersect(Point ray1, Point rayThru, Point seg1, Point seg2) {
		// Returns the intersection point of a ray and line segment
		float mRay = slope(ray1, rayThru);
		float mSeg = slope(seg1, seg2);
		
		// x and y of intersection point
		float interX = (mRay * ray1.x - mSeg * seg1.x - ray1.y + seg1.y) / (mRay - mSeg);
		float interY = mRay * (interX - ray1.x) + ray1.y;
		
		return new Point(interX, interY);
	}
	public static Point raySegIntersect(Point ray1, float rayAng, Point seg1, Point seg2) {
		// Returns the intersection point of a ray and line segment
		float interX;
		float interY;
		float mSeg;
		float mRay;
		if(Math.abs(rayAng - Math.PI / 2) < z || Math.abs(rayAng - 3 * Math.PI / 2) < z) {
			// ray slope is vertical
			mSeg = slope(seg1, seg2);
			interX = ray1.x;
			interY = mSeg * (ray1.x - seg1.x) + seg1.y;
		} else if(seg1.x == seg2.x) {
			// seg slope is vertical
			mRay = (float) Math.tan(rayAng);
			interX = seg1.x;
			interY = mRay * (seg1.x - ray1.x) + ray1.y;
			
		} else {
			// x and y of intersection point
			mSeg = slope(seg1, seg2);
			mRay = (float) Math.tan(rayAng);
			interX = (mRay * ray1.x - mSeg * seg1.x - ray1.y + seg1.y) / (mRay - mSeg);
			interY = mRay * (interX - ray1.x) + ray1.y;
		}
		
		return new Point(interX, interY);
	}
	public static Point raySegIntersect(Point ray1, float rayAng, Point[] seg) {
		return raySegIntersect(ray1, rayAng, seg[0], seg[1]);
	}
	
	public static boolean angleFallsIn(float testAngle, float min, float max) {
		// Returns whether or not an angle falls within a minimum and maximum range
		if(min > max) {
			min -= 2 * Math.PI;
		}
		if(testAngle < min) {
			testAngle += 2 * Math.PI;
		}
		if(testAngle > min && testAngle < max) {
			return true;
		}
		return false;
	}
	
	public static int orientation(Point p, Point q, Point r) {
		// Returns the orientation of three points (i.e: CW, CWW, collinear)
	    float val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y); 
	    
	    if(val == 0) {
	    	return 0; //collinear
	    } else if(val > 0) {
	    	return 1; //CW
	    } else {
	    	return -1; //CCW
	    }
	}
	
	public static boolean intersects(Point p1, Point q1, Point p2, Point q2) {
		// Returns whether or not two line segments intersect
		int o1 = orientation(p1, q1, p2); 
	    int o2 = orientation(p1, q1, q2); 
	    int o3 = orientation(p2, q2, p1); 
	    int o4 = orientation(p2, q2, q1);
	    
	    if (o1 != o2 && o3 != o4) {
	    	return true;
	    }
	    return false;
	    // (!!!) doesn't cover collinear pairs
	}
	
	public static float angleBoost(float ang1, float ang2) {
		// Returns the angle of ang1 reflected over ang2
		
		/*if(ang2 < ang1) {
			System.out.println("test");
			ang2 += 2 * Math.PI;
		}*/
		float result = 2 * ang2 - ang1;
		if(result > 2 * Math.PI) {
			result -= 2 * Math.PI;
		} else if(result < 0) {
			result += 2 * Math.PI;
		}
		return result;
	}
	
	public static Point[] segCircIntersects(Point seg1, Point seg2, Point C, float R) {
		// Returns the intersection points between a circle at point C radius R, and a line along seg
		// compute the distance between seg1 and seg2
		float segDist = distance(seg1, seg2);

		// compute the direction vector D from seg1 to seg2
		float Dx = (seg2.x - seg1.x) / segDist;
		float Dy = (seg2.y - seg1.y) / segDist;

		// the equation of the line seg is x = Dx*t + seg1.x, y = Dy*t + seg1.y with 0 <= t <= LAB.

		// compute the distance between the points A and E, where
		// E is the point on seg closest the circle center (Cx, Cy)
		float t = Dx * (C.x - seg1.x) + Dy * (C.y - seg1.y);

		// compute the coordinates of the point E
		Point E = new Point(t * Dx + seg1.x, t * Dy + seg1.y);

		// compute the euclidean distance between E and C
		float LEC = distance(E, C);

		// test if the line intersects the circle
		if(LEC < R) {
			// compute distance from t to circle intersection point
			float dt = (float) Math.sqrt(R * R - LEC * LEC);
			
			// compute first intersection point
			float Fx = (t - dt) * Dx + seg1.x;
			float Fy = (t - dt) * Dy + seg1.y;
			
			// compute second intersection point
			float Gx = (t + dt) * Dx + seg1.x;
			float Gy = (t + dt) * Dy + seg1.y;
			
			Point[] points = new Point[2];
			points[0] = new Point(Fx, Fy);
			points[1] = new Point(Gx, Gy);
			return points;
			
		} else {
			// line doesn't touch circle or is tangent (treat tangent as no touch)
			return null;
		}
	}
	
	public static boolean firstIsMin(float ang1, float ang2) {
		// Returns the most CCW angle assuming the angle between both rays is less than 180 degrees
		return (Math.abs(ang1 - ang2) > Math.PI ^ ang1 - ang2 < 0);
	}
	
	public static boolean pointFallsWithin(Point pCheck, Point p1, Point p2) {
		// Returns whether or not pCheck falls within the rectangle formed by p1 and p2
		if(p1.x - pCheck.x > 0 ^ p2.x - pCheck.x > 0) {
			// falls in x boundary
			if(p1.y - pCheck.y > 0 ^ p2.y - pCheck.y > 0) {
				// falls in y boundary
				return true;
			}
		}
		return false;
	}
	
	public static boolean rectIntersect(Point p1, Point p2, Point q1, Point q2) {
		// Returns whether or not the rectangle formed by corner points p1 and p2
		//     intersects the rectangle formed by corner points q1 and q2
		float pMin = Math.min(p1.x, p2.x);
		float pMax = Math.max(p1.x, p2.x);
		float qMin = Math.min(q1.x, q2.x);
		float qMax = Math.max(q1.x, q2.x);
		if(pMin > qMax || qMin > pMax) {
			return false; 
		}
		
		if(!(Math.abs(p1.y - q1.y) < z && Math.abs(p2.y - q2.y) < z && Math.abs(p1.y - p2.y) < z)) {
			// same y protection
			pMin = Math.min(p1.y, p2.y);
			pMax = Math.max(p1.y, p2.y);
			qMin = Math.min(q1.y, q2.y);
			qMax = Math.max(q1.y, q2.y);
			if(pMin > qMax || qMin > pMax) {
				return false; 
			}
		}
		
		return true;
	}
	public static boolean rectIntersect(Point[] rect1, Point[] rect2) {
		return rectIntersect(rect1[0], rect1[1], rect2[0], rect2[1]);
	}
	
	public static Point center(Point p1, Point p2) {
		// Returns a new point in the middle of p1 and p2
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}
	
	/*
	public static void main(String[] args) {
		Point a = new Point(1, 2);
		Point b = new Point(4, 5);
		Point c = new Point(3, 4);
		
		System.out.println(c.fallsWithin(a, b));
	}
	*/
	
}
