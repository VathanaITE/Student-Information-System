package forms.SIS;

import cls.sisclass.Genderkeyvalue;
import cls.sisclass.coursekey;
import db.DBConnection;
import net.proteanit.sql.DbUtils;
//import DBconnection conection;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

public class AddstudentForm extends JFrame{
    private JPanel mainpanel;
    private JTextField idfield;
    private JTextField namefield;
    private JComboBox coursebox;
    private JComboBox genderbox;
    private JButton addButton;
    private JComboBox gradebox;
    private Connection connection;
    private int idcourse;

    public AddstudentForm(DefaultTableModel model){
        connection = DBConnection.getConnection();
        setTitle("ADD STUDENT");
        setContentPane(mainpanel);
        setMinimumSize(new Dimension(600,400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        try {
            BufferedImage icon = ImageIO.read(new File("C:\\Users\\KENSAVANGVATHANA\\IdeaProjects\\SIS_javaproject\\src\\images\\education-cap.png"));
            setIconImage(icon);
        } catch (IOException e) {
            throw new RuntimeException(e);
            // or  e.printStackTrace();
        }

        //cell function
        initGcomboBox();
        initgradecomboBox();
        initcoursecomboBox();


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = namefield.getText();
                Genderkeyvalue genderboxSelectedItem = (Genderkeyvalue) genderbox.getSelectedItem();
                Genderkeyvalue gradeboxSelectedItem = (Genderkeyvalue) gradebox.getSelectedItem();
                //Genderkeyvalue courseSelectedItem = (Genderkeyvalue) coursebox.getSelectedItem();
                int keyGender = genderboxSelectedItem.getgKey();
                String gender = genderboxSelectedItem.getgValue();
                int keyGrade = gradeboxSelectedItem.getgKey();
                String grade = gradeboxSelectedItem.getgValue();
                int keyCourse = 1+coursebox.getSelectedIndex();
                String course =coursebox.getSelectedItem().toString();
              //  String course = courseSelectedItem.getgValue();

                 if (idfield.getText().isEmpty()){
                    JOptionPane.showMessageDialog(mainpanel, "ENTER ID",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                } else if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(mainpanel, "ENTER NAME",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }else if (genderbox.getSelectedItem().toString().isEmpty()) {
                    JOptionPane.showMessageDialog(mainpanel, "SELECT GENDER",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }else if (gradebox.getSelectedItem().toString().isEmpty()) {
                    JOptionPane.showMessageDialog(mainpanel, "SELECT GRADE",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }else if (coursebox.getSelectedItem().toString().isEmpty()) {
                    JOptionPane.showMessageDialog(mainpanel, "SELECT COURSE",
                            "ERROR", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    try {
                        int id = Integer.parseInt(idfield.getText());//convert to integer
                        PreparedStatement preparedStatement = connection.prepareStatement("insert into StudentTable values (?,?,?,?,?) ");
                        preparedStatement.setInt(1, id);
                        preparedStatement.setString(2, name);
                        preparedStatement.setInt(3,genderboxSelectedItem.getgKey());
                        preparedStatement.setInt(4,gradeboxSelectedItem.getgKey());
                        preparedStatement.setInt(5,keyCourse);
                        preparedStatement.execute();
                        System.out.println("Added");
                        model.addRow(new Object[]{id,name,gender,grade,course});
                        dispose();

                    } catch (SQLException ex) {
                        System.out.println("failed");
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    //add items to comboBox
    public void initGcomboBox(){
        Genderkeyvalue[] items = {new Genderkeyvalue(0,""),
                new Genderkeyvalue(1,"Male"),
                new Genderkeyvalue(2,"Female")};
        Iterator iterator = Arrays.stream(items).iterator();
        while (iterator.hasNext()){
            genderbox.addItem(iterator.next());
        }
    }

    //add items to gradeBox
    public void initgradecomboBox(){
        Genderkeyvalue[] items = {new Genderkeyvalue(0,""),
                new Genderkeyvalue(1,"A"),
                new Genderkeyvalue(2,"B"),
                new Genderkeyvalue(3,"C"),
                new Genderkeyvalue(4,"D"),
                new Genderkeyvalue(5,"E")};
        Iterator iterator = Arrays.stream(items).iterator();
        while (iterator.hasNext()){
            gradebox.addItem(iterator.next());
        }
    }
    public void initcoursecomboBox(){
        try{
            connection = DBConnection.getConnection();
            String query = "SELECT idcourse,coursename FROM SISDB.CourseTable";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String coursename = resultSet.getString("coursename");
                idcourse = resultSet.getInt("idcourse");
                //System.out.println(idcourse);
                coursebox.addItem(coursename);//coursename
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}