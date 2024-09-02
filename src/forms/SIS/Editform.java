package forms.SIS;

import cls.KeyValue;
import cls.sisclass.Genderkeyvalue;
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
import java.util.Arrays;
import java.util.Iterator;

public class Editform extends JFrame{
    private JPanel mainpanel;
    private JTextField oldidF;
    private JTextField newnameF;
    private JComboBox newgenderbox;
    private JComboBox newcoursebox;
    private JButton updateButton;
    private JComboBox newgradebox;
    private Connection connection = DBConnection.getConnection();
    private int idcourse;
    private String coursename;
   // private int eid;


    public Editform(DefaultTableModel model , ListSelectionModel selectionModel){
        setContentPane(mainpanel);
        setTitle("EDIT INFORMATION STUDENT");
        setMinimumSize(new Dimension(600,400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        try {
            BufferedImage icon = ImageIO.read(new File("C:\\Users\\KENSAVANGVATHANA\\IdeaProjects\\SIS_javaproject\\src\\images\\education-cap.png"));
            setIconImage(icon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initGcomboBox();
        initgradecomboBox();
        initcoursecomboBox();

        //get information from selection
        try {
            int oldid = Integer.parseInt(model.getValueAt(selectionModel.getLeadSelectionIndex(),0).toString());
            String query ="SELECT StudentTable.Id, StudentTable.student_name, GenderTable.Id,GenderTable.gender, GradeTable.Idgrade,GradeTable.grade, \n" +
                    "CourseTable.idcourse,CourseTable.coursename\n" +
                    "FROM SISDB.StudentTable \n" +
                    "JOIN SISDB.GenderTable  ON StudentTable.student_gender = GenderTable.Id\n" +
                    "JOIN SISDB.GradeTable   ON StudentTable.grade = GradeTable.Idgrade\n" +
                    "JOIN SISDB.CourseTable  ON StudentTable.course = CourseTable.idcourse\n where StudentTable.Id = ? ";
            PreparedStatement preparedStatement  = connection.prepareStatement(query);
            preparedStatement.setInt(1,oldid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                //System.out.println("can get value");
                //get value
                 int eid =resultSet.getInt(1);
                String ename= resultSet.getString(2);
                int eidcourse = resultSet.getInt(7);
                String ecoursename = resultSet.getString("coursename");
                Genderkeyvalue egender = new Genderkeyvalue(resultSet.getInt(3),resultSet.getString("gender"));
                Genderkeyvalue egrade = new Genderkeyvalue(resultSet.getInt(5),resultSet.getString("grade"));
                //set value to text field
                oldidF.setText(String.valueOf(eid));
                newnameF.setText(ename);
                newgenderbox.getModel().setSelectedItem(egender);
                newgradebox.getModel().setSelectedItem(egrade);
                newcoursebox.getModel().setSelectedItem(ecoursename);
            }else {
                System.out.println("can't get value");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int newid = Integer.parseInt(oldidF.getText());
                    String newname = newnameF.getText();

                    Genderkeyvalue newgender = (Genderkeyvalue) newgenderbox.getSelectedItem();
                    int keygender = newgender.getgKey();
                    String valuegender = newgender.getgValue();

                    Genderkeyvalue newgrade = (Genderkeyvalue) newgradebox.getSelectedItem();
                    int keygrade = newgrade.getgKey();
                    String valuegrade = newgrade.getgValue();

                    int newidcourse = newcoursebox.getSelectedIndex();
                    String newcourse = newcoursebox.getSelectedItem().toString();

                    PreparedStatement preparedStatement = connection.prepareStatement("Update StudentTable set Id = ?, student_name = ?, Id = ?, Idgrade = ?, idcourse = ? where StudentTable.Id = ? ");
                    preparedStatement.setInt(1,newid);
                    preparedStatement.setString(2,newname);
                    preparedStatement.setInt(3,keygender);
                    preparedStatement.setInt(4,keygrade);
                    preparedStatement.setInt(5,newidcourse);
                    preparedStatement.setInt(6, (Integer) model.getValueAt(selectionModel.getLeadSelectionIndex(),0));
                    preparedStatement.execute();
                    model.setValueAt(newid,selectionModel.getLeadSelectionIndex(),0);
                    model.setValueAt(newname,selectionModel.getLeadSelectionIndex(),1);
                    model.setValueAt(valuegender,selectionModel.getLeadSelectionIndex(),2);
                    model.setValueAt(valuegrade,selectionModel.getLeadSelectionIndex(),3);
                    model.setValueAt(newcourse,selectionModel.getLeadSelectionIndex(),4);

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void initGcomboBox(){
        Genderkeyvalue[] items = {new Genderkeyvalue(0,""),
                new Genderkeyvalue(1,"Male"),
                new Genderkeyvalue(2,"Female")};
        Iterator iterator = Arrays.stream(items).iterator();
        while (iterator.hasNext()){
            newgenderbox.addItem(iterator.next());
        }
    }
    public void initgradecomboBox(){
        Genderkeyvalue[] items = {new Genderkeyvalue(0,""),
                new Genderkeyvalue(1,"A"),
                new Genderkeyvalue(2,"B"),
                new Genderkeyvalue(3,"C"),
                new Genderkeyvalue(4,"D"),
                new Genderkeyvalue(5,"E")};
        Iterator iterator = Arrays.stream(items).iterator();
        while (iterator.hasNext()){
            newgradebox.addItem(iterator.next());
        }
    }
    public void initcoursecomboBox(){
        try{
            String query = "SELECT idcourse,coursename FROM SISDB.CourseTable";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                coursename = resultSet.getString("coursename");
                idcourse = resultSet.getInt("idcourse");
                newcoursebox.addItem(coursename);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
