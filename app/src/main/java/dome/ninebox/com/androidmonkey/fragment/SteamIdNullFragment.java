package dome.ninebox.com.androidmonkey.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dome.ninebox.com.androidmonkey.R;
import dome.ninebox.com.androidmonkey.provider.MatchDetailsProvider;

/**
 * Created by Administrator on 2016/11/3.
 */

public class SteamIdNullFragment extends Fragment {

    @BindView(R.id.login_user)
    ImageButton loginUser;
    @BindView(R.id.login_steam)
    TextView loginSteam;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String sortBy = "start_time DESC limit 4 offset 0";
        Cursor c = getActivity().getContentResolver().query(MatchDetailsProvider.URI_DOTA_ALL, null, null, null, sortBy);

        mView = inflater.inflate(R.layout.frag_main_null, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
