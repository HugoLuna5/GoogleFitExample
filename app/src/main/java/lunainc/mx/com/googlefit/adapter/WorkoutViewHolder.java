package lunainc.mx.com.googlefit.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import lunainc.mx.com.googlefit.R;

public class WorkoutViewHolder extends RecyclerView.ViewHolder {
    public final ImageView image;
    public final TextView text;
    public final TextView detail;
    public final LinearLayout container;

    public WorkoutViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.image);
        text = (TextView) itemView.findViewById(R.id.text);
        detail = (TextView) itemView.findViewById(R.id.summary_text);
        container = (LinearLayout) itemView.findViewById(R.id.container);
    }

    // TODO: Add setItem function here.
}
