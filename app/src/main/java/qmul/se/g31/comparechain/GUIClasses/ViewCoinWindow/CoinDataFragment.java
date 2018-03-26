package qmul.se.g31.comparechain.GUIClasses.ViewCoinWindow;

import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.Repository;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 15/03/2018.
 */

public class CoinDataFragment extends Fragment{
    View view;

    ImageView coinIcon;
    TextView coinSymbol;

    TextView coinPrice;
    TextView bitPrice;

    TextView coinPercent1H;
    ImageView arrow1H;
    TextView coinPercent24H;
    ImageView arrow24H;
    TextView coinPercent7D;
    ImageView arrow7D;

    TextView rank;
    TextView marketCap;
    TextView supply;
    TextView volume;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_coin_data, container, false);
        return view;
    }

    public void setData(Coin c){
        Repository repo = Repository.getInstance();
        c = repo.searchCoin(c.getSymbol());
        getActivity().setTitle(c.getName());

        NumberFormat bitFormatter = new DecimalFormat("#0.00000000");
        Coin bitcoin = Repository.getInstance().searchCoin("BTC");
        String bitprices = bitFormatter.format(c.getPrice()/bitcoin.getPrice());

        NumberFormat bitFormatter2 = new DecimalFormat("#,###");
        NumberFormat priceFormatter = new DecimalFormat("$#,##0.00");

        coinIcon = (ImageView) view.findViewById(R.id.icon);
        coinSymbol = (TextView) view.findViewById(R.id.symbol);
        coinPrice = (TextView) view.findViewById(R.id.price);
        bitPrice = (TextView) view.findViewById(R.id.bitprice);
        coinPercent1H = (TextView) view.findViewById(R.id.coinChange1H);
        arrow1H = (ImageView) view.findViewById(R.id.arrow1);
        coinPercent24H = (TextView) view.findViewById(R.id.coinChange24H);
        arrow24H = (ImageView) view.findViewById(R.id.arrow24);
        coinPercent7D = (TextView) view.findViewById(R.id.coinChange7D);
        arrow7D = (ImageView) view.findViewById(R.id.arrow7);
        rank = (TextView) view.findViewById(R.id.rank);
        marketCap = (TextView) view.findViewById(R.id.marketcap);
        supply = (TextView) view.findViewById(R.id.supply);
        volume = (TextView) view.findViewById(R.id.volume);

        String imageName = c.getSymbol();
        imageName = imageName.toLowerCase();
        int resID = getContext().getResources().getIdentifier(imageName, "mipmap", "qmul.se.g31.comparechain");
        if(resID != 0) coinIcon.setImageResource(resID);
        else coinIcon.setImageResource(getContext().getResources().getIdentifier("missingcoin", "mipmap", "qmul.se.g31.comparechain"));

        int mycolor = this.getContext().getColor(R.color.bad);
        if(c.getPercent1h() < 0) {
            arrow1H.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
            arrow1H.setColorFilter(mycolor);
            coinPercent1H.setTextColor(ContextCompat.getColor(getContext(), R.color.bad));
        }
        if(c.getPercent24h() < 0){
            arrow24H.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
            arrow24H.setColorFilter(mycolor);
            coinPercent24H.setTextColor(ContextCompat.getColor(getContext(), R.color.bad));
        }
        if(c.getPercent7d() < 0){
            arrow7D.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
            arrow7D.setColorFilter(mycolor);
            coinPercent7D.setTextColor(ContextCompat.getColor(getContext(), R.color.bad));
        }

        coinSymbol.setText(c.getSymbol());
        coinPrice.setText(priceFormatter.format(c.getPrice()));
        bitPrice.setText(bitprices + " BTC");
        coinPercent1H.setText("(" + Double.toString(c.getPercent1h()) + "%)");
        coinPercent24H.setText("(" + Double.toString(c.getPercent24h()) + "%)");
        coinPercent7D.setText("(" + Double.toString(c.getPercent7d()) + "%)");
        rank.setText("#" + Integer.toString(c.getRank()));
        marketCap.setText("$" + bitFormatter2.format(c.getMarketCap()));
        supply.setText(bitFormatter2.format(c.getSupply()) + " " + c.getSymbol());
        volume.setText("$" + bitFormatter2.format(c.getVolume()));
    }
}
