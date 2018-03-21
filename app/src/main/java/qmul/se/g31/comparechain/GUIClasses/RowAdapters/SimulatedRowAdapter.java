package qmul.se.g31.comparechain.GUIClasses.RowAdapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import qmul.se.g31.comparechain.GUIClasses.Dialog.ChangeBalanceDialog;
import qmul.se.g31.comparechain.GUIClasses.Dialog.ConfDialog;
import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.Favorites;
import qmul.se.g31.comparechain.DataClasses.Repository;
import qmul.se.g31.comparechain.DataClasses.SimulatedPortfolio;
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
            viewHolder.removeButton = (ImageView) convertView.findViewById(R.id.removeButton);
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
        mainViewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coin coin = repo.searchCoin(mObjects.get(position).getSymbol());
                //openConfDialog(coin.getSymbol());
                sim.removeCoin(coin);
                mObjects.remove(position);
                updatedData(mObjects);
            }
        });
        mainViewHolder.balanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coin coin = repo.searchCoin(mObjects.get(position).getSymbol());
                openDialog(coin.getSymbol());
            }
        });
        return convertView;
    }


    private void openDialog(String sym){
        Bundle args = new Bundle();
        args.putString("coin", sym);

        final FragmentManager fm = ((Activity) context).getFragmentManager();
        ChangeBalanceDialog balDialog = new ChangeBalanceDialog();

        balDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Refresh.
                updatedData(mObjects);
            }
        });
        balDialog.setArguments(args);
        balDialog.show(fm, "FPM");
    }

    private void openConfDialog(String sym){
        Bundle args = new Bundle();
        args.putString("coin", sym);

        final FragmentManager fm = ((Activity) context).getFragmentManager();
        ConfDialog balDialog = new ConfDialog();

        balDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Refresh.
                updatedData(mObjects);
            }
        });
        balDialog.setArguments(args);
        balDialog.show(fm, "YESNO");
    }



    public void updatedData(ArrayList<Coin> itemsArrayList) {
//        this.clear();
//        if (itemsArrayList != null){
//            for (Coin c : itemsArrayList) {
//                this.insert(c, this.getCount());
//            }
//        }
        notifyDataSetChanged();

        Intent i = new Intent();
        i.setAction("qmul.se.g31.refreshfrag");
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(i);
    }



    public class ViewHolder {
        ImageView favButton;
        ImageView removeButton;
        ImageView balanceButton;
    }
}
