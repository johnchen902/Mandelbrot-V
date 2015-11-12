package moe.paga.mdb5;

/**
 * An immutable representation of dimensions of a rectangle.
 * 
 * @author johnchen902
 */
public final class Size {
	private final int width, height;

	/**
	 * Constructs a <code>Size</code> and initializes it to the specified width
	 * and specified height.
	 *
	 * @param width
	 *            the specified width
	 * @param height
	 *            the specified height
	 * @throws IllegalArgumentException
	 *             if either dimension is non-positive or the product of both
	 *             dimensions exceeds the range of <code>int</code>
	 */
	public Size(int width, int height) {
		this.width = width;
		this.height = height;
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException("non-positive dimension");
		if ((long) width * height > Integer.MAX_VALUE)
			throw new IllegalArgumentException("area too large");
	}

	/**
	 * Returns the width of this <code>Size</code>
	 * 
	 * @return the width of this <code>Size</code>
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of this <code>Size</code>
	 * 
	 * @return the height of this <code>Size</code>
	 */
	public int getHeight() {
		return height;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Size) {
			Size s = (Size) obj;
			return width == s.width && height == s.height;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return 31 * (31 + width) + height;
	}

	@Override
	public String toString() {
		return "Size [" + width + " * " + height + "]";
	}
}
