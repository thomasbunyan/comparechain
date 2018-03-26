
package qmul.se.g31.comparechain.DataClasses;

import java.util.ArrayList;

public class Favorites {

    public static Favorites instance;
    private Repository repo;
    private ArrayList<String> coins;
    private ArrayList<Alert> alerts;

    public Favorites(){
        instance = this;
        this.repo = Repository.getInstance();
        this.coins = new ArrayList<String>();
        this.alerts = new ArrayList<Alert>();
    }
    
    // Pass a coin which is added to list of favorites and flags the coin as favorite.
    public void addToFavorites(String symbol){
        if(!this.coins.contains(symbol)){
            coins.add(symbol);
            repo.searchCoin(symbol).setFavorite(true);
        }
    }
    
    // Pass a coin which is removed from the list and removes the favorite flag.
    public void removeFromFavorites(String symbol){
        if(this.coins.contains(symbol)){
            coins.remove(symbol);
            repo.searchCoin(symbol).setFavorite(false);
        }
    }
    
    // Returns arraylist of Coins from the list of favorites using the Repository.
    public ArrayList<Coin> getFavorites(){
        ArrayList<Coin> c = new ArrayList<Coin>();
        for(int i = 0; i < this.coins.size(); i++){
            Coin toAdd = repo.searchCoin(this.coins.get(i));
            if(toAdd != null) c.add(toAdd);
        }
        return c;
    }

    // Updates/Adds alert to an item in the favs.
    public void updateAlert(Alert alert){
        for(int i = 0; i < alerts.size(); i++){
            if(alerts.get(i).getSymbol().equals(alert.getSymbol())) {
                alerts.set(i, alert);
                return;
            }
        }
        alerts.add(alert);
    }

    // Removes alert.
    public void removeAlert(String symbol){
        for(int i = 0; i < alerts.size(); i++){
            if(alerts.get(i).getSymbol().equals(symbol)) {
                alerts.remove(i);
                return;
            }
        }
    }

    public boolean hasAlert(String symbol){
        for(int i = 0; i < alerts.size(); i++){
            if(alerts.get(i).getSymbol().equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    public Alert getAlert(String symbol){
        for(int i = 0; i < alerts.size(); i++){
            if(alerts.get(i).getSymbol().equals(symbol)) {
                return alerts.get(i);
            }
        }
        return null;
    }

    public ArrayList<Alert> getAlerts(){
        return this.alerts;
    }
    
    // Singleton method to return instance.
    public static Favorites getInstance(){
        if(instance == null) instance = new Favorites();
        return instance;
    }
}
