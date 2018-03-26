package qmul.se.g31.comparechain.GUIClasses;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.GUIClasses.RowAdapters.FavoriteRowAdapter;
import qmul.se.g31.comparechain.DataClasses.Favorites;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 12/03/2018.
 */

public class FavoritesView extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_favorites, container, false);
        hideKeyboard(getActivity());


        Favorites fav = Favorites.getInstance();
        ArrayList<Coin> favorites = fav.getFavorites();

        ListView myList = (ListView) view.findViewById(R.id.favoritesListView);
        ListAdapter myAdapter = new FavoriteRowAdapter(getContext(), R.layout.favorite_list_row, favorites);
        myList.setAdapter(myAdapter);

        return view;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
