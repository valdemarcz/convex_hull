/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package convhull;

import java.util.Objects;

/**
 *
 * @author Ewa
 */
final class Point implements Comparable<Point> {
	
	public final double x;
	public final double y;
	
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	
	public String toString() {
		return String.format("Point(%g, %g)", x, y);
	}
	
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Point))
			return false;
		else {
			Point other = (Point)obj;
			return x == other.x && y == other.y;
		}
	}
	
	
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	
	public int compareTo(Point other) {
		if (x != other.x)
			return Double.compare(x, other.x);
		else
			return Double.compare(y, other.y);
	}
	
}
