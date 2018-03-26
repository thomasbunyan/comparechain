package qmul.se.g31.comparechain;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import qmul.se.g31.comparechain.DataClasses.Alert;
import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.Favorites;
import qmul.se.g31.comparechain.DataClasses.HistoricalData;
import qmul.se.g31.comparechain.DataClasses.MarketData;
import qmul.se.g31.comparechain.DataClasses.Repository;
import qmul.se.g31.comparechain.DataClasses.SimulatedPortfolio;
import qmul.se.g31.comparechain.GUIClasses.CoinView;
import qmul.se.g31.comparechain.GUIClasses.FavoritesView;
import qmul.se.g31.comparechain.GUIClasses.NewsView;
import qmul.se.g31.comparechain.GUIClasses.PortfolioView;
import qmul.se.g31.comparechain.GUIClasses.PriceConverterWindow.PriceConverter;
import qmul.se.g31.comparechain.GUIClasses.ToolsView;

public class MainWindowActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent in = new Intent(this, MarketData.class);
        startService(in);

        getData();

        setContentView(R.layout.activity_main_window);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Coins");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new CoinView()).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        System.out.println("PAUSE   PAUSE   PAUSE");
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
            OutputStream os = openFileOutput("appsave.json", MODE_PRIVATE);
            BufferedWriter file = new BufferedWriter(new OutputStreamWriter(os));
            file.write(savedData.toString());
            file.flush();
        }
        catch(IOException e){
            Toast.makeText(MainWindowActivity.this, "I/O error.", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("RESUME  RESUME  RESUME");

        Repository repo = Repository.getInstance();
        SimulatedPortfolio sim = SimulatedPortfolio.getInstance();
        Favorites fav = Favorites.getInstance();

        JSONParser parser = new JSONParser();

        try{
            InputStream os = openFileInput("appsave.json");
            BufferedReader file = new BufferedReader(new InputStreamReader(os));
            Object obj = parser.parse(file);
            JSONObject jsonObjectOld = (JSONObject) obj;

            // Load simulated data.
            JSONArray simulatedSave = (JSONArray) jsonObjectOld.get("simulatedSave");
            JSONObject jsonObject = (JSONObject) simulatedSave.get(0);

            double userFunds = Double.parseDouble((String) jsonObject.get("usrFunds").toString());
            double totalCost = Double.parseDouble((String) jsonObject.get("totalCost").toString());

            JSONArray simCoinArray = (JSONArray) jsonObject.get("simData");

            for(int i = 0; i < simCoinArray.size(); i++){
                JSONObject simCoin = (JSONObject) simCoinArray.get(i);
                String symbol = (String) simCoin.get("symbol").toString();
                String volume = (String) simCoin.get("volume").toString();
                String cost = (String) simCoin.get("cost").toString();

                Coin coin = repo.searchCoin(symbol);
                sim.addCoin(coin);
                sim.changeVolume(coin, Double.parseDouble(volume));
                sim.setCoinCost(coin, Double.parseDouble(cost));
            }

            sim.setUserFunds(userFunds);
            sim.setTotalCost(totalCost);


            // Load favortite data.
            JSONArray favoritesSave = (JSONArray) jsonObjectOld.get("favoritesSave");
            JSONObject fjsonObject = (JSONObject) favoritesSave.get(0);

            JSONArray favorites = (JSONArray) fjsonObject.get("favorites");
            for(int i = 0; i < favorites.size(); i++){
                fav.addToFavorites((String) favorites.get(i));
            }

            JSONArray alerts = (JSONArray) fjsonObject.get("alertData");
            for(int i = 0; i < alerts.size(); i++){
                JSONObject alert = (JSONObject) alerts.get(i);
                String symbol = (String) alert.get("symbol").toString();
                String max = (String) alert.get("max").toString();
                String min = (String) alert.get("min").toString();

                fav.updateAlert(new Alert(symbol, Double.parseDouble(max), Double.parseDouble(min)));
            }

        }
        catch(FileNotFoundException e){
            e.printStackTrace();
            Toast.makeText(MainWindowActivity.this, "File not found.", Toast.LENGTH_SHORT);
        }
        catch(IOException e){
            e.printStackTrace();
            Toast.makeText(MainWindowActivity.this, "I/O error", Toast.LENGTH_SHORT);
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(MainWindowActivity.this, "Other exception", Toast.LENGTH_SHORT);
        }
    }

    // Gets the coins from the stored file and loads them into the repository.
    public void getData(){
        JSONParser parser = new JSONParser();
        Repository repo = Repository.getInstance();

        try{
            InputStream os = openFileInput("coinsave.json");
            BufferedReader file = new BufferedReader(new InputStreamReader(os));
            Object obj = parser.parse(file);
            JSONArray jsonData = (JSONArray) obj;

            for(int i = 0; i < jsonData.size(); i++){
                JSONObject currentCoin = (JSONObject) jsonData.get(i);
                String name = currentCoin.get("name").toString();
                String symbol = currentCoin.get("symbol").toString();
                int rank = Integer.parseInt(currentCoin.get("rank").toString());
                double price = Double.parseDouble(currentCoin.get("price_usd").toString());
                long volume = (long)Double.parseDouble(currentCoin.get("24h_volume_usd").toString());
                long marketcap = (long)Double.parseDouble(currentCoin.get("market_cap_usd").toString());
                long supply = (long)Double.parseDouble(currentCoin.get("available_supply").toString());
                double percent1H = Double.parseDouble(currentCoin.get("percent_change_1h").toString());
                double percent24H = Double.parseDouble(currentCoin.get("percent_change_24h").toString());
                double percent7D = Double.parseDouble(currentCoin.get("percent_change_7d").toString());
                ArrayList<Double> a = new HistoricalData().getHistoricData(price, percent24H, percent7D);
                repo.updateCoin(new Coin(symbol, name, price, marketcap, supply, volume, rank, percent1H, percent24H, percent7D, a));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_window, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_coins) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new CoinView()).commit();
            setTitle("Coins");
        } else if (id == R.id.nav_favorites) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new FavoritesView()).commit();
            setTitle("Favorites");
        } else if (id == R.id.nav_portfolio) {
            setTitle("Simulated Portfolio");
            fragmentManager.beginTransaction().replace(R.id.content_frame, new PortfolioView()).commit();
        } else if(id == R.id.nav_priceConverter) {
            setTitle("Price Converter");
            fragmentManager.beginTransaction().replace(R.id.content_frame, new PriceConverter()).commit();
        } else if (id == R.id.nav_news) {
            setTitle("News");
            fragmentManager.beginTransaction().replace(R.id.content_frame , new NewsView()).commit();
        } else if (id == R.id.nav_tools) {
            setTitle("Coin Comparison");
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ToolsView()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
