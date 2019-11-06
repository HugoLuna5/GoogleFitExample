package lunainc.mx.com.googlefit.adapter;

import android.os.Parcelable;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.HashMap;
import java.util.Map;

import lunainc.mx.com.googlefit.fragment.PageFragment;


public class TabPagerDataAdapter extends FragmentStatePagerAdapter {

    public static final String TAG = "TabPagerDataAdapter";

    private Map<Integer, PageFragment> mPageReferenceMap = new HashMap<>();

    public String filter = "";

    private static final String[] TITLES = new String[] {
            "Today",
            "Week",
            "Month",
            "Last Month",
            "Year"
    };

    public static final int NUM_TITLES = TITLES.length;

    public TabPagerDataAdapter(FragmentManager fm) {
        super(fm);
        Log.d(TAG, "INIT");
    }

    @Override
    public int getCount() {
        return NUM_TITLES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public Fragment getItem(int position) {
        PageFragment myFragment = PageFragment.create(position + 1, filter);
        mPageReferenceMap.put(position, myFragment);
        //Log.d(TAG, "PUT: " + position);
        return myFragment;
    }

    @Override
    public Parcelable saveState() {
        Log.d(TAG, "SAVE STATE");
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        Log.d(TAG, "RESTORE STATE");
    }

    public void destroy() {
        mPageReferenceMap.clear();
        Log.d(TAG, "DESTROY");
    }

    public PageFragment getFragment(int key) {
        PageFragment pf = mPageReferenceMap.get(key);
        if (pf != null) {
            pf.setFilterText(filter);
        }
        return pf;
    }
}
