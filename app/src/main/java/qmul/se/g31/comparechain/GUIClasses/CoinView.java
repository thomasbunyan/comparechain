package qmul.se.g31.comparechain.GUIClasses;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.ComparisonComparator;
import qmul.se.g31.comparechain.DataClasses.ComparisonType;
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
    private boolean coinClicked = false;
    private boolean priceClicked = false;
    private boolean percentClicked = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_coins, container, false);
        final Repository repo = Repository.getInstance();
        ArrayList<Coin> coin = repo.getData(new ComparisonComparator(ComparisonType.RANK, false));

        coinClicked = false;
        priceClicked = false;
        percentClicked = false;

        searchBar();

        TextView coinHeader = (TextView) view.findViewById(R.id.coinHeader);
        TextView priceHeader = (TextView) view.findViewById(R.id.priceHeader);
        TextView changeHeader = (TextView) view.findViewById(R.id.changeHeader);
        this.myList = (ListView) view.findViewById(R.id.coinListView);
        ListAdapter myAdapter = new CoinRowAdapter(myList.getContext(), coin);
        myList.setAdapter(myAdapter);

        coinHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Coin> coin;
                if(!coinClicked) {
                    coinClicked = true;
                    coin = repo.getData(new ComparisonComparator(ComparisonType.RANK, false));
                } else{
                    coinClicked = false;
                    coin = repo.getData(new ComparisonComparator(ComparisonType.RANK, true));
                }
                ListAdapter myAdapter = new CoinRowAdapter(myList.getContext(), coin);
                myList.setAdapter(myAdapter);
            }
        });
        priceHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Coin> coin;
                if(!priceClicked) {
                    priceClicked = true;
                    coin = repo.getData(new ComparisonComparator(ComparisonType.PRICE, false));
                } else{
                    priceClicked = false;
                    coin = repo.getData(new ComparisonComparator(ComparisonType.PRICE, true));
                }
                ListAdapter myAdapter = new CoinRowAdapter(myList.getContext(), coin);
                myList.setAdapter(myAdapter);
            }
        });
        changeHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Coin> coin;
                if(!percentClicked) {
                    percentClicked = true;
                    coin = repo.getData(new ComparisonComparator(ComparisonType.PERCENT24H, false));
                } else{
                    percentClicked = false;
                    coin = repo.getData(new ComparisonComparator(ComparisonType.PERCENT24H, true));
                }
                ListAdapter myAdapter = new CoinRowAdapter(myList.getContext(), coin);
                myList.setAdapter(myAdapter);
            }
        });

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArrayList<Coin> coin = repo.getData(new ComparisonComparator(ComparisonType.RANK, false));
                ListAdapter myAdapter = new CoinRowAdapter(myList.getContext(), coin);
                myList.setAdapter(myAdapter);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl.setRefreshing(false);
                    }
                }, 300);
            }
        });

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
        myList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (myList == null || myList.getChildCount() == 0) ?
                                0 : myList.getChildAt(0).getTop();
                srl.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
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
