package forms.SIS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Exitform extends JFrame{
    private JPanel mainpanel;
    private JButton exitButton;
    public Exitform(){
        setContentPane(mainpanel);
        setTitle("EXIT");
        setMinimumSize(new Dimension(300,200));
        setLocationRelativeTo(null);
        //setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        try {
            BufferedImage icon = ImageIO.read(new File("C:\\Users\\KENSAVANGVATHANA\\IdeaProjects\\SIS_javaproject\\src\\images\\education-cap.png"));
            setIconImage(icon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
