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

        // Testing data.
        repo.updateCoin(new Coin("BTC", "Bitcoin", 30123.23, 34304300, 15844176, 1351351,1, 0.04, -0.3, -0.57));
        repo.updateCoin(new Coin("TOM", "Thomas", 2352.53, 43535345, 454252, 67436346, 1, 0.02, -0.3, -0.57));
        repo.updateCoin(new Coin("ETH", "Etherium", 5435.03, 4300, 346646, 3453, 1, -0.05, -0.3, -0.57));
        repo.updateCoin(new Coin("OAG", "Anothercoin", 234.34, 3453, 7565, 6856346, 1, 0.13, -0.3, -0.57));
        repo.updateCoin(new Coin("ARS", "Arsenalcoin", 2.34, 67, 37337, 2352626,1, -0.27, -0.3, -0.57));
        SimulatedPortfolio sim = SimulatedPortfolio.getInstance();
        ArrayList<Coin> simulations = sim.getSimPort();
        if(!simulations.contains(repo.searchCoin("BTC"))){
            sim.changeVolume(repo.searchCoin("BTC"), 1);
            sim.changeVolume(repo.searchCoin("ETH"), 9.234);
        }


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
