package models;

public class UserDetails {
    private static String user_id;
    private static String user_name;
    private static String user_type;
    private static String user_code;
    private static String today_date;
    private static String entity_gid;
    public static String menu_gid;
    public static String menu_parent_gid;

    public static String getMenu_name() {
        return menu_name;
    }
    public static String menu_name;
    public static String menu_link;
    public static String menu_displayorder;
    public static String menu_level;


    public static void setUser_id(String user_id) {
        UserDetails.user_id = user_id;
    }

    public static void setUser_name(String user_name) {
        UserDetails.user_name = user_name;
    }

    public static void setUser_type(String user_type) {
        UserDetails.user_type = user_type;
    }

    public static void setUser_code(String user_code) {
        UserDetails.user_code = user_code;
    }

    public static void setToday_date(String today_date) {
        UserDetails.today_date = today_date;
    }

    public static void setEntity_gid(String entity_gid) {
        UserDetails.entity_gid = entity_gid;
    }


    public static String getUser_id() {
        return user_id;
    }

    public static String getUser_name() {
        return user_name;
    }

    public static String getUser_type() {
        return user_type;
    }

    public static String getUser_code() {
        return user_code;
    }

    public static String getToday_date() {
        return today_date;
    }

    public static String getEntity_gid() {
        return entity_gid;
    }


    public static void setmenu_gid(String menu_gid) {
        UserDetails.menu_gid = menu_gid;
    }

    public static void setmenu_parent_gid(String menu_parent_gid) {
        UserDetails.menu_parent_gid = menu_parent_gid;
    }

    public static void setmenu_name(String menu_name) {
        UserDetails.menu_name = menu_name;
    }

    public static void setmenu_link(String menu_link) {
        UserDetails.menu_link = menu_link;
    }

    public static void setmenu_displayorder(String menu_displayorder) {
        UserDetails.menu_displayorder = menu_displayorder;
    }

    public static void setmenu_level(String menu_level) {
        UserDetails.menu_level = menu_level;
    }
}
