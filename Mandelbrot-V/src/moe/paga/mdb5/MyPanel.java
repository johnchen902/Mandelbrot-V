package moe.paga.mdb5;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

import javax.swing.SwingWorker;

import moe.paga.mdb5.Location.Quadrant;
import moe.paga.mdb5.func.DirectImage;
import moe.paga.mdb5.func.MandelbrotDouble;
import moe.paga.mdb5.func.Reverse;
import moe.paga.mdb5.func.Utilities;

@SuppressWarnings("serial")
public class MyPanel extends Canvas {

	private static final int PANEL_SIZE = 512;
	private static final int CHUNK_SIZE = 128;
	private Map<Location, Image> images;
	private Location location;
	private Offset offset;
	private Size chunkSize;

	public MyPanel() {
		images = new HashMap<>();
		location = new Location(Arrays.asList(Quadrant.III, Quadrant.III));
		offset = new Offset();
		chunkSize = new Size(CHUNK_SIZE, CHUNK_SIZE);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PANEL_SIZE, PANEL_SIZE);
	}

	/**
	 * Get the location of the top-left sub-image.
	 * 
	 * @return the location of the top-left sub-image
	 * @see #setImageLocation(Location)
	 */
	public Location getImageLocation() {
		return location;
	}

	/**
	 * Get the offset from the origin of top-left sub-image to the origin of
	 * this component.
	 * 
	 * @see #setOffset(Offset)
	 * 
	 * @return the offset of top-left sub-image
	 */
	public Offset getOffset() {
		return offset;
	}

	/**
	 * Set the location of the top-left sub-image.
	 * 
	 * @param location
	 *            the location of the top-left sub-image
	 * @see #getImageLocation()
	 */
	public void setImageLocation(Location location) {
		this.location = Objects.requireNonNull(location);
		repaint();
	}

	/**
	 * Set the offset from the origin of top-left sub-image to the origin of
	 * this component. Actual location and offset may be normalized.
	 * 
	 * @param offset
	 *            the offset of top-left sub-image
	 * @see #getOffset()
	 */
	public void setOffset(Offset offset) {
		setImageLocationAndOffset(location, offset);
	}

	/**
	 * Set the location and the offset of top-left sub-image at once. Actual
	 * location and offset may be normalized.
	 * 
	 * @param location
	 *            the location of the top-left sub-image
	 * @param offset
	 *            the offset of top-left sub-image
	 * @see #setImageLocation(Location)
	 * @see #setOffset(Offset)
	 */
	public void setImageLocationAndOffset(Location location, Offset offset) {
		this.location = offset.normalize(location, getChunkSize());
		this.offset = offset.normalize(getChunkSize());
		repaint();
	}

	/**
	 * Get the size of each sub-image.
	 * 
	 * @return the size of each sub-image
	 */
	public Size getChunkSize() {
		return chunkSize;
	}

	private BiFunction<Location, Size, Image> getFunction() {
		return Utilities.compose(new MandelbrotDouble().andThen(new Reverse()), new DirectImage());
	}

	private Image getImage(Location location) {
		if (!images.containsKey(location)) {
			images.put(location, null);
			if (!location.isRoot()) {
				Image parent = images.get(location.parent());
				if (parent != null) {
					int w = getChunkSize().getWidth(), h = getChunkSize().getHeight();
					Image image = createImage(w, h);
					int x = location.getQuadrantFromParent().isXPositive() ? w / 2 : 0;
					int y = location.getQuadrantFromParent().isYPositive() ? h / 2 : 0;
					Graphics graphics = image.getGraphics();
					graphics.drawImage(parent, 0, 0, w, h, x, y, x + w / 2, y + h / 2, this);
					graphics.dispose();
					images.put(location, image);
				}
			}
			new SwingWorker<Image, Void>() {
				@Override
				protected Image doInBackground() throws Exception {
					return getFunction().apply(location, getChunkSize());
				}

				@Override
				protected void done() {
					try {
						images.put(location, get());
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
					repaint();
				}
			}.execute();
		}
		return images.get(location);
	}

	@Override
	public void update(Graphics g0) {
		Image buffer = createImage(getWidth(), getHeight());
		Graphics g = buffer.getGraphics();
		paint(g);
		g.dispose();
		g0.drawImage(buffer, 0, 0, this);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Location locY = location;
		for (int y = -offset.getY(); y < getHeight(); y += chunkSize.getHeight(), locY = locY.increaseY()) {
			Location loc = locY;
			for (int x = -offset.getX(); x < getWidth(); x += chunkSize.getWidth(), loc = loc.increaseX()) {
				Image image = getImage(loc);
				if (image != null)
					g.drawImage(image, x, y, this);
			}
		}
	}
}
