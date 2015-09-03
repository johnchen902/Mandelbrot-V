package moe.paga.mdb5;

import java.awt.Frame;

@SuppressWarnings("serial")
public class MyFrame extends Frame {
	public MyFrame() {
		MyPanel myPanel = new MyPanel();
		MyMouseListener.addFor(myPanel);
		add(myPanel);
		setMenuBar(new MyMenuBar(myPanel));
		pack();
	}
}
