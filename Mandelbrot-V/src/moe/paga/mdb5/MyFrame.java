package moe.paga.mdb5;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MyFrame extends JFrame {
	public MyFrame() {
		MyPanel myPanel = new MyPanel();
		MyMouseListener.addFor(myPanel);
		add(myPanel);
		pack();
	}
}
