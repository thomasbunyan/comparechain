package qmul.se.g31.comparechain.MarketData;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 16/03/2018.
 */

public class FavoriteRowAdapter extends ArrayAdapter<Coin> {

    private int layout;
    private  ArrayList<Coin> mObjects;
    private Coin c;

    private Repository repo;
    private Favorites fav;

    public FavoriteRowAdapter(@NonNull Context context, int resource, ArrayList<Coin> objects) {
        super(context, resource, objects);
        this.mObjects = objects;
        this.layout = resource;

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
            convertView.setTag(viewHolder);
        }
        mainViewHolder = (ViewHolder) convertView.getTag();

        Coin c = repo.searchCoin(mObjects.get(position).getSymbol());

        TextView coinName = (TextView) convertView.findViewById(R.id.coinName);
        TextView coinSymbol = (TextView) convertView.findViewById(R.id.coinSymbol);
        ImageView coinIcon = (ImageView) convertView.findViewById(R.id.coinIcon);

        coinName.setText(c.getName());
        coinSymbol.setText(c.getSymbol());

        String imageName = c.getSymbol();
        imageName = imageName.toLowerCase();
        int resID = getContext().getResources().getIdentifier(imageName, "mipmap", "qmul.se.g31.comparechain");
        if(resID != 0) coinIcon.setImageResource(resID);

        mainViewHolder.favButton.setImageResource(R.drawable.ic_menu_favorites);

        mainViewHolder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coin coin = repo.searchCoin(mObjects.get(position).getSymbol());
                fav.removeFromFavorites(coin.getSymbol());
                mObjects.remove(coin);
                notifyDataSetChanged();
                // Somewhat buggy.
//                if(coin.isFavorite()){
//                    mainViewHolder.favButton.setImageResource(R.drawable.ic_star_border_black_24dp);
//                    fav.removeFromFavorites(coin.getSymbol());
//                }
//                else{
//                    mainViewHolder.favButton.setImageResource(R.drawable.ic_menu_favorites);
//                    fav.addToFavorites(coin.getSymbol());
//                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView favButton;
    }
}
