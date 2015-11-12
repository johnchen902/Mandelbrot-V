package moe.paga.mdb5;

import java.awt.Component;
import javax.swing.*;

// TODO document
public class LookAndFeelChooser {
    private LookAndFeelChooser() {
    }

    public static boolean showDialog(Component parentComponent) {
        UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        LookAndFeel currentLaf = UIManager.getLookAndFeel();
        String className = currentLaf == null ? null : currentLaf.getClass().getName();
        JToggleButton[] buttons = new JToggleButton[infos.length];

        JPanel panel = new JPanel();
        ButtonGroup group = new ButtonGroup();
        for(int i = 0; i < infos.length; i++) {
            buttons[i] = new JRadioButton(infos[i].getName());
            if(infos[i].getClassName().equals(className))
                buttons[i].setSelected(true);
            panel.add(buttons[i]);
            group.add(buttons[i]);
        }

        int status = JOptionPane.showConfirmDialog(parentComponent, panel,
                "Select a Look and Feel", JOptionPane.OK_CANCEL_OPTION);

        if(status != JOptionPane.OK_OPTION)
            return false;

        for(int i = 0; i < infos.length; i++)
            if(buttons[i].isSelected()) {
                try {
                    UIManager.setLookAndFeel(infos[i].getClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }

        return false;
    }
}

