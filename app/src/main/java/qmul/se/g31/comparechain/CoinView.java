package qmul.se.g31.comparechain;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import qmul.se.g31.comparechain.MarketData.Coin;
import qmul.se.g31.comparechain.MarketData.Repository;
import qmul.se.g31.comparechain.MarketData.SimulatedPortfolio;
import qmul.se.g31.comparechain.ViewCoinWindow.ViewCoinWindow;

/**
 * Created by Thomas on 12/03/2018.
 */

public class CoinView extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_coins, container, false);
        Repository repo = Repository.getInstance();
        ArrayList<Coin> coin = repo.getData();

        ListView myList = (ListView) view.findViewById(R.id.coinListView);
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
}
