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
import qmul.se.g31.comparechain.MarketData.SimulatedPortfolio;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 16/03/2018.
 */

public class ActionsCoinFragment extends Fragment{

    private View view;
    private ImageView favIcon;
    private ImageView simButton;
    private Favorites fav;
    private Coin coin;
    private Repository repo;
    private SimulatedPortfolio sim;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_coin_actions, container, false);
        fav = Favorites.getInstance();
        repo = Repository.getInstance();
        sim = SimulatedPortfolio.getInstance();

        favIcon = (ImageView) view.findViewById(R.id.favoriteIcon);
        simButton = (ImageView) view.findViewById(R.id.simButton);


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

        simButton.setOnClickListener(
                new ImageView.OnClickListener(){
                    public void onClick(View v){
                        if(sim.getSimPort().contains(repo.searchCoin(coin.getSymbol()))){
                            // Remove from sim.
                            simButton.setImageResource(R.drawable.ic_add);
                            sim.removeCoin(repo.searchCoin(coin.getSymbol()));
                        }
                        else{
                            // Add to sim.
                            simButton.setImageResource(R.drawable.ic_check);
                            sim.addCoin(repo.searchCoin(coin.getSymbol()));
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
        if(sim.getSimPort().contains(repo.searchCoin(coin.getSymbol()))) simButton.setImageResource(R.drawable.ic_check);
        else simButton.setImageResource(R.drawable.ic_add);
    }
}
