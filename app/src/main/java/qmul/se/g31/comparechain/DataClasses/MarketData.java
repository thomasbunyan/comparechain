package qmul.se.g31.comparechain.DataClasses;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
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
import java.net.URL;
import java.util.ArrayList;

import qmul.se.g31.comparechain.MainWindowActivity;

/**
 * Created by Thomas on 24/03/2018.
 */

public class MarketData extends Service{

    private Repository repo;
    //private ArrayList<Double> a;
    private boolean initialised = false;

    // This can be used to find out how up-to-date the data is.
    private long lastBitTime;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        repo = Repository.getInstance();

        Runnable r = new Runnable(){
            @Override
            public void run() {
                while(!Thread.interrupted()){
                    synchronized (this){
                        try {
                            updateData();
                            wait(12000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        Thread t = new Thread(r);
        t.start();
        return Service.START_STICKY;
    }

    private void updateData(){
        if(!initialised){
            getData();
            initialised = true;
        }
        long currentTime = System.currentTimeMillis() / 1000L;
        double diff = Math.abs(lastBitTime - currentTime) / 60;
        if(diff >= 5){
            updateFromAPI();
            getData();
        }
    }

    public void getData(){
        JSONParser parser = new JSONParser();
        try{
            InputStream os = openFileInput("coinsave.json");
            BufferedReader file = new BufferedReader(new InputStreamReader(os));
            Object obj = parser.parse(file);
            JSONArray jsonData = (JSONArray) obj;

            JSONObject bitTime = (JSONObject) jsonData.get(0);
            lastBitTime = Integer.parseInt(bitTime.get("last_updated").toString());

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

    private void updateFromAPI(){
        String url = "https://api.coinmarketcap.com/v1/ticker/?limit=0";
        JSONParser parser = new JSONParser();
        try{
            InputStream in = new URL(url).openStream();
            BufferedReader fileIn = new BufferedReader(new InputStreamReader(in));
            Object obj = parser.parse(fileIn);

            OutputStream out = openFileOutput("coinsave.json", MODE_PRIVATE);
            BufferedWriter fileOut = new BufferedWriter(new OutputStreamWriter(out));
            fileOut.write(obj.toString());
            fileOut.flush();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
