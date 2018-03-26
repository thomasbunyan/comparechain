package qmul.se.g31.comparechain.GUIClasses;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.Repository;
import qmul.se.g31.comparechain.DataClasses.SimulatedPortfolio;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 17/03/2018.
 */

public class PortfolioHeaderFragment extends Fragment{

    private View view;
    HeaderListener headerListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_portfolio_header, container, false);
        return view;
    }

    public void setData(){
        SimulatedPortfolio sim = SimulatedPortfolio.getInstance();
        NumberFormat priceFormatter = new DecimalFormat("$#,##0.00");

        TextView userBalance = (TextView) view.findViewById(R.id.userBalance);
        Button resetButton = (Button) view.findViewById(R.id.resetButton);

        userBalance.setText(priceFormatter.format(sim.getUserFunds()));
        resetButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SimulatedPortfolio sim = SimulatedPortfolio.getInstance();
                Repository repo = Repository.getInstance();

                ArrayList<Coin> sims = sim.getSimPort();
                for(int i = 0; i < sims.size(); i++){
                    sim.removeCoin(repo.searchCoin(sims.get(i).getSymbol()));
                }

                // Send signal here
                headerListener.updateData();

                Intent i = new Intent();
                i.setAction("qmul.se.g31.refreshfrag");
                i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                getContext().sendBroadcast(i);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("qmul.se.g31.refreshfrag");
        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        getActivity().registerReceiver(receiver, intentFilter);
    }

    public interface HeaderListener{
        public void updateData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            headerListener = (HeaderListener) getParentFragment();
        } catch (ClassCastException e){
            System.out.println("NOPE");
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();// update your textView in the main layout
        }
    }

}
