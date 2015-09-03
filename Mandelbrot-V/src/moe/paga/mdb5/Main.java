package moe.paga.mdb5;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MyFrame f = new MyFrame();
				f.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				f.setTitle("Mandelbrot V by johnchen902");
				f.setLocationByPlatform(true);
				f.setVisible(true);
			}
		});
	}
}
