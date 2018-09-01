package DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import constant.Constant;
import models.Variables;

public class DataBaseHandler extends SQLiteOpenHelper{
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

    public void Insert(String TableName, ContentValues contentValues){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.insert(TableName,null,contentValues);
        sqLiteDatabase.close();

    }

    public List<Variables> Read_Menu() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from gal_mst_tmenu", null);
        List<Variables> list = new ArrayList<Variables>();

        if (cursor.moveToFirst()) {
            do {
                Variables variables = new Variables();

                variables.Menu_gid = cursor.getInt(cursor.getColumnIndex("menu_gid"));
                variables.Menu_Parent_gid = cursor.getInt(cursor.getColumnIndex("menu_parent_gid"));
                variables.Menu_Name = cursor.getString(cursor.getColumnIndex("menu_name"));
                variables.Menu_Link = cursor.getString(cursor.getColumnIndex("menu_link"));
                variables.Menu_Display_Order = cursor.getInt(cursor.getColumnIndex("menu_displayorder"));
                variables.Menu_Level = cursor.getInt(cursor.getColumnIndex("menu_level"));

                list.add(variables);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


}
