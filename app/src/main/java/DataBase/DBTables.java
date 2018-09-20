package DataBase;

public class DBTables {

    public static final String CREATE_TABLE_Menu = "CREATE TABLE gal_mst_tmenu ( "+
                            "  menu_gid int  NOT NULL,"  +
                            "  menu_parent_gid integer NOT NULL,"  +
                            "  menu_name varchar(64) NOT NULL,"  +
                            "  menu_link varchar(128) DEFAULT NULL,"  +
                            "  menu_displayorder integer NOT NULL DEFAULT '0',"  +
                            "  menu_level integer NOT NULL DEFAULT '0'"  +
                            ") ";

    public static final String CREATE_TABLE_LatLong = "CREATE TABLE fet_trn_tlatlong ( "+
                            "  latlong_gid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "  latlong_lat double NOT NULL, " +
                            "  latlong_long double NOT NULL, " +
                            "  latlong_emp_gid INT NOT NULL, " +
                            "  latlong_date DATETIME NOT NULL, " +
                            "  latlong_issync varchar(1) NOT NULL DEFAULT 'N', " +
                            "  entity_gid INT NOT NULL " +
                            ") ";

}
