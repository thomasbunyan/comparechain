package qmul.se.g31.comparechain.DataClasses;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Thomas on 17/03/2018.
 */

public class SimulatedPortfolio {

    private static SimulatedPortfolio instance;
    private HashMap<String, Double> coinCost; // Amount spent.
    private HashMap<String, Double> coinVolume; // Volume of coin.
    private Repository repo;

    private double userFunds;
    private double cost;

    public SimulatedPortfolio(){
        instance = this;
        this.coinCost = new HashMap<String, Double>();
        this.coinVolume = new HashMap<String, Double>();
        this.repo = Repository.getInstance();
        this.userFunds = 0.0;
        this.cost = 0.0;
    }

    public boolean changeVolume(Coin coin, double vol){
        if(vol == 0) return false;
        String symbol = coin.getSymbol();
        coin = repo.searchCoin(symbol);
        if(this.coinVolume.containsKey(symbol)){ // The coin is present in the sim.
            double oldVol = this.coinVolume.get(symbol);
            if((vol + oldVol) < 0) return false;
            this.coinVolume.replace(symbol, vol + oldVol);
            double oldCost = this.coinCost.get(symbol);
            this.coinCost.replace(symbol, oldCost + (coin.getPrice() * vol));
        }
        else{ // The coin is not present in the sim.
            if(vol < 0) return false;
            this.coinVolume.put(coin.getSymbol(), vol);
            this.coinCost.put(symbol, (coin.getPrice() * vol));
        }
        this.cost = this.cost + (coin.getPrice() * vol);
        this.userFunds = userFunds + ((coin.getPrice() * vol) * -1);
        return true;
    }

    public void addCoin(Coin coin){
        this.coinVolume.put(coin.getSymbol(), 0.0);
        this.coinCost.put(coin.getSymbol(), 0.0);
    }
    public void removeCoin(Coin coin){
        if(this.coinVolume.containsKey(coin.getSymbol())){
            this.userFunds = this.userFunds + this.coinCost.remove(coin.getSymbol());
            this.coinVolume.remove(coin.getSymbol());
            this.coinCost.remove(coin.getSymbol());
        }
    }

    public double getCoinCost(Coin coin){
        return coinCost.get(coin.getSymbol());
    }
    public double getCoinVolume(Coin coin){
        return coinVolume.get(coin.getSymbol());
    }

    public double getCoinWorth(Coin coin){
        coin = repo.searchCoin(coin.getSymbol());
        return coin.getPrice() * coinVolume.get(coin.getSymbol());
    }
    public double getCoinProfit(Coin coin){
        coin = repo.searchCoin(coin.getSymbol());
        return getCoinWorth(coin) - coinCost.get(coin.getSymbol());
    }
    public double getUserFunds(){
        return this.userFunds;
    }


    public void setUserFunds(double funds){
        this.userFunds = funds;
    }

    // For I/O purposes
    public double getTotalCost(){
        return this.cost;
    }
    public void setTotalCost(double cost){
        this.cost = cost;
    }
    public void setCoinCost(Coin coin, double nPrice){
        coinCost.replace(coin.getSymbol(), nPrice);
    }

    public ArrayList<Coin> getSimPort(){
        ArrayList<String> simport = new ArrayList<String>(coinVolume.keySet());
        ArrayList<Coin> simportcoins = new ArrayList<Coin>();
        for(int i = 0; i < simport.size(); i++){
            simportcoins.add(repo.searchCoin(simport.get(i)));
        }
        return simportcoins;
    }

    public static SimulatedPortfolio getInstance(){
        if(instance == null) instance = new SimulatedPortfolio();
        return instance;
    }
}
