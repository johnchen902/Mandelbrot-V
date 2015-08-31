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

	public MyPanel() {
		setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
		images = new HashMap<>();
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
					return getFunction().apply(location, new Size(CHUNK_SIZE, CHUNK_SIZE));
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

		Location locY = new Location(Arrays.asList(III, III));
		for (int y = 0; y < getHeight(); y += CHUNK_SIZE, locY = locY.increaseY()) {
			Location loc = locY;
			for (int x = 0; x < getWidth(); x += CHUNK_SIZE, loc = loc.increaseX()) {
				Image image = getImage(loc);
				if (image != null)
					g.drawImage(image, x, y, this);
			}
		}
	}
}
