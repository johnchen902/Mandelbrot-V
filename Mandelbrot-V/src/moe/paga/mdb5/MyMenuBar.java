package moe.paga.mdb5;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class MyMenuBar extends JMenuBar {

	private final MyPanel target;
	private JFileChooser fileChooser;

	public MyMenuBar(MyPanel target) {
		this.target = Objects.requireNonNull(target);

		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("PNG file", "png"));
		File dirPictures = new File(System.getProperty("user.home"), "Pictures");
		if (dirPictures.isDirectory()) {
			fileChooser.setCurrentDirectory(dirPictures);
		}

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		add(mnFile);

		JMenuItem mntmSave = new JMenuItem("Save", KeyEvent.VK_S);
		mntmSave.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		mnFile.add(mntmSave);
		mntmSave.addActionListener(e -> saveImage());

		JMenuItem mntmExit = new JMenuItem("Exit", KeyEvent.VK_X);
		mnFile.add(mntmExit);
		mntmExit.addActionListener(e -> {
			Window w = SwingUtilities.getWindowAncestor(this.target);
			w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
		});

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic(KeyEvent.VK_H);
		add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About Mandelbrot V", KeyEvent.VK_A);
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke("F1"));
		mnHelp.add(mntmAbout);
		mntmAbout.addActionListener(e -> JOptionPane.showMessageDialog(this.target, createAbout(), "About Mandelbrot V",
				JOptionPane.PLAIN_MESSAGE));
	}

	private void saveImage() {
		fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory(), createFileName()));
		int result = fileChooser.showSaveDialog(target);
		if (result != JFileChooser.APPROVE_OPTION)
			return;

		BufferedImage image = new BufferedImage(target.getWidth(), target.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		target.paintComponent(graphics);
		graphics.dispose();

		try {
			ImageIO.write(image, "png", fileChooser.getSelectedFile());
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(target, ex, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private String createFileName() {
		StringBuilder s = new StringBuilder();
		s.append("Mandelbrot-");
		for (Location.Quadrant q : target.getImageLocation().getQuadrants())
			s.append(q.ordinal() + 1);
		Offset offset = target.getOffset();
		s.append("-").append(offset.getX()).append("-").append(offset.getY());
		s.append("-").append(target.getWidth()).append("x").append(target.getHeight());
		s.append(".png");
		return s.toString();
	}

	private Object createAbout() {
		JTextArea textArea = new JTextArea(
			// @formatter:off
			"                             Written by johnchen902                             \n" +
			"                                                                                \n" +
			"                      Usage: drag to move; scroll to zoom.                      \n" +
			"                                                                                \n" +
			"  ------------------------ GNU General Public License ------------------------  \n" +
			"  |  This program is free software; you can redistribute it and/or modify    |  \n" +
			"  |  it under the terms of the GNU General Public License as published by    |  \n" +
			"  |  the Free Software Foundation; either version 2 of the License, or       |  \n" +
			"  |  (at your option) any later version.                                     |  \n" +
			"  |                                                                          |  \n" +
			"  |  This program is distributed in the hope that it will be useful,         |  \n" +
			"  |  but WITHOUT ANY WARRANTY; without even the implied warranty of          |  \n" +
			"  |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           |  \n" +
			"  |  GNU General Public License for more details.                            |  \n" +
			"  |                                                                          |  \n" +
			"  |  You should have received a copy of the GNU General Public License along |  \n" +
			"  |  with this program; if not, write to the Free Software Foundation, Inc., |  \n" +
			"  |  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.             |  \n" +
			"  ----------------------------------------------------------------------------  "
			// @formatter:on
		);
		textArea.setEditable(false);
		textArea.setOpaque(false);
		Font font = textArea.getFont();
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, font.getSize()));
		return textArea;
	}
}
