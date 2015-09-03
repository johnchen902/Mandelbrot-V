package moe.paga.mdb5;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.util.Objects;

import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class MyMenuBar extends MenuBar {
	private final MyPanel target;

	public MyMenuBar(MyPanel target) {
		this.target = Objects.requireNonNull(target);

		Menu mnFile = new Menu("File");
		add(mnFile);

		MenuItem mntmExit = new MenuItem("Exit");
		mnFile.add(mntmExit);
		mntmExit.addActionListener(e -> {
			Window w = SwingUtilities.getWindowAncestor(this.target);
			w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
		});
	}
}
