package lunainc.mx.com.googlefit.activity;

import android.view.View;

import lunainc.mx.com.googlefit.model.Workout;


public interface IMainActivityCallback {
    void launch(View transitionView, Workout workout);
    void quickDataRead();
    void setStepCounting(boolean active);
    void setActivityTracking(boolean active);
}
