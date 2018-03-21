package qmul.se.g31.comparechain.GUIClasses;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.Repository;
import qmul.se.g31.comparechain.GUIClasses.RowAdapters.CoinRowAdapter;
import qmul.se.g31.comparechain.R;
import qmul.se.g31.comparechain.GUIClasses.ViewCoinWindow.ViewCoinWindow;

/**
 * Created by Thomas on 12/03/2018.
 */

public class CoinView extends Fragment {

    private View view;
    private ListView myList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_coins, container, false);
        Repository repo = Repository.getInstance();
        ArrayList<Coin> coin = repo.getData();

        searchBar();

        this.myList = (ListView) view.findViewById(R.id.coinListView);
        ListAdapter myAdapter = new CoinRowAdapter(myList.getContext(), coin);
        myList.setAdapter(myAdapter);

        myList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Coin c = (Coin)adapterView.getItemAtPosition(i);
                        Intent intent = new Intent(getActivity(), ViewCoinWindow.class);
                        intent.putExtra("coin", (Serializable) c);
                        startActivity(intent);
                    }
                }
        );
        return view;
    }

    public void searchBar(){
        final SearchView searchView = (SearchView) view.findViewById(R.id.searchBar);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.BLACK);
        id = searchView.getContext().getResources().getIdentifier("android:id/search_button", null, null);
        ImageView imgView = (ImageView) searchView.findViewById(id);
        Drawable whiteIcon = imgView.getDrawable();
        whiteIcon.setTint(Color.GRAY);
        imgView.setImageDrawable(whiteIcon);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Coin> tempList = new ArrayList<Coin>();
                ArrayList<Coin> coin = new ArrayList<Coin>();

                Repository repo = Repository.getInstance();
                ArrayList<Coin> coins = repo.getData();
                for(int i = 0; i < coins.size(); i++){
                    coin.add(coins.get(i));
                }

                for(Coin temp : coin){
                    if(temp.getName().toLowerCase().contains(s.toLowerCase()) || temp.getSymbol().toUpperCase().contains(s.toUpperCase())){
                        tempList.add(temp);
                    }
                }
                ListAdapter myAdapter = new CoinRowAdapter(getContext(), tempList);
                myList.setAdapter(myAdapter);

                return true;
            }

        });
    }
}
