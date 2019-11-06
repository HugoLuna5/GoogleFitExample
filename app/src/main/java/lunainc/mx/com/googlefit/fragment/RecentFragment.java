package lunainc.mx.com.googlefit.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;



import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.mx.com.googlefit.R;
import lunainc.mx.com.googlefit.adapter.AnimatedLayoutManager;
import lunainc.mx.com.googlefit.adapter.WorkoutListViewAdapter;
import lunainc.mx.com.googlefit.animation.ItemAnimator;
import lunainc.mx.com.googlefit.database.CacheManager;
import lunainc.mx.com.googlefit.database.DataManager;
import lunainc.mx.com.googlefit.model.Workout;

/**
 *
 * Displays historical entries in a vertical list.
 */
@SuppressWarnings("WeakerAccess") // Butterknife requires public reference of injected views
public class RecentFragment extends BaseFragment implements WorkoutListViewAdapter.OnItemClickListener {

    public static final String TAG = "RecentFragment";

    private DataManager.IDataManager dataReceiver;
    private CacheManager.ICacheManager cacheReceiver;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private WorkoutListViewAdapter adapter;

    public static RecentFragment create() {
        return new RecentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);

        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new AnimatedLayoutManager(this.getActivity(), 1));

        adapter = new WorkoutListViewAdapter(this.getActivity(), cacheReceiver.getCursor());
        adapter.setHasStableIds(true);
        adapter.setOnItemClickListener(this);

        mRecyclerView.setItemAnimator(new ItemAnimator());
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if(activity instanceof DataManager.IDataManager) {
            dataReceiver = (DataManager.IDataManager)activity;
        }
        if(activity instanceof CacheManager.ICacheManager) {
            cacheReceiver = (CacheManager.ICacheManager)activity;
        }
    }

    public void swapCursor(Cursor cursor) {
        adapter.swapCursor(cursor);
        adapter.notifyDataSetChanged();
    }

    /**
     * Clear callback on detach to prevent null reference errors after the view has been
     */
    @Override
    public void onDetach() {
        super.onDetach();
        dataReceiver = null;
        cacheReceiver = null;
    }

    @Override
    public void onItemClick(View view, Workout viewModel) {
        if (dataReceiver != null) {
            dataReceiver.removeData(viewModel);
        }
    }
}
