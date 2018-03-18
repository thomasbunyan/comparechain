package qmul.se.g31.comparechain.MarketData;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
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

import qmul.se.g31.comparechain.ChangeBalanceDialog;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 17/03/2018.
 */

public class SimulatedRowAdapter extends ArrayAdapter<Coin>{

    private int layout;
    private ArrayList<Coin> mObjects;
    private Context context;

    private Repository repo;
    private Favorites fav;
    private SimulatedPortfolio sim;

    public SimulatedRowAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Coin> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.mObjects = objects;
        repo = Repository.getInstance();
        fav = Favorites.getInstance();
        sim = SimulatedPortfolio.getInstance();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder mainViewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.favButton = (ImageView) convertView.findViewById(R.id.favoriteButton);
            viewHolder.alertButton = (ImageView) convertView.findViewById(R.id.alertButton);
            viewHolder.balanceButton = (ImageView) convertView.findViewById(R.id.balanceButton);
            convertView.setTag(viewHolder);
        }
        mainViewHolder = (ViewHolder) convertView.getTag();
        Coin c = repo.searchCoin(mObjects.get(position).getSymbol());
        NumberFormat volumeFormatter = new DecimalFormat("#0.00000000");
        NumberFormat priceFormatter = new DecimalFormat("$#,###.00");
        NumberFormat percentFormatter = new DecimalFormat("##.000%");

        ImageView coinIcon = (ImageView) convertView.findViewById(R.id.coinIcon);
        TextView coinName = (TextView) convertView.findViewById(R.id.coinName);
        TextView coinSymbol = (TextView) convertView.findViewById(R.id.coinSymbol);
        TextView investmentWorth = (TextView) convertView.findViewById(R.id.investmentWorth);
        TextView totalVolume = (TextView) convertView.findViewById(R.id.totalVolume);
        TextView coinProfit = (TextView) convertView.findViewById(R.id.coinProfit);
        TextView coinPrice = (TextView) convertView.findViewById(R.id.coinPrice);
        TextView coinChange = (TextView) convertView.findViewById(R.id.coinChange);

        coinName.setText(c.getName());
        coinSymbol.setText(c.getSymbol());
        investmentWorth.setText(priceFormatter.format(sim.getCoinWorth(c)));
        totalVolume.setText(volumeFormatter.format(sim.getCoinVolume(c)) + " " + c.getSymbol());
        coinProfit.setText(priceFormatter.format(sim.getCoinProfit(c)));
        coinPrice.setText(priceFormatter.format(c.getPrice()));
        coinChange.setText(percentFormatter.format(c.getPercent1h()));

        String imageName = c.getSymbol();
        imageName = imageName.toLowerCase();
        int resID = getContext().getResources().getIdentifier(imageName, "mipmap", "qmul.se.g31.comparechain");
        if(resID != 0) coinIcon.setImageResource(resID);

        if(!c.isFavorite()){
            mainViewHolder.favButton.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        else{
            mainViewHolder.favButton.setImageResource(R.drawable.ic_menu_favorites);
        }

        mainViewHolder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coin coin = repo.searchCoin(mObjects.get(position).getSymbol());
                if(coin.isFavorite()){
                    mainViewHolder.favButton.setImageResource(R.drawable.ic_star_border_black_24dp);
                    fav.removeFromFavorites(coin.getSymbol());
                }
                else{
                    mainViewHolder.favButton.setImageResource(R.drawable.ic_menu_favorites);
                    fav.addToFavorites(coin.getSymbol());
                }
            }
        });
        mainViewHolder.alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coin coin = repo.searchCoin(mObjects.get(position).getSymbol());
                if(false){
                    mainViewHolder.alertButton.setImageResource(R.drawable.ic_favorite_border);
                }
                else{
                    mainViewHolder.alertButton.setImageResource(R.drawable.ic_favorite);
                }
            }
        });
        mainViewHolder.balanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Coin coin = repo.searchCoin(mObjects.get(position).getSymbol());
                openDialog();
            }
        });
        return convertView;
    }

    public void openDialog(){
        FragmentManager fm = ((Activity) context).getFragmentManager();
        ChangeBalanceDialog balDialog = new ChangeBalanceDialog();
        balDialog.show(fm, "FPM");
    }

    public class ViewHolder {
        ImageView favButton;
        ImageView alertButton;
        ImageView balanceButton;
    }
}
