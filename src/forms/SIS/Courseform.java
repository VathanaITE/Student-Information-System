package forms.SIS;

import db.DBConnection;

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

public class Courseform extends JFrame{
    private JTextField courseidfield;
    private JPanel panelmain;
    private JButton addcoursebutton;
    private JTable coursetable;
    private JButton deletecourse;
    private JTextField coursenamefield;
    private DefaultTableModel modelc;
    private Connection connection = DBConnection.getConnection();
    private ListSelectionModel selectionModel = coursetable.getSelectionModel();

    public Courseform(){
//        if (connection!=null){
//            System.out.println("can connect");
//        }else {System.out.println("fail");}
        setContentPane(panelmain);
        setTitle("COURSE MANAGEMENT");
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

        initTable();

        addcoursebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (courseidfield.getText().isEmpty()){
                    JOptionPane.showMessageDialog(panelmain,"Enter course ID","ERROR",JOptionPane.ERROR_MESSAGE);
                } else if (coursenamefield.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(panelmain,"Enter course Name","ERROR",JOptionPane.ERROR_MESSAGE);
                }else {
                try {
                    int coursid = Integer.parseInt(courseidfield.getText());
                    String coursename = coursenamefield.getText();
                    PreparedStatement preparedStatement = connection.prepareStatement("insert into CourseTable values (?,?)");
                    preparedStatement.setInt(1,coursid);
                    preparedStatement.setString(2, coursename);
                    preparedStatement.executeUpdate();
                    System.out.println("Added");
                    modelc.addRow(new Object[]{coursid,coursename});
                    courseidfield.setText("");
                    coursenamefield.setText("");
                } catch (SQLException ex) {
                    System.out.println("failed");
                    ex.printStackTrace();
                }}
            }
        });


        deletecourse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (selectionModel.isSelectionEmpty()){
                   System.out.println("select course!!");
               }else {
                   try
                   {
                       String query = "Delete from CourseTable where idcourse = ?";
                       PreparedStatement preparedStatement = connection.prepareStatement(query);
                       String courseid = modelc.getValueAt(selectionModel.getLeadSelectionIndex(),0).toString();
                       int idcourse = Integer.parseInt(courseid);
                       preparedStatement.setInt(1,idcourse);
                       preparedStatement.execute();
                       modelc.removeRow(selectionModel.getLeadSelectionIndex());
                       System.out.println("deleted");
                   } catch (SQLException ex) {
                       throw new RuntimeException(ex);
                   }
               }

            }
        });
    }

    public void initTable() {
        modelc = (DefaultTableModel) coursetable.getModel();
        modelc.addColumn("ID");
        modelc.addColumn("Course Name");
        try {
            String query ="select * from CourseTable ";
            ResultSet result = connection.createStatement().executeQuery(query);

            while (result.next()) {
                String Id = result.getString(1);
                String cname = result.getString(2);
                Object[] data = {Id, cname};
                modelc.addRow(data);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Courseform();
    }
}
