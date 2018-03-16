
package qmul.se.g31.comparechain.MarketData;

import java.util.ArrayList;

public class Favorites {

    public static Favorites instance;
    private Repository repo;
    private ArrayList<String> coins;

    public Favorites(){
        instance = this;
        this.repo = Repository.getInstance();
        this.coins = new ArrayList<String>();
    }
    
    // Pass a coin which is added to list of favorites and flags the coin as favorite.
    public void addToFavorites(String symbol){
        if(!this.coins.contains(symbol)){
            coins.add(symbol);
            repo.searchCoin(symbol).setFavorite(true);
            System.out.println("ADDED: " + repo.searchCoin(symbol).isFavorite());
        }
    }
    
    // Pass a coin which is removed from the list and removes the favorite flag.
    public void removeFromFavorites(String symbol){
        if(this.coins.contains(symbol)){
            coins.remove(symbol);
            repo.searchCoin(symbol).setFavorite(false);
            System.out.println("REMOVED: " + repo.searchCoin(symbol).isFavorite());
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
    
    // Singleton method to return instance.
    public static Favorites getInstance(){
        if(instance == null) instance = new Favorites();
        return instance;
    }
}
