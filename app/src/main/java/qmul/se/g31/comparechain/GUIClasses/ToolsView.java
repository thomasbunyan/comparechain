package qmul.se.g31.comparechain.GUIClasses;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 12/03/2018.
 */

public class ToolsView extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_tools, container, false);
        return view;
    }



}
