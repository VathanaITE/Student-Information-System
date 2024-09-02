package cls;

public class User {
    private static String username;
    private static int roleid;

    public static void setUserName(String user_name){
        username = user_name;
    }
    public static String getUserName(){
        return username;
    }
    public static void setRoleId(int role_id){
        roleid = role_id;
    }
    public static int getRoleId(){
        return roleid;
    }
}
