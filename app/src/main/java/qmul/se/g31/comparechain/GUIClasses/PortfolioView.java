package qmul.se.g31.comparechain.GUIClasses;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.Serializable;

import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.SimulatedPortfolio;
import qmul.se.g31.comparechain.GUIClasses.RowAdapters.SimulatedRowAdapter;
import qmul.se.g31.comparechain.R;
import qmul.se.g31.comparechain.GUIClasses.ViewCoinWindow.ViewCoinWindow;

/**
 * Created by Thomas on 12/03/2018.
 */

public class PortfolioView extends Fragment{

    View view;
    //ArrayList<Coin> simulations;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_portfolio, container, false);

        PortfolioHeaderFragment data = (PortfolioHeaderFragment) getChildFragmentManager().findFragmentById(R.id.simHeader);
        data.setData();
        SimulatedPortfolio sim = SimulatedPortfolio.getInstance();
        //simulations = sim.getSimPort();

        ListView myList = (ListView) view.findViewById(R.id.simList);
        ListAdapter myAdapter = new SimulatedRowAdapter(getContext(), R.layout.simulate_list_row, sim.getSimPort());
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



    @Override
    public void onResume() {
        super.onResume();
        PortfolioHeaderFragment data = (PortfolioHeaderFragment) getChildFragmentManager().findFragmentById(R.id.simHeader);
        data.setData();
        SimulatedPortfolio sim = SimulatedPortfolio.getInstance();
        //simulations = sim.getSimPort();

        ListView myList = (ListView) view.findViewById(R.id.simList);
        ListAdapter myAdapter = new SimulatedRowAdapter(getContext(), R.layout.simulate_list_row, sim.getSimPort());
        myList.setAdapter(myAdapter);
    }
}
