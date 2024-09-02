package forms.SIS;

import db.DBConnection;
import net.proteanit.sql.DbUtils;

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

public class SISForm extends JFrame{
    private JPanel mainpanel;
    private JButton attendecneButton;
    private JButton addbutton;
    private JButton editButton;
    private JButton courseButton;
    private JTable studentTable;
    private JButton exitbutton;
    private JButton deleteButton;
    private DefaultTableModel model;
    Connection connection;
    private ListSelectionModel selectionModel = studentTable.getSelectionModel();


    public SISForm(){
        connection = DBConnection.getConnection();
        if (connection != null) {
            System.out.println("success to connect");
        } else {
            System.out.println("can not connect to database!!");
        }
        setContentPane(mainpanel);
        setTitle("STUDENT INFORMATION SYSTEM");
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000,700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        initTable();
        //change default java icon on the left top
        try {
            BufferedImage icon = ImageIO.read(new File("C:\\Users\\KENSAVANGVATHANA\\IdeaProjects\\SIS_javaproject\\src\\images\\education-cap.png"));
            setIconImage(icon);
        } catch (IOException e) {
            throw new RuntimeException(e);
               // or  e.printStackTrace();
        }

        //Button add
        addbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddstudentForm addform = new AddstudentForm(model);
            }
        });

        //Button Edit
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectionModel.isSelectionEmpty()){
                    JOptionPane.showMessageDialog(mainpanel,"Select student to Edit information");
                }else {
                    Editform editform = new Editform(model,selectionModel);
                }

            }
        });

        //Button Delete
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!selectionModel.isSelectionEmpty()) {
                        String query = "delete from SISDB.StudentTable where Id=?";
                        int studentdelete = Integer.parseInt(model.getValueAt(selectionModel.getLeadSelectionIndex(), 0).toString());
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1,studentdelete);
                        preparedStatement.executeUpdate();
                        model.removeRow(selectionModel.getLeadSelectionIndex());
                        JOptionPane.showMessageDialog(mainpanel,"Delete success!!");
                    }
                }catch (SQLException ex) {
                        JOptionPane.showMessageDialog(mainpanel,"Select student to delete.!!");
                    }
                }
        });

        //Button course management
        courseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Courseform courseform = new Courseform();
            }
        });

        //Exit Button
        exitbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Exitform exit = new Exitform();
            }
        });
    }

    public void initTable() {
        model = (DefaultTableModel) studentTable.getModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Gender");
        model.addColumn("Grade");
        model.addColumn("Course");
        try {
            String query ="SELECT StudentTable.Id, StudentTable.student_name, GenderTable.gender, GradeTable.grade, CourseTable.coursename\n" +
                    "FROM SISDB.StudentTable \n" +
                    "JOIN SISDB.GenderTable  ON StudentTable.student_gender = GenderTable.Id\n" +
                    "JOIN SISDB.GradeTable   ON StudentTable.grade = GradeTable.Idgrade\n" +
                    "JOIN SISDB.CourseTable  ON StudentTable.course = CourseTable.idcourse order by StudentTable.Id asc;";
            ResultSet result = connection.createStatement().executeQuery(query);

            while (result.next()) {
                String Id = result.getString(1);
                String name = result.getString(2);
                String gender = result.getString(3);
                String grade = result.getString(4);
                String course = result.getString(5);
                Object[] data = {Id, name, gender,grade, course};
                model.addRow(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


        public static void main(String[] args) {
        new SISForm();

    }
}
