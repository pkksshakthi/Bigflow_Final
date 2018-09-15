package constant;

import java.util.List;

import models.UserMenu;
import models.Variables;

public class Constant {
    public static final String DATABASENAME = "VSOLV";
    public static final int DATABASE_VERSION = 1;
    public static String IP_ADDRESS = "174.138.120.196";
    public static String HOST_NAME = "bigflowdemo";
    public static String URL = "https://" + IP_ADDRESS + "/" + HOST_NAME + "/";
    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    public static List<Variables.Menulist> parentMenus;




    //table_columnName
    public static String menu_gid="menu_gid";
    public static String menu_parent_gid="menu_parent_gid";
    public static String menu_name="menu_name";
    public static String menu_link="menu_link";
    public static String menu_displayorder="menu_displayorder";
    public static String menu_level="menu_level";

    public static String latitude="latlong_lat";
    public static String longitude ="latlong_long";
    public static String latlong_emp_gid = "latlong_emp_gid";
    public static String latlong_date = "latlong_date";
    public static String latlong_issync = "latlong_issync";

}
