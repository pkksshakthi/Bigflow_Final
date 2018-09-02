package models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import DataBase.DataBaseHandler;
import constant.Constant;

/**
 * Created by msahakyan on 22/10/15.
 */
public class ExpandableListDataSource {

    private static Context context1;

    /**
     * Returns fake data of films
     *
     * @param context
     * @return
     */
    public static Map<String, List<String>> getData(Context context) {
        context1 = context;
        DataBaseHandler dataBaseHandler = new DataBaseHandler(context);

        Constant.parentMenus = dataBaseHandler.Read_Menu();


        Map<String, List<String>> expandableListData = new TreeMap<>();

        for (int i = 0; i < Constant.parentMenus.size(); i++) {
            String name = Constant.parentMenus.get(i).getMenu_Name();
            int id = Constant.parentMenus.get(i).getMenu_Parent_gid();
            Log.v("name" + id, name);

            expandableListData.put(Constant.parentMenus.get(i).getMenu_Name(), getSubMenus(id));

        }
        return expandableListData;
    }

    private static List<String> getSubMenus(int menu_name) {
        ArrayList<String> strings = new ArrayList<>();
        if (context1 != null) {
            DataBaseHandler dataBaseHandler = new DataBaseHandler(context1);
            SQLiteDatabase sqLiteOpenHelper = dataBaseHandler.getReadableDatabase();
            Cursor cursor = sqLiteOpenHelper.rawQuery("select menu_name from gal_mst_tmenu where menu_parent_gid=" + menu_name + "", null);

            if (cursor.moveToFirst()) {
                do {
                    strings.add(cursor.getString(cursor.getColumnIndex("menu_name")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return strings;
    }
}
