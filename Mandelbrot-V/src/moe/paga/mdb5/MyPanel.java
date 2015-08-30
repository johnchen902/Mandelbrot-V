package moe.paga.mdb5;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import moe.paga.mdb5.func.DirectImage;
import moe.paga.mdb5.func.MandelbrotDouble;
import moe.paga.mdb5.func.Reverse;
import moe.paga.mdb5.func.Utilities;

@SuppressWarnings("serial")
public class MyPanel extends JPanel {

	private Image image;

	public MyPanel() {
		setPreferredSize(new Dimension(512, 512));
		new SwingWorker<Image, Void>() {
			@Override
			protected Image doInBackground() throws Exception {
				return Utilities.compose(new MandelbrotDouble().andThen(new Reverse()), new DirectImage())
						.apply(new Location(), new Size(512, 512));
			}

			@Override
			protected void done() {
				try {
					image = get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				repaint();
			}
		}.execute();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null)
			g.drawImage(image, 0, 0, this);
	}
}
