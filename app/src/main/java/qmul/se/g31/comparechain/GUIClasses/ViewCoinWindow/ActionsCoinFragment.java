package qmul.se.g31.comparechain.GUIClasses.ViewCoinWindow;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import qmul.se.g31.comparechain.DataClasses.Alert;
import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.Favorites;
import qmul.se.g31.comparechain.DataClasses.Repository;
import qmul.se.g31.comparechain.DataClasses.SimulatedPortfolio;
import qmul.se.g31.comparechain.R;

import static android.content.Context.MODE_PRIVATE;

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
                        // Because it is paused, we have to re-save the data. Annoying.
                        saveData();
                    }
                }
        );
        return view;
    }

    public void setCoin(Coin coin){
        this.coin = repo.searchCoin(coin.getSymbol());
        if(repo.searchCoin(coin.getSymbol()).isFavorite()) favIcon.setImageResource(R.drawable.ic_menu_favorites);
        else favIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
        if(sim.getSimPort().contains(repo.searchCoin(coin.getSymbol()))) simButton.setImageResource(R.drawable.ic_check);
        else simButton.setImageResource(R.drawable.ic_add);
    }

    public void saveData(){
        // Save data
        Repository repo = Repository.getInstance();
        SimulatedPortfolio sim = SimulatedPortfolio.getInstance();
        Favorites fav = Favorites.getInstance();
        ArrayList<Coin> coins = sim.getSimPort();
        ArrayList<Coin> favorites = fav.getFavorites();
        ArrayList<Alert> alerts = fav.getAlerts();

        JSONObject savedData = new JSONObject();

        // Simulation data.
        JSONArray simData = new JSONArray();
        JSONObject simObj = new JSONObject();

        simObj.put("usrFunds", sim.getUserFunds());
        simObj.put("totalCost", sim.getTotalCost());

        JSONArray coinArray = new JSONArray();
        for(int i = 0; i < coins.size(); i++){
            JSONObject coinObj = new JSONObject();
            coinObj.put("symbol", coins.get(i).getSymbol());
            coinObj.put("volume", sim.getCoinVolume(coins.get(i)));
            coinObj.put("cost", sim.getCoinCost(coins.get(i)));
            coinArray.add(coinObj);
        }
        simObj.put("simData", coinArray);

        simData.add(simObj);
        savedData.put("simulatedSave", simData);

        // Favorite data.
        JSONArray favData = new JSONArray();
        JSONObject favObj = new JSONObject();

        JSONArray favArray = new JSONArray();
        for(int i = 0; i < favorites.size(); i++){
            favArray.add(favorites.get(i).getSymbol());
        }
        favObj.put("favorites", favArray);

        JSONArray alertArray = new JSONArray();
        for(int i = 0; i < alerts.size(); i++){
            JSONObject alertObj = new JSONObject();
            alertObj.put("symbol", alerts.get(i).getSymbol());
            alertObj.put("max", alerts.get(i).getMax());
            alertObj.put("min", alerts.get(i).getMin());
            alertArray.add(alertObj);
        }
        favObj.put("alertData", alertArray);

        favData.add(favObj);
        savedData.put("favoritesSave", favData);

        // Save to file.
        try{
            OutputStream os = getContext().openFileOutput("appsave.json", MODE_PRIVATE);
            BufferedWriter file = new BufferedWriter(new OutputStreamWriter(os));
            file.write(savedData.toString());
            file.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
