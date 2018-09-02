package models;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.vsolv.bigflow.BuildConfig;
import com.vsolv.bigflow.R;

import presenter.NavigationManager;
import view.activity.DashBoardActivity;
import view.fragment.FragmentACtion;


/**
 * @author sakthivel
 */

public class FragmentNavigationManager implements NavigationManager {

    private static FragmentNavigationManager sInstance;

    private FragmentManager mFragmentManager;

    public static FragmentNavigationManager obtain(DashBoardActivity activity) {
        if (sInstance == null) {
            sInstance = new FragmentNavigationManager();
        }
        sInstance.configure(activity);
        return sInstance;
    }

    private void configure(DashBoardActivity activity) {
        DashBoardActivity mActivity = activity;
        mFragmentManager = mActivity.getSupportFragmentManager();
    }

    @Override
    public void showFragmentAction(String title) {
        showFragment(FragmentACtion.newInstance(title), false);
    }

    private void showFragment(Fragment fragment, boolean allowStateLoss) {
        FragmentManager fm = mFragmentManager;

        @SuppressLint("CommitTransaction")
        FragmentTransaction ft = fm.beginTransaction()
            .replace(R.id.container, fragment);

        ft.addToBackStack(null);

        if (allowStateLoss || !BuildConfig.DEBUG) {
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }

        fm.executePendingTransactions();
    }
}
