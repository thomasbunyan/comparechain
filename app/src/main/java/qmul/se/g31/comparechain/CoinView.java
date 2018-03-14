package qmul.se.g31.comparechain;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import qmul.se.g31.comparechain.MarketData.Coin;
import qmul.se.g31.comparechain.MarketData.Repository;

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

        // Testing data.
        repo.updateCoin(new Coin("BTC", "Bitcoin", 30123.23, 34304300, 15844176, 1, 0.04, -0.3, -0.57));
        repo.updateCoin(new Coin("TOM", "Thomas", 2352.53, 43535345, 454252, 1, 0.02, -0.3, -0.57));
        repo.updateCoin(new Coin("ETH", "Etherium", 5435.03, 4300, 346646, 1, -0.05, -0.3, -0.57));
        repo.updateCoin(new Coin("OAG", "Anothercoin", 234.34, 3453, 7565, 1, 0.13, -0.3, -0.57));
        repo.updateCoin(new Coin("ARS", "Arsenalcoin", 2.34, 67, 37337, 1, -0.27, -0.3, -0.57));

        ArrayList<Coin> coin = repo.getData();

        //String[] coins = {"alpha", "beta", "charlie", "delta", "echo", "foxtrot", "gecho", "alpha", "beta", "charlie", "delta", "echo"};
        ListView myList = (ListView) view.findViewById(R.id.coinListView);
        ListAdapter myAdapter = new CoinRowAdapter(myList.getContext(), coin);
        myList.setAdapter(myAdapter);

        myList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = String.valueOf(adapterView.getItemAtPosition(i));
                        Toast.makeText(view.getContext(), item, Toast.LENGTH_LONG).show();
                    }
                }
        );

        return view;
    }
}
