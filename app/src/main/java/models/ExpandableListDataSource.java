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
 * Created by sakthivel
 */
public class ExpandableListDataSource {


    public static Map<String, List<String>> getData(Context context) {

        DataBaseHandler dataBaseHandler = new DataBaseHandler(context);
        Constant.parentMenus = dataBaseHandler.Read_Menu();
        Map<String, List<String>> expandableListData = new TreeMap<>();
        for (int i = 0; i < Constant.parentMenus.size(); i++) {
            expandableListData.put(Constant.parentMenus.get(i).getMenu_Name(), DataBaseHandler.getSubMenus(Constant.parentMenus.get(i).getMenu_Parent_gid(), context));
        }
        return expandableListData;
    }


}
