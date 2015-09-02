package moe.paga.mdb5;

import static moe.paga.mdb5.Location.Quadrant.I;
import static moe.paga.mdb5.Location.Quadrant.II;
import static moe.paga.mdb5.Location.Quadrant.III;
import static moe.paga.mdb5.Location.Quadrant.IV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * An immutable representation of a sub-rectangle within a "root" rectangle.
 * 
 * A location is either root or one of a quadrant of its parent,
 * 
 * @author johnchen902
 */
public final class Location implements Comparable<Location> {

	/**
	 * Quadrants as defined mathematically.
	 * 
	 * @author johnchen902
	 */
	public static enum Quadrant {
		I, II, III, IV;

		public boolean isXPositive() {
			return this == I || this == IV;
		}

		public boolean isYPositive() {
			return this == I || this == II;
		}
	}

	private final List<Quadrant> quadrants;

	/**
	 * Create a "root" location.
	 */
	public Location() {
		this.quadrants = Collections.emptyList();
	}

	/**
	 * Create a location by following specified quadrants.
	 * 
	 * @param quadrants
	 *            the specified quadrants; the first in the list is the most
	 *            significant one
	 * @throws NullPointerException
	 *             if the specified list is null
	 */
	public Location(List<Quadrant> quadrants) {
		this.quadrants = Collections.unmodifiableList(new ArrayList<>(quadrants));
		if (this.quadrants.contains(null))
			throw new IllegalArgumentException("contains null");
	}

	/**
	 * Get the quadrants needed to follow from root to reach this location.
	 * 
	 * @return an unmodifiable list of quadrants
	 */
	public List<Quadrant> getQuadrants() {
		return quadrants;
	}

	/**
	 * Return whether it is a "root" location.
	 * 
	 * @return whether it is a "root" location
	 */
	public boolean isRoot() {
		return quadrants.isEmpty();
	}

	/**
	 * Get the parent location, of which this location is a quadrant.
	 * 
	 * @return the parent location
	 * @throws NoSuchElementException
	 *             if this location is a root location, which has no parent
	 *             location
	 */
	public Location parent() {
		if (isRoot())
			throw new NoSuchElementException("is root");
		List<Quadrant> q = new ArrayList<>(quadrants);
		q.remove(q.size() - 1);
		return new Location(q);
	}

	/**
	 * Get a child location by following the specified quadrant.
	 * 
	 * @param e
	 *            the specified quadrant
	 * @return the child location
	 */
	public Location child(Quadrant e) {
		Objects.requireNonNull(e);
		List<Quadrant> q = new ArrayList<>(quadrants);
		q.add(e);
		return new Location(q);
	}

	/**
	 * Get the location with X increased by one least significant quadrant.
	 * Wraps around to the smallest X if exceeded boundary.
	 * 
	 * @return the location with X increased
	 */
	public Location increaseX() {
		return operate(II, I, IV, III, II, III);
	}

	/**
	 * Get the location with X decreased by one least significant quadrant.
	 * Wraps around to the largest X if exceeded boundary.
	 * 
	 * @return the location with X decreased
	 */
	public Location decreaseX() {
		return operate(II, I, IV, III, I, IV);
	}

	/**
	 * Get the location with Y increased by one least significant quadrant.
	 * Wraps around to the smallest Y if exceeded boundary.
	 * 
	 * @return the location with Y increased
	 */
	public Location increaseY() {
		return operate(IV, III, II, I, III, IV);
	}

	/**
	 * Get the location with Y decreased by one least significant quadrant.
	 * Wraps around to the largest Y if exceeded boundary.
	 * 
	 * @return the location with Y decreased
	 */
	public Location decreaseY() {
		return operate(IV, III, II, I, I, II);
	}

	private Location operate(Quadrant a, Quadrant b, Quadrant c, Quadrant d, Quadrant e, Quadrant f) {
		List<Quadrant> q = new ArrayList<>(quadrants);
		ListIterator<Quadrant> i = q.listIterator(q.size());
		while (i.hasPrevious()) {
			Quadrant x = i.previous();
			switch (x) {
			// @formatter:off
			case I:   i.set(a); break;
			case II:  i.set(b); break;
			case III: i.set(c); break;
			case IV:  i.set(d); break;
			default:  throw new AssertionError();
			// @formatter:on
			}
			if (x == e || x == f)
				break;
		}
		return new Location(q);
	}

	@Override
	public int hashCode() {
		return quadrants.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Location) && quadrants.equals(((Location) obj).quadrants);
	}

	/**
	 * Compare two location by comparing their quadrants lexicographically.
	 * 
	 * @param loc
	 *            the location to be compared
	 * @return a negative integer, zero, or a positive integer as this location
	 *         is less than, equal to, or greater than the specified location.
	 */
	@Override
	public int compareTo(Location loc) {
		Iterator<Quadrant> i = quadrants.iterator(), j = loc.quadrants.iterator();
		while (i.hasNext() && j.hasNext()) {
			Quadrant x = i.next(), y = j.next();
			if (x != y)
				return Integer.compare(x.ordinal(), y.ordinal());
		}
		return Boolean.compare(i.hasNext(), j.hasNext());
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(10 + quadrants.size() + 1);
		s.append("Location [");
		for (Quadrant q : quadrants)
			s.append(q.ordinal() + 1);
		s.append("]");
		return s.toString();
	}
}
