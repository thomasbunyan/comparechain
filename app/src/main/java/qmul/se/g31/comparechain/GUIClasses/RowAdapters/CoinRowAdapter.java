package qmul.se.g31.comparechain.GUIClasses.RowAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.Repository;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 14/03/2018.
 */

public class CoinRowAdapter extends ArrayAdapter<Coin>{

    private Coin c;
    private Repository repo;

    private TextView coinPrice;
    private TextView coinChange;

    public CoinRowAdapter(@NonNull Context context, ArrayList<Coin> resource) {
        super(context, R.layout.coin_list_row, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.coin_list_row, parent, false);
        }

        NumberFormat priceFormatter = new DecimalFormat("$#,##0.00");
        repo = Repository.getInstance();
        c = repo.searchCoin(getItem(position).getSymbol());

        TextView coinName = (TextView) view.findViewById(R.id.coinName);
        TextView coinSymbol = (TextView) view.findViewById(R.id.coinSymbol);
        coinPrice = (TextView) view.findViewById(R.id.coinPrice);
        coinChange = (TextView) view.findViewById(R.id.coinChange);
        ImageView coinIcon = (ImageView) view.findViewById(R.id.coinIcon);
        ImageView percentArraow = (ImageView) view.findViewById(R.id.arrow);

        coinName.setText(c.getName());
        coinSymbol.setText(c.getSymbol());
        coinPrice.setText(priceFormatter.format(c.getPrice()));

        String imageName = c.getSymbol();
        imageName = imageName.toLowerCase();
        int resID = getContext().getResources().getIdentifier(imageName, "mipmap", "qmul.se.g31.comparechain");
        if(resID != 0) coinIcon.setImageResource(resID);
        else coinIcon.setImageResource(getContext().getResources().getIdentifier("missingcoin", "mipmap", "qmul.se.g31.comparechain"));

        if(c.getPercent24h() < 0) {
            int mycolor = this.getContext().getColor(R.color.bad);
            coinChange.setText(Double.toString(c.getPercent24h()) + "%");
            coinChange.setTextColor(mycolor);
            percentArraow.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
            percentArraow.setColorFilter(mycolor, PorterDuff.Mode.SRC_ATOP);
        }
        else{
            int mycolor = this.getContext().getColor(R.color.good);
            coinChange.setText("+" + Double.toString(c.getPercent24h()) + "%");
            coinChange.setTextColor(mycolor);
            percentArraow.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
            percentArraow.setColorFilter(mycolor, PorterDuff.Mode.SRC_ATOP);
        }

        return view;
    }
}
