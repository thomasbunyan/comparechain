package qmul.se.g31.comparechain.GUIClasses.RowAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 14/03/2018.
 */

public class CoinRowAdapter extends ArrayAdapter<Coin>{

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

        Coin c = getItem(position);

        TextView coinName = (TextView) view.findViewById(R.id.coinName);
        TextView coinSymbol = (TextView) view.findViewById(R.id.coinSymbol);
        TextView coinPrice = (TextView) view.findViewById(R.id.coinPrice);
        TextView coinChange = (TextView) view.findViewById(R.id.coinChange);
        ImageView coinIcon = (ImageView) view.findViewById(R.id.coinIcon);

        coinName.setText(c.getName());
        coinSymbol.setText(c.getSymbol());
        coinPrice.setText("$" + Double.toString(c.getPrice()));
        coinChange.setText(Double.toString(c.getPercent1h()) + "%");

        String imageName = c.getSymbol();
        imageName = imageName.toLowerCase();
        int resID = getContext().getResources().getIdentifier(imageName, "mipmap", "qmul.se.g31.comparechain");
        if(resID != 0) coinIcon.setImageResource(resID);

        if(c.getPercent1h() < 0) coinChange.setTextColor(Color.parseColor("#FF4136"));
        else if(c.getPercent1h() > 0) coinChange.setTextColor(Color.parseColor("#2ECC40"));

        return view;
    }
}
