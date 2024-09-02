package cls;

import db.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentCode {
    private static final String prefix = "STU";
    private static final int nbDigit = 6;

    private static String formatNumber(int nbDigits , int number){
        String formatString = "%0"+nbDigits+"d";
        return String.format(formatString,number);
    }
    private static String getStudentCode(){
        Connection  con = DBConnection.getConnection();
        String code = null;
        //get latest code from db
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT CODE")
                    .append("FORM")
                    .append("STUDENT")
                    .append("ORDER BY ID DESC LIMIT 1");
            Statement statement =con.createStatement();
            ResultSet resultSet = statement.executeQuery(query.toString());
            if (resultSet.next()){
                code = resultSet.getString("Code");
            }
            con.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        finally {
            return code;
        }
    }
    public static String generateStudentCode(){
        String newNumberPart = "";
        try {
            String oldCode = getStudentCode();
            if (oldCode!=null){
                String numberPart = oldCode.substring(prefix.length());
                int numberVale = Integer.parseInt(numberPart);
                numberVale ++;
                newNumberPart = formatNumber(nbDigit,numberVale);
            }
            else {
                newNumberPart = formatNumber(nbDigit,1);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            return prefix.concat(newNumberPart);
        }

    }
}
