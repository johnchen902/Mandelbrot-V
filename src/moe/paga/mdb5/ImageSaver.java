package moe.paga.mdb5;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageSaver {

    private final MyPanel target;
    private JFileChooser fileChooser;

    public ImageSaver(MyPanel target) {
        this.target = Objects.requireNonNull(target);

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG file", "png"));
        File dirPictures = new File(System.getProperty("user.home"), "Pictures");
        if (dirPictures.isDirectory())
            fileChooser.setCurrentDirectory(dirPictures);
    }

    public void saveImage() {
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

