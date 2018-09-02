package models;
/**
 * @author sakthivel
 */
public class UserMenu {
    public int Menu_gid;
    public int Menu_Parent_gid;
    public String Menu_Name;
    public String Menu_Link;
    public int Menu_Display_Order;
    public int Menu_Level;
    public int Entity_gid;
    private int menu_Parent_gid;
    private String menu_Name;

    public void setMenuGID(int menuGID) {
        int menuGID1 = menuGID;
    }

    public void setMenu_Parent_gid(int menu_Parent_gid) {
        this.menu_Parent_gid = menu_Parent_gid;
    }

    public void setMenu_Name(String menu_Name) {
        this.menu_Name = menu_Name;
    }

    public void setMenu_Link(String menu_Link) {
        String menu_Link1 = menu_Link;
    }

    public void setMenu_Display_Order(int menu_Display_Order) {
        int menu_Display_Order1 = menu_Display_Order;
    }

    public void setMenu_Level(int menu_Level) {
        int menu_Level1 = menu_Level;
    }

    public int getMenu_Parent_gid() {
        return menu_Parent_gid;
    }

    public String getMenu_Name() {
        return menu_Name;
    }
}
