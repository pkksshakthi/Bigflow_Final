package models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.vsolv.bigflow.R;

import java.util.List;


public class ScheduleForAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    Context _Context;
    int _resource;
    List<Object> _ScheduleTypeList;
    final int isHeader = 0;
    final int isItems = 1;

    public ScheduleForAdapter(@NonNull Context context, int resource, List<Object> scheduleTypeList) {
        this._Context = context;
        inflater = LayoutInflater.from(_Context);
        this._resource = resource;
        this._ScheduleTypeList = scheduleTypeList;
    }


    @Override
    public int getItemViewType(int position) {
        if (_ScheduleTypeList.get(position) instanceof Variables.ScheduleType) {
            return isItems;
        } else {
            return isHeader;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return _ScheduleTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return _ScheduleTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            switch (getItemViewType(position)) {
                case isHeader:
                    view = inflater.inflate(R.layout.list_header, null);
                    break;
                case isItems:
                    view = inflater.inflate(_resource, null);
                    break;
            }
        }
        switch (getItemViewType(position)) {
            case isHeader:
                TextView headerText = view.findViewById(R.id.list_header);
                headerText.setText(_ScheduleTypeList.get(position).toString());
                break;
            case isItems:
                TextView scheduletype_name = view.findViewById(R.id.item_name);
                Variables.ScheduleType scheduleType = (Variables.ScheduleType) _ScheduleTypeList.get(position);
                scheduletype_name.setText(scheduleType.getSchedule_type_name());
                break;
        }


        return view;
    }
}
