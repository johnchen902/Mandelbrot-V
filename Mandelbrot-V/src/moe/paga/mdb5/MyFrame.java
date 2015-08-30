package moe.paga.mdb5;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MyFrame extends JFrame {
	public MyFrame() {
		add(new MyPanel());
		pack();
	}
}
