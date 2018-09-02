package DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import constant.Constant;
import models.UserMenu;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static DataBaseHandler dataBaseHandler;

    public DataBaseHandler(Context context) {

        super(context, Constant.DATABASENAME, null, Constant.DATABASE_VERSION);
    }

    //, String name, SQLiteDatabase.CursorFactory factory, int version ------ Commented bcz not used - Doubt
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE gal_mst_tmenu (" +
                "  menu_gid int  PRIMARY KEY," +
                "  menu_parent_gid integer NOT NULL," +
                "  menu_name varchar(64) NOT NULL," +
                "  menu_link varchar(128) DEFAULT NULL," +
                "  menu_displayorder integer NOT NULL DEFAULT '0'," +
                "  menu_level integer NOT NULL DEFAULT '0'" +
                ") ";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void Insert(String TableName, ContentValues contentValues) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.insert(TableName, null, contentValues);
        sqLiteDatabase.close();

    }

    public List<UserMenu> Read_Menu() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from gal_mst_tmenu where NOT menu_parent_gid=0", null);
        List<UserMenu> list = new ArrayList<UserMenu>();

        if (cursor.moveToFirst()) {
            do {
                UserMenu userMenu = new UserMenu();
                //  userMenu.setMenuGID(cursor.getInt(cursor.getColumnIndex("menu_gid")));
                userMenu.setMenu_Parent_gid(cursor.getInt(cursor.getColumnIndex("menu_parent_gid")));
                userMenu.setMenu_Name(cursor.getString(cursor.getColumnIndex("menu_name")));
               /* userMenu.setMenu_Link(cursor.getString(cursor.getColumnIndex("menu_link")));
                userMenu.setMenu_Display_Order(cursor.getInt(cursor.getColumnIndex("menu_displayorder")));
                userMenu.setMenu_Level(cursor.getInt(cursor.getColumnIndex("menu_level")));*/


                Log.v("userMenu", "" + userMenu.Menu_gid);
                list.add(userMenu);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();

        return list;
    }

    public static List<String> getSubMenus(int menu_name, Context context) {
        if (dataBaseHandler == null) {
            dataBaseHandler = new DataBaseHandler(context);
        }
        ArrayList<String> strings = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = dataBaseHandler.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select menu_name from gal_mst_tmenu where menu_parent_gid=" + menu_name + "", null);

        if (cursor.moveToFirst()) {
            do {
                strings.add(cursor.getString(cursor.getColumnIndex("menu_name")));
            } while (cursor.moveToNext());
        }
        cursor.close();


        return strings;
    }
}
