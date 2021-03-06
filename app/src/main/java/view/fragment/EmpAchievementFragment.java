package view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vsolv.bigflow.R;

import java.util.ArrayList;

import models.TimeLineAdapter;
import models.Variables;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmpAchievementFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_Title = "title";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String Title;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ArrayList<Variables.Timeline> timelines;

    public EmpAchievementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param Title  Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSchedule.
     */
    // TODO: Rename and change types and number of parameters
    public static EmpAchievementFragment newInstance(String Title, String param2) {
        EmpAchievementFragment fragment = new EmpAchievementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_Title, Title);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Title = getArguments().getString(ARG_Title);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Sales Details");
        View view = inflater.inflate(R.layout.fragment_emp_achievement, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_timeLine);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        setDataListItems();
        TimeLineAdapter mTimeLineAdapter = new TimeLineAdapter(getActivity(), timelines);
        recyclerView.setAdapter(mTimeLineAdapter);
        return view;

    }

    private void setDataListItems() {
        timelines = new ArrayList<>();
        Variables.Timeline timeline = new Variables.Timeline();
        timeline.title = "Item successfully delivereddsfgsdfsdfsd edgdsgf sd";
        timeline.subtitle = "sub delivered";
        timeline.status = Variables.Timeline.Status.ACTIVE;
        timelines.add(timeline);
        timeline = new Variables.Timeline();
        timeline.title = "Item successfully delivered";
        timeline.subtitle = "sub delivered";
        timeline.status = Variables.Timeline.Status.INACTIVE;
        timelines.add(timeline);
        timeline = new Variables.Timeline();
        timeline.title = "Item successfully delivered";
        timeline.subtitle = "sub delivered";
        timeline.status = Variables.Timeline.Status.REJECTED;
        timelines.add(timeline);
        timeline = new Variables.Timeline();
        timeline.title = "Item successfully delivered";
        timeline.subtitle = "sub delivered";
        timeline.status = Variables.Timeline.Status.COMPLETED;
        timelines.add(timeline);
        timeline = new Variables.Timeline();
        timeline.title = "Item successfully delivered";
        timeline.subtitle = "sub delivered";
        timeline.status = Variables.Timeline.Status.ACTIVE;
        timelines.add(timeline);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
