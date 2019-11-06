package lunainc.mx.com.googlefit.adapter;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;


public class AnimatedLayoutManager extends GridLayoutManager {


    public AnimatedLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }
}
