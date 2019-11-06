package lunainc.mx.com.googlefit.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;


import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.mx.com.googlefit.R;
import lunainc.mx.com.googlefit.Utilities;
import lunainc.mx.com.googlefit.database.CacheManager;
import lunainc.mx.com.googlefit.database.DataManager;
import lunainc.mx.com.googlefit.database.SimpleDBHelper;
import lunainc.mx.com.googlefit.fragment.RecentFragment;
import lunainc.mx.com.googlefit.model.Workout;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


public class RecentActivity extends BaseActivity implements DataManager.IDataManager, CacheManager.ICacheManager {
    private static final String TAG = "RecentActivity";
    private RecentFragment fragment;
    private static final String ARG_ACTIVITY_TYPE = "ARG_ACTIVITY_TYPE";
    private DataManager mDataManager;
    private Cursor mCursor;
    private Workout lastWorkout;

    @BindView(R.id.container) View container;

    /**
     * Used to start the activity using a custom animation.
     *
     * @param activity Reference to the Android Activity we are animating from
     */
    public static void launch(BaseActivity activity) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.enter_anim, R.anim.no_anim);
        Intent intent = new Intent(activity, RecentActivity.class);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    ///////////////////////////////////////
    // LIFE CYCLE
    ///////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Recent History");
        }
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_white, null));
        mDataManager = DataManager.getInstance(this);
        SQLiteDatabase mDb = SimpleDBHelper.INSTANCE.open(this.getApplicationContext());
        fragment = RecentFragment.create();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.placeholder, fragment, RecentFragment.TAG);
        transaction.commit();

        mCursor = cupboard().withDatabase(mDb).query(Workout.class).withSelection("type != ? AND type != ?", "3", "-2").orderBy("start DESC").limit(200).query().getCursor();

    }

    @Override
    public void onStart() {
        super.onStart();
        mDataManager.addListener(this);
        mDataManager.setContext(this);
        mDataManager.connect();
    }

    @Override
    protected void onStop() {
        mDataManager.removeListener(this);
        mDataManager.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mCursor.close();
        SimpleDBHelper.INSTANCE.close();
        mDataManager.disconnect();
        super.onDestroy();
    }

    @Override
    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_recent;
    }

    ///////////////////////////////////////
    // OPTIONS MENU
    ///////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                ActivityCompat.finishAfterTransition(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ///////////////////////////////////////
    // CALLBACKS
    ///////////////////////////////////////

    @Override
    public void insertData(Workout workout) {

    }

    @Override
    public void removeData(Workout workout) {
        lastWorkout = workout;
        Snackbar.make(container, "Removed entry", Snackbar.LENGTH_LONG).setAction("UNDO", clickListener).show();
        Log.d(TAG, "Removed: " + workout.toString());
        mDataManager.deleteWorkout(workout);
        onDataChanged(Utilities.TimeFrame.ALL_TIME);
    }

    @Override
    public void onConnected() {

    }

    final View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View v) {
            mDataManager.insertData(lastWorkout);
        }
    };

    @Override
    public void onDataChanged(Utilities.TimeFrame timeFrame) {
        final SQLiteDatabase mDb = SimpleDBHelper.INSTANCE.open(this.getApplicationContext());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDb != null) {
                    mCursor = cupboard().withDatabase(mDb).query(Workout.class).withSelection("type != ? AND type != ?", "3", "-2").orderBy("start DESC").limit(200).query().getCursor();
                    fragment.swapCursor(mCursor);
                    Log.d(TAG, "Refresh cursor");
                }
            }
        });
    }

    @Override
    public void onDataComplete() {}
}
