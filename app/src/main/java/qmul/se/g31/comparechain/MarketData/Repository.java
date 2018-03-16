
package qmul.se.g31.comparechain.MarketData;

import java.util.ArrayList;
import java.util.HashMap;

public class Repository {
    
    public static Repository instance;
    private HashMap<String, Coin> repo;
    private HashMap<String, String> id;
    
    public Repository(){
        instance = this;
        repo = new HashMap<String, Coin>();
        id = new HashMap<String, String>();
    }
    
    // Pass a coin which is added to the id and repo.
    public void updateCoin(Coin coin){
        if(searchCoin(coin.getSymbol()) == null){
            repo.put(coin.getSymbol(), coin);
            id.put(coin.getName(), coin.getSymbol());
        }
        else{
            if(searchCoin(coin.getSymbol()).isFavorite()) coin.setFavorite(true);
            repo.replace(coin.getSymbol(), coin);
        }
    }
    
    // Pass a coin which is removed from the id and repo.
    public void removeCoin(Coin coin){
        repo.remove(coin.getSymbol());
        id.remove(coin.getName());
    }
    
    // Pass a string, either the symbol or name, which is used to get the Coin.
    public Coin searchCoin(String query){
        Coin toReturn = null;
        query = query.trim();    
        
        // If the query is all caps, it is the symbol that is being searched. Otherwise it is the name.
        if(query.toUpperCase().equals(query)) toReturn = repo.get(query);
        else toReturn = repo.get(id.get(query));
        
        if(toReturn == null) System.out.println("Can't find coin");
        return toReturn;
    }
    
    // Returns arraylist of Coins in the repo, with the option to have them sorted.
    public ArrayList<Coin> getData(SortTypeComparator c){
        ArrayList<Coin> coins = new ArrayList<Coin>(repo.values());
        coins.sort(c);
        return coins;
    }
    public ArrayList<Coin> getData(){
        ArrayList<Coin> coins = new ArrayList<Coin>(repo.values());
        return coins;
    }
    
    // Print to terminal all entries in the repo.
    public void printRepo(){
        System.out.printf("%-10s %-15s %-15s %-15s %-15s\n", "Symbol:", "Name:", "Price:", "Market cap:", "Supply:");
        for(Coin coin : repo.values()){
            System.out.printf("%-10s %-15s %-15s %-15s %-15s\n", coin.getSymbol(), coin.getName(), coin.getPrice(), coin.getMarketCap(), coin.getSupply());
        }
        System.out.println("");
    }
    
    // Singleton method to return instance.
    public static Repository getInstance(){
        if(instance == null) instance = new Repository();
        return instance;
    }
}
