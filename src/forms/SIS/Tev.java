package forms.SIS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tev extends JFrame{
    private JPanel meme;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JTable table1;

    public Tev(){
        setContentPane(meme);
        setMinimumSize(new Dimension(1000,600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        // Load the new logo image
        try {
            BufferedImage logoImage = ImageIO.read(new File("C:\\Users\\KENSAVANGVATHANA\\IdeaProjects\\SIS_javaproject\\src\\images\\Logo.png"));
            setIconImage(logoImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Tev();
    }
}
