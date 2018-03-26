package qmul.se.g31.comparechain.GUIClasses.RowAdapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import qmul.se.g31.comparechain.DataClasses.Alert;
import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.Favorites;
import qmul.se.g31.comparechain.DataClasses.Repository;
import qmul.se.g31.comparechain.GUIClasses.Dialog.UpdateAlertDialog;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 16/03/2018.
 */

public class FavoriteRowAdapter extends ArrayAdapter<Coin> {

    private int layout;
    private  ArrayList<Coin> mObjects;
    private Coin c;
    private View view;

    private Repository repo;
    private Favorites fav;
    private Context context;

    public FavoriteRowAdapter(@NonNull Context context, int resource, ArrayList<Coin> objects) {
        super(context, resource, objects);
        this.mObjects = objects;
        this.layout = resource;
        this.context = context;

        repo = Repository.getInstance();
        fav = Favorites.getInstance();
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
            convertView.setTag(viewHolder);
        }
        mainViewHolder = (ViewHolder) convertView.getTag();
        view = convertView;

        c = repo.searchCoin(mObjects.get(position).getSymbol());

        TextView coinName = (TextView) convertView.findViewById(R.id.coinName);
        TextView coinSymbol = (TextView) convertView.findViewById(R.id.coinSymbol);
        ImageView coinIcon = (ImageView) convertView.findViewById(R.id.coinIcon);

        coinName.setText(c.getName());
        coinSymbol.setText(c.getSymbol());

        String imageName = c.getSymbol();
        imageName = imageName.toLowerCase();
        int resID = getContext().getResources().getIdentifier(imageName, "mipmap", "qmul.se.g31.comparechain");
        if(resID != 0) coinIcon.setImageResource(resID);
        else coinIcon.setImageResource(getContext().getResources().getIdentifier("missingcoin", "mipmap", "qmul.se.g31.comparechain"));

        mainViewHolder.favButton.setImageResource(R.drawable.ic_menu_favorites);
        if(fav.hasAlert(c.getSymbol())) mainViewHolder.alertButton.setImageResource(R.drawable.ic_favorite);
        else mainViewHolder.alertButton.setImageResource(R.drawable.ic_favorite_border);

        checkAlert();

        mainViewHolder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                                Coin coin = repo.searchCoin(mObjects.get(position).getSymbol());
                                fav.removeFromFavorites(coin.getSymbol());
                                mObjects.remove(coin);
                                notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to remove this from favorites?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        mainViewHolder.alertButton.setOnClickListener(new View.OnClickListener() {
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
        UpdateAlertDialog alertDialog = new UpdateAlertDialog();

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Refresh.
                checkAlert();
                notifyDataSetChanged();
            }
        });
        alertDialog.setArguments(args);
        alertDialog.show(fm, "ALRT");
    }

    private void checkAlert(){
        // Check to see if an alert's been triggered.
        ArrayList<Alert> alerts = fav.getAlerts();
        for(int i = 0; i < alerts.size(); i++){
            if(alerts.get(i).getSymbol().equals(c.getSymbol())){
                if(alerts.get(i).checkAlert(c)){
                    // Set background to a colour.
                    int mycolor = this.getContext().getColor(R.color.alertwarning);
                    view.setBackgroundColor(mycolor);
                }
                else{
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
                break;
            }
            else{
                view.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public class ViewHolder {
        ImageView favButton;
        ImageView alertButton;
    }
}
