package lunainc.mx.com.googlefit.fragment;


import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

/**
 *
 * Contains functionality common to all Fragments. Code here should be kept to the bare
 * minimum.
 */
public abstract class BaseFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Release the views injects by butterknife
        ButterKnife.bind(getView());
    }


}