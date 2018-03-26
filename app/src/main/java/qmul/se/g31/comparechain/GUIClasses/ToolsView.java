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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.Favorites;
import qmul.se.g31.comparechain.DataClasses.Repository;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 12/03/2018.
 */

public class ToolsView extends Fragment {

    View view;
    private Repository repo;
    private Coin coin1;
    private Coin coin2;

    private Spinner spinnerOne;
    private Spinner spinnerTwo;

    private TextView priceA;
    private TextView marketcapA;
    private TextView rankA;
    private TextView percent1HA;
    private TextView percent24HA;
    private TextView percent7DA;

    private TextView priceB;
    private TextView marketcapB;
    private TextView rankB;
    private TextView percent1HB;
    private TextView percent24HB;
    private TextView percent7DB;

    private TextView priceDiff;
    private TextView mcapDiff;
    private TextView rankDiff;
    private TextView p1HDiff;
    private TextView p24HDiff;
    private TextView p7DDiff;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_tools, container, false);
        hideKeyboard(getActivity());

        repo = repo.getInstance();
        Favorites fav = Favorites.getInstance();
        ArrayList<Coin> coin = fav.getFavorites();
        final ArrayList<String> coinNames = new ArrayList<String>();
        for(int i = 0; i < coin.size(); i++) coinNames.add(coin.get(i).getSymbol());

        priceA = (TextView) view.findViewById(R.id.priceA);
        marketcapA = (TextView) view.findViewById(R.id.marketcapA);
        rankA = (TextView) view.findViewById(R.id.rankA);
        percent1HA = (TextView) view.findViewById(R.id.percent1HA);
        percent24HA = (TextView) view.findViewById(R.id.percent24HA);
        percent7DA = (TextView) view.findViewById(R.id.percent7DA);

        priceB = (TextView) view.findViewById(R.id.priceB);
        marketcapB = (TextView) view.findViewById(R.id.marketcapB);
        rankB = (TextView) view.findViewById(R.id.rankB);
        percent1HB = (TextView) view.findViewById(R.id.percent1HB);
        percent24HB = (TextView) view.findViewById(R.id.percent24HB);
        percent7DB = (TextView) view.findViewById(R.id.percent7DB);

        priceDiff = (TextView) view.findViewById(R.id.priceDiff);
        mcapDiff = (TextView) view.findViewById(R.id.mcapDiff);
        rankDiff = (TextView) view.findViewById(R.id.rankDiff);
        p1HDiff = (TextView) view.findViewById(R.id.p1HDiff);
        p24HDiff = (TextView) view.findViewById(R.id.p24HDiff);
        p7DDiff = (TextView) view.findViewById(R.id.p7DDiff);


        spinnerOne = (Spinner) view.findViewById(R.id.coinOne);
        spinnerTwo = (Spinner) view.findViewById(R.id.coinTwo);
        ArrayAdapter<String> a = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, coinNames);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerOne.setAdapter(a);
        spinnerOne.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        NumberFormat priceFormatter = new DecimalFormat("$#,##0.00");
                        coin1 = repo.searchCoin(coinNames.get(i));
                        priceA.setText(priceFormatter.format(coin1.getPrice()));
                        marketcapA.setText(formatPrice(coin1.getMarketCap()));
                        rankA.setText("#" + coin1.getRank());
                        percent1HA.setText(Double.toString(coin1.getPercent1h()) + "%");
                        percent24HA.setText(Double.toString(coin1.getPercent24h()) + "%");
                        percent7DA.setText(Double.toString(coin1.getPercent7d()) + "%");
                        if(coin2 != null) calcDiff();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                }
        );

        spinnerTwo.setAdapter(a);
        spinnerTwo.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        NumberFormat priceFormatter = new DecimalFormat("$#,##0.00");
                        coin2 = repo.searchCoin(coinNames.get(i));
                        priceB.setText(priceFormatter.format(coin2.getPrice()));
                        marketcapB.setText(formatPrice(coin2.getMarketCap()));
                        rankB.setText("#" + coin2.getRank());
                        percent1HB.setText(Double.toString(coin2.getPercent1h()) + "%");
                        percent24HB.setText(Double.toString(coin2.getPercent24h()) + "%");
                        percent7DB.setText(Double.toString(coin2.getPercent7d()) + "%");
                        calcDiff();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                }
        );

        return view;
    }

    private String formatPrice(double price){
        NumberFormat priceFormatter = new DecimalFormat("$#,##0.00");
        String newPrice;

        if(price > 1000000000.0) newPrice = String.format("$%.2fB", price/ 1000000000.0);
        else if(price > 1000000.0) newPrice = String.format("$%.2fM", price/ 1000000.0);
        else newPrice = priceFormatter.format(price);

        return newPrice;
    }

    private void calcDiff(){
        coin1 = repo.searchCoin(coin1.getSymbol());
        coin2 = repo.searchCoin(coin2.getSymbol());

        NumberFormat percentFormatter = new DecimalFormat("#,###");

        priceDiff.setText("Price Difference: " + percentFormatter.format(((coin2.getPrice() - coin1.getPrice()) / coin2.getPrice()) * 100) + "%");
        mcapDiff.setText("Marketcap Difference: " + percentFormatter.format(((coin2.getMarketCap() - coin1.getMarketCap()) / coin2.getMarketCap()) * 100) + "%");
        rankDiff.setText("Rank Difference: " + (coin1.getRank() - coin2.getRank()));
        p1HDiff.setText("Percent1H Difference: " + percentFormatter.format(((coin2.getPercent1h() - coin1.getPercent1h()) / coin2.getPercent1h()) * 100) + "%");
        p24HDiff.setText("Percent24H Difference: " + percentFormatter.format(((coin2.getPercent24h() - coin1.getPercent24h()) / coin2.getPercent24h()) * 100) + "%");
        p7DDiff.setText("Percent7D Difference: " + percentFormatter.format(((coin2.getPercent7d() - coin1.getPercent7d()) / coin2.getPercent7d()) * 100) + "%");
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
