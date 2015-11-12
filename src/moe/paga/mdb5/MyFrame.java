package moe.paga.mdb5;

import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class MyFrame extends JFrame {
    public MyFrame() {
        MyPanel myPanel = new MyPanel();
        MyMouseListener.addFor(myPanel);
        add(myPanel);
        setJMenuBar(new MyMenuBar(myPanel));
        pack();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // hard shutdown after 1000 ms.
                new Timer(true).schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                }, 1000);
            }
        });
    }
}
