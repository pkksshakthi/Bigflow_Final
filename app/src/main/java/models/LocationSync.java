package models;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import DataBase.DataBaseHandler;

public class LocationSync extends Activity {

    public  static String LatLongSet(Context context){
        try{

            //To Get the Values from Local SQLIte

            DataBaseHandler dataBaseHandler = new DataBaseHandler(context);
            List<Variables.Location> locations = new ArrayList<>();
            locations = dataBaseHandler.getLatLong(context);

            if (!locations.isEmpty()){
                for (int i = 0;i<locations.size();i++){
                   String s =   locations.get(i).toString();

                }
            }



        }catch (Exception e){
            return e.getMessage();
        }
        return "SUCCESS";
    }

}
