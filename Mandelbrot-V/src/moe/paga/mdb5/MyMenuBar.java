package moe.paga.mdb5;

import java.awt.Window;
import java.awt.event.WindowEvent;
import java.util.Objects;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class MyMenuBar extends JMenuBar {
	private final MyPanel target;

	public MyMenuBar(MyPanel target) {
		this.target = Objects.requireNonNull(target);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit", KeyEvent.VK_X);
		mnFile.add(mntmExit);
		mntmExit.addActionListener(e -> {
			Window w = SwingUtilities.getWindowAncestor(this.target);
			w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
		});
	}
}
