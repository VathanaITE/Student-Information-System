package forms;
import cls.KeyValue;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import db.DBConnection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;


public class StudentForm extends JFrame {
    private JTextField lnamefiled;
    private JTextField knamefield;
    private JComboBox genderBox;
    private JLabel clabel;
    private JLabel lname;
    private JLabel kname;
    private JLabel glabel;
    private JLabel dlabel;
    private JButton addbutton;
    private JButton upbutton;
    private JButton dbutton;
    private JButton resetbutton;
    private JPanel mainpanel;
    private JTextField codefield;
    private JPanel panel2;
    private JTable table1;
    private JPanel panel1;
    private JPanel datepanel;
    private JScrollPane pane;
    DatePicker mydate;
    private DefaultTableModel model;
    Connection connection ;
    private JPanel panelData;
    private ListSelectionModel selectionModel = table1.getSelectionModel();

    public StudentForm() {
        setContentPane(mainpanel);
        setTitle("Student Form");
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        connection = DBConnection.getConnection();
        if (connection != null) {
            System.out.println("success to connect");
        } else {
            System.out.println("can not connect to database!!");
        }

        StartDatePicker();
        initComboBox();
        initTable();

        addbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    int id = Integer.parseInt(codefield.getText());
                    String latin = lnamefiled.getText();
                    String khmer = knamefield.getText();
                    KeyValue selectedItem = (KeyValue) genderBox.getSelectedItem();
                    int gender = selectedItem.getKey();
                    String genderValue = selectedItem.getValue();
                    LocalDate dob = mydate.getDate();

                    String query = "insert into student value(?,?,?,?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1,id);
                    preparedStatement.setString(2,latin);
                    preparedStatement.setString(3,khmer);
                    preparedStatement.setInt(4,gender);
                    preparedStatement.setString(5, dob.toString());
                    preparedStatement.execute();
                    model.addRow(new Object[]{id,latin,khmer,genderValue,dob.toString()});
                    System.out.println("added");

                }catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        });


//        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//
//
//            }
//        });
        resetbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codefield.setText("");
                lnamefiled.setText("");
                knamefield.setText("");
                genderBox.setSelectedItem(null);
                mydate.clear();
            }
        });
        dbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!selectionModel.isSelectionEmpty())
                {
                    try
                    {
                        String query = "Delete from student where studentID = ?";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1,(int)model.getValueAt(selectionModel.getLeadSelectionIndex(),0));
                        preparedStatement.execute();
                        model.removeRow(selectionModel.getLeadSelectionIndex());
                        System.out.println("deleted");
                    }catch (SQLException ex)
                    {
                        ex.printStackTrace();
                    }
                }else
                {
                    System.out.println("Select row to delete");
                }
            }
        });
        upbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            if (!selectionModel.isSelectionEmpty()){
                int id = Integer.parseInt(codefield.getText());
                String latin = lnamefiled.getText();
                String khmer = knamefield.getText();
                KeyValue selectedItem = (KeyValue) genderBox.getSelectedItem();
                int key = selectedItem.getKey();
                String value = selectedItem.getValue();
                String dob = mydate.getDate().toString();
                try
                {
                    String query = "Update student set studentID = ?, namelatin = ?, namekhmer = ?, genderID = ?, dateofbirth = ? where studentID = ? ORDER BY student.studentID asc";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1,id);
                    preparedStatement.setString(2,latin);
                    preparedStatement.setString(3,khmer);
                    preparedStatement.setInt(4,key);
                    preparedStatement.setString(5,dob);
                    preparedStatement.setInt(6,(int)model.getValueAt(selectionModel.getLeadSelectionIndex(),0));
                    preparedStatement.execute();
                    model.setValueAt(id,selectionModel.getLeadSelectionIndex(),0);
                    model.setValueAt(latin,selectionModel.getLeadSelectionIndex(),1);
                    model.setValueAt(khmer,selectionModel.getLeadSelectionIndex(),2);
                    model.setValueAt(value,selectionModel.getLeadSelectionIndex(),3);
                    model.setValueAt(dob,selectionModel.getLeadSelectionIndex(),4);
                }catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }else
            {
                System.out.println("Select row to update");
            }
            }
        });
    }
    public void initComboBox() {
        KeyValue[] items = {new KeyValue(0, ""), new KeyValue(1, "Male"), new KeyValue(2, "Female")};
        Iterator iterator = Arrays.stream(items).iterator();
        while (iterator.hasNext()) {
            genderBox.addItem(iterator.next());
        }
    }
    public void StartDatePicker() {
        DatePickerSettings dateSetting = new DatePickerSettings();
        dateSetting.setFormatForDatesCommonEra("yyyy-MM-dd");
        mydate = new DatePicker(dateSetting);
        datepanel.add(mydate);
        //mydate.setDate(LocalDate.now().minusYears(18));
    }
    public void initTable()
    {
        model = (DefaultTableModel) table1.getModel();
        model.addColumn("Code");
        model.addColumn("Name Latin");
        model.addColumn("Name Khmer");
        model.addColumn("Gender");
        model.addColumn("Date of Birth");
        try
        {
            String query = "select * from student inner join gender on student.genderId = gender.id ORDER BY student.studentID asc";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            ResultSet result = preparedStatement.getResultSet();
            int id;
            String lname;
            String kname;
            String gender;
            String dob;
            while(result.next())
            {
                id = result.getInt(1);
                lname = result.getString(2);
                kname = result.getString(3);
                gender = result.getString(7);
                dob = result.getString(5);
                model.addRow(new Object[]{id, lname, kname,gender,dob});
            }
        }catch(SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new StudentForm();
    }
}