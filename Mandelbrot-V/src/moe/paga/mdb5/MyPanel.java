package moe.paga.mdb5;

import static moe.paga.mdb5.Location.Quadrant.III;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.BiFunction;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import moe.paga.mdb5.func.DirectImage;
import moe.paga.mdb5.func.MandelbrotDouble;
import moe.paga.mdb5.func.Reverse;
import moe.paga.mdb5.func.Utilities;

@SuppressWarnings("serial")
public class MyPanel extends JPanel {

	private static final int PANEL_SIZE = 512;
	private static final int CHUNK_SIZE = 128;
	private Map<Location, Image> images;
	private Location location;
	private Offset offset;
	private Size chunkSize;

	public MyPanel() {
		images = new HashMap<>();
		location = new Location(Arrays.asList(III, III));
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
		this.location = location;
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
		if (images.containsKey(location)) {
			return images.get(location);
		} else {
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
			return null;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

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
