package moe.paga.mdb5;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MyFrame f = new MyFrame();
				f.setDefaultCloseOperation(MyFrame.EXIT_ON_CLOSE);
				f.setTitle("Mandelbrot V by johnchen902");
				f.setLocationByPlatform(true);
				f.setVisible(true);
			}
		});
	}
}
