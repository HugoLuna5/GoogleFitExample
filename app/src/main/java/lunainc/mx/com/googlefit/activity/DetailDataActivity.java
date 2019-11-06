package lunainc.mx.com.googlefit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;


import butterknife.BindView;
import butterknife.ButterKnife;
import lunainc.mx.com.googlefit.R;
import lunainc.mx.com.googlefit.Utilities;
import lunainc.mx.com.googlefit.database.CacheManager;
import lunainc.mx.com.googlefit.fragment.ReportsFragment;
import lunainc.mx.com.googlefit.model.SummaryData;
import lunainc.mx.com.googlefit.model.Workout;
import lunainc.mx.com.googlefit.model.WorkoutTypes;
import lunainc.mx.com.googlefit.service.CacheResultReceiver;


@SuppressWarnings("WeakerAccess") // Butterknife requires public reference of injected views
public class DetailDataActivity extends BaseActivity implements CacheResultReceiver.Receiver {

    private static final String EXTRA_TYPE = "DetailDataActivity:type";
    private static final String EXTRA_TITLE = "DetailDataActivity:title";
    private static final String EXTRA_IMAGE = "DetailDataActivity:image";

    @BindView(R.id.spinner) Spinner navigationSpinner;
    @BindView(R.id.average_text)
    TextView averageText;

    @BindView(R.id.current_text)
    TextView currentText;

    private CacheResultReceiver mReciever;
    private ReportsFragment mReportsFragment;

    /**
     * Used to start the activity using a custom animation.
     *
     * @param activity Reference to the Android Activity we are animating from
     * @param transitionView Target view used in the scene transition animation
     * @param workout Type of workout the DetailDataActivity should load
     */
    public static void launch(BaseActivity activity, View transitionView, Workout workout) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, EXTRA_IMAGE);
        Intent intent = new Intent(activity, DetailDataActivity.class);
        intent.putExtra(EXTRA_IMAGE, WorkoutTypes.getImageById(workout.type));
        intent.putExtra(EXTRA_TITLE, WorkoutTypes.getWorkOutTextById(workout.type));
        intent.putExtra(EXTRA_TYPE, workout.type);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    ///////////////////////////////////////
    // LIFE CYCLE
    ///////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);


        ButterKnife.bind(this);
        mReciever = new CacheResultReceiver(new Handler());
        ImageView image = (ImageView) findViewById(R.id.image);
        ViewCompat.setTransitionName(image, EXTRA_IMAGE);
        image.setImageResource(getIntent().getIntExtra(EXTRA_IMAGE, R.drawable.heart_icon));
        int vibrant = ContextCompat.getColor(this, WorkoutTypes.getColorById(getIntent().getIntExtra(EXTRA_TYPE, -1)));
        /*
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        Palette palette = Palette.from(bitmap).generate();
        Palette.Swatch swatch = palette.getLightVibrantSwatch();

        /*
        int vibrant = 0xFF110000;
        if (swatch != null) {
            vibrant = swatch.getRgb();//palette.getVibrantColor(0xFF110000);
        }
        if(vibrant == 0xFF110000) {
            swatch = palette.getVibrantSwatch();
            //vibrant = palette.getLightMutedColor(0x000000);
            if (swatch != null) {
                vibrant = swatch.getRgb();//palette.getVibrantColor(0xFF110000);
            }
        }
        */
        image.setBackgroundColor(Utilities.lighter(vibrant, 0.4f));

        View container = findViewById(R.id.container);
        container.setBackgroundColor(vibrant);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_TITLE));
        }

        toolbar.setBackgroundColor(vibrant);

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.graph_types, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        navigationSpinner.setAdapter(spinnerAdapter);

        navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateReport();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        getWindow().setStatusBarColor(vibrant);


        // Report fragment used to display charts and graphs
        FragmentManager fragmentManager = getSupportFragmentManager();
        mReportsFragment = ReportsFragment.newInstance(getIntent().getIntExtra(EXTRA_TYPE, 0), 1);
        fragmentManager.beginTransaction()
                .replace(R.id.chart_container, mReportsFragment)
                .commit();


    }

    @Override
    public void onResume() {
        super.onResume();
        mReciever.setReceiver(this);
        CacheManager.getSummary(-2, mReciever, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mReciever.setReceiver(null);
    }

    private void updateReport() {
        int selectedIndex = navigationSpinner.getSelectedItemPosition();
        switch (selectedIndex) {
            case 0:
                mReportsFragment.setGroupCount(1);
                break;
            case 1:
                mReportsFragment.setGroupCount(7);
                break;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail_data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    ///////////////////////////////////////
    // OPTIONS MENU
    ///////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                // Reverse the animation back to the previous view.
                finishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ///////////////////////////////////////
    // CALLBACKS
    ///////////////////////////////////////

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        final SummaryData data = resultData.getParcelable("workoutSummary");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO: Work in progress
                //averageText.setText("Average steps per day: " + data.averageDailyData);
                //currentText.setText("Steps today: " + data.todayData);
            }
        });
    }
}