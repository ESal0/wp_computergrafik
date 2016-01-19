package computergraphics.framework;

import javax.swing.*;
import java.awt.*;

/**
 * Simple viewer for images.
 */
public class ImageViewer extends JFrame {

    /**
     * Generated ID, required for JFrame classes.
     */
    private static final long serialVersionUID = 8140257581540101857L;

    /**
     * Constructor. Displays the image.
     */
    public ImageViewer(Image image) {
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(image));
        getContentPane().add(label);

        setLocation(0, 0);
        setSize(image.getWidth(null), image.getHeight(null));
        setVisible(true);
    }

}
