package qmul.se.g31.comparechain.GUIClasses;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.SimulatedPortfolio;
import qmul.se.g31.comparechain.GUIClasses.RowAdapters.CoinRowAdapter;
import qmul.se.g31.comparechain.GUIClasses.RowAdapters.SimulatedRowAdapter;
import qmul.se.g31.comparechain.R;
import qmul.se.g31.comparechain.GUIClasses.ViewCoinWindow.ViewCoinWindow;

/**
 * Created by Thomas on 12/03/2018.
 */

public class PortfolioView extends Fragment implements PortfolioHeaderFragment.HeaderListener{

    View view;
    private ListView myList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_portfolio, container, false);
        hideKeyboard(getActivity());

        final PortfolioHeaderFragment data = (PortfolioHeaderFragment) getChildFragmentManager().findFragmentById(R.id.simHeader);
        data.setData();
        final SimulatedPortfolio sim = SimulatedPortfolio.getInstance();

        myList = (ListView) view.findViewById(R.id.simList);
        ListAdapter myAdapter = new SimulatedRowAdapter(getContext(), R.layout.simulate_list_row, sim.getSimPort());
        myList.setAdapter(myAdapter);

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.setData();
                ListAdapter myAdapter = new SimulatedRowAdapter(getContext(), R.layout.simulate_list_row, sim.getSimPort());
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

    @Override
    public void updateData() {
        PortfolioHeaderFragment data = (PortfolioHeaderFragment) getChildFragmentManager().findFragmentById(R.id.simHeader);
        data.setData();
        SimulatedPortfolio sim = SimulatedPortfolio.getInstance();

        ListView myList = (ListView) view.findViewById(R.id.simList);
        ListAdapter myAdapter = new SimulatedRowAdapter(getContext(), R.layout.simulate_list_row, sim.getSimPort());
        myList.setAdapter(myAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        PortfolioHeaderFragment data = (PortfolioHeaderFragment) getChildFragmentManager().findFragmentById(R.id.simHeader);
        data.setData();
        SimulatedPortfolio sim = SimulatedPortfolio.getInstance();

        ListView myList = (ListView) view.findViewById(R.id.simList);
        ListAdapter myAdapter = new SimulatedRowAdapter(getContext(), R.layout.simulate_list_row, sim.getSimPort());
        myList.setAdapter(myAdapter);
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
