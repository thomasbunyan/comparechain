package qmul.se.g31.comparechain.ViewCoinWindow;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.Serializable;

import qmul.se.g31.comparechain.MarketData.Coin;
import qmul.se.g31.comparechain.MarketData.Favorites;
import qmul.se.g31.comparechain.MarketData.Repository;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 16/03/2018.
 */

public class ActionsCoinFragment extends Fragment{

    private View view;
    private ImageView favIcon;
    private Favorites fav;
    private Coin coin;
    private Repository repo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_coin_actions, container, false);
        fav = Favorites.getInstance();
        repo = Repository.getInstance();

        favIcon = (ImageView) view.findViewById(R.id.favoriteIcon);

        favIcon.setOnClickListener(
                new ImageView.OnClickListener(){
                    public void onClick(View v){
                        if(repo.searchCoin(coin.getSymbol()).isFavorite()){
                            favIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
                            fav.removeFromFavorites(coin.getSymbol());
                        }
                        else{
                            favIcon.setImageResource(R.drawable.ic_menu_favorites);
                            fav.addToFavorites(coin.getSymbol());
                        }
                    }
                }
        );
        return view;
    }

    public void setCoin(Coin coin){
        this.coin = coin;
        if(repo.searchCoin(coin.getSymbol()).isFavorite()) favIcon.setImageResource(R.drawable.ic_menu_favorites);
        else favIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
    }
}
