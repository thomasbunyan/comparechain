package qmul.se.g31.comparechain;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import qmul.se.g31.comparechain.MarketData.Coin;
import qmul.se.g31.comparechain.MarketData.FavoriteRowAdapter;
import qmul.se.g31.comparechain.MarketData.Favorites;

/**
 * Created by Thomas on 12/03/2018.
 */

public class FavoritesView extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_favorites, container, false);

        Favorites fav = Favorites.getInstance();
        ArrayList<Coin> favorites = fav.getFavorites();

        ListView myList = (ListView) view.findViewById(R.id.favoritesListView);
        ListAdapter myAdapter = new FavoriteRowAdapter(getContext(), R.layout.favorite_list_row, favorites);
        myList.setAdapter(myAdapter);

        return view;
    }
}
