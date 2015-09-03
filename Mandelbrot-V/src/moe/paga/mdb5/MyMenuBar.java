package moe.paga.mdb5;

import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class MyMenuBar extends JMenuBar {

	private final MyPanel target;
	private JFileChooser fileChooser;
	private PrinterJob job;

	public MyMenuBar(MyPanel target) {
		this.target = Objects.requireNonNull(target);

		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("PNG file", "png"));
		File dirPictures = new File(System.getProperty("user.home"), "Pictures");
		if (dirPictures.isDirectory()) {
			fileChooser.setCurrentDirectory(dirPictures);
		}

		job = PrinterJob.getPrinterJob();
		job.setPrintable(createPrintable());

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		add(mnFile);

		JMenuItem mntmSave = new JMenuItem("Save", KeyEvent.VK_S);
		mntmSave.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		mnFile.add(mntmSave);
		mntmSave.addActionListener(e -> saveImage());

		JMenuItem mntmPrint = new JMenuItem("Print...", KeyEvent.VK_P);
		mntmPrint.setAccelerator(KeyStroke.getKeyStroke("ctrl P"));
		mnFile.add(mntmPrint);
		mntmPrint.addActionListener(e -> printImage());

		JMenuItem mntmExit = new JMenuItem("Exit", KeyEvent.VK_X);
		mnFile.add(mntmExit);
		mntmExit.addActionListener(e -> {
			Window w = SwingUtilities.getWindowAncestor(this.target);
			w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
		});
	}

	private Printable createPrintable() {
		return (g, format, page) -> {
			if (page > 0)
				return Printable.NO_SUCH_PAGE;

			double scale = Math.min(format.getImageableWidth() / target.getWidth(),
					format.getImageableHeight() / target.getHeight());

			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(format.getImageableX(), format.getImageableY());
			g2d.scale(scale, scale);
			target.printAll(g2d);

			return Printable.PAGE_EXISTS;
		};
	}

	private void printImage() {
		if (job.printDialog())
			try {
				job.print();
			} catch (PrinterException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(target, ex, "Error", JOptionPane.ERROR_MESSAGE);
			}
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
}
