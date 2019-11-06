package lunainc.mx.com.googlefit.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.appcompat.widget.Toolbar;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import lunainc.mx.com.googlefit.R;
import lunainc.mx.com.googlefit.activity.IMainActivityCallback;
import lunainc.mx.com.googlefit.model.UserPreferences;


@SuppressWarnings({"WeakerAccess", "unused"}) // Butterknife requires public reference of injected views
public class SettingsFragment extends BaseFragment {

    private IMainActivityCallback mCallback;
    private boolean viewLoaded = false;

    @BindView(R.id.step_toggle)
    ToggleButton step_toggle;

    @BindView(R.id.activity_toggle)
    ToggleButton activity_toggle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewLoaded = false;
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_close_white, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        step_toggle.setChecked(UserPreferences.getCountSteps(getActivity()));
        activity_toggle.setChecked(UserPreferences.getActivityTracking(getActivity()));
        viewLoaded = true;

        return view;
    }

    @OnCheckedChanged(R.id.step_toggle)
    public void onStepCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(viewLoaded && mCallback != null) {
            mCallback.setStepCounting(isChecked);
        }
    }

    @OnCheckedChanged(R.id.activity_toggle)
    public void onActivityCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(viewLoaded && mCallback != null) {
            mCallback.setActivityTracking(isChecked);
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if(activity instanceof IMainActivityCallback) {
            mCallback = (IMainActivityCallback)activity;
        }
    }

    /**
     * Clear callback on detach to prevent null reference errors after the view has been
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
}
