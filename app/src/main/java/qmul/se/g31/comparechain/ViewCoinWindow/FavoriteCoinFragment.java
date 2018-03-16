package qmul.se.g31.comparechain.ViewCoinWindow;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 16/03/2018.
 */

public class FavoriteCoinFragment extends Fragment{

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_coin_actions, container, false);
        return view;
    }
}
