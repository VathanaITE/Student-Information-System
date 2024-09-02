package forms.SIS;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Loginform extends JFrame{
    private JPanel Mainpanel;
    private JPanel leftp;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JPanel rightp;
    private JLabel ulabel;
    private JLabel plabel;
    private JTextField utextField1;
    private JPasswordField passwordField1;
    private JCheckBox pcheckBox1;
    private JButton LOGINButton;


    public  Loginform (){
        //super(parent);
        setContentPane(Mainpanel);

        setTitle("STUDENT INFORMATION SYSTEM");
        setMinimumSize(new Dimension(100,700));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        // Load the new logo icon (old icon of java icon on the top-left of frame
        try {
            BufferedImage logoImage = ImageIO.read(new File("C:\\Users\\KENSAVANGVATHANA\\IdeaProjects\\SIS_javaproject\\src\\images\\education-cap.png"));
            setIconImage(logoImage);
        } catch (IOException e) {
            e.printStackTrace();
        }


        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name= utextField1.getText();
                String password = new String(passwordField1.getPassword());
                if (name.isEmpty()){
                    JOptionPane.showMessageDialog(Mainpanel, "ENTER USERNAME PLEASE..!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }else if(password.isEmpty()){
                    JOptionPane.showMessageDialog(Mainpanel, "ENTER PASSWORD PLEASE..!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else if (name.equals("admin") && password.equals("1111")){
                    SISForm sisForm = new SISForm();
                    dispose();

                }else {
                    JOptionPane.showMessageDialog(Mainpanel, "  WRONG USERNAME OR PASSWORD...!!!",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                    utextField1.setText("");
                    passwordField1.setText("");

                }
            }
        });

        pcheckBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pcheckBox1.isSelected()){
                    passwordField1.setEchoChar((char)0);
                }else {
                    passwordField1.setEchoChar('*');
                }
            }
        });
    }
}
