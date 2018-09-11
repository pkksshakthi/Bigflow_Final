package models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.vsolv.bigflow.R;

import java.util.List;


public class ScheduleForAdapter extends ArrayAdapter {
    Context _Context;
    int _resource;
    List<Variables.ScheduleType> _ScheduleTypeList;

    public ScheduleForAdapter(@NonNull Context context, int resource, List<Variables.ScheduleType> scheduleTypeList) {
        super(context, resource, scheduleTypeList);
        this._Context = context;
        this._resource = resource;
        this._ScheduleTypeList = scheduleTypeList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(_Context);
        View view = inflater.inflate(_resource, null);
        TextView scheduletype_name = view.findViewById(R.id.item_name);
        Variables.ScheduleType scheduleType = _ScheduleTypeList.get(position);
        scheduletype_name.setText(scheduleType.getSchedule_type_name());
        return view;
    }
}
