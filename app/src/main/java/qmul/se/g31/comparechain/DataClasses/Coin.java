
package qmul.se.g31.comparechain.DataClasses;

import java.io.Serializable;
import java.util.ArrayList;

public class Coin implements Serializable{
    
    private String symbol;
    private String name;
    private double price;
    private long marketcap;
    private long supply;
    private long volume;
    private int rank;
    private double percent1h;
    private double percent24h;
    private double percent7d;
    private Graph[] graphs;
    private boolean favorite;
    
    public Coin(String symbol, String name, double price, long marketcap, long supply, long volume,int rank, double percent1h, double percent24h, double percent7d, ArrayList<Double> graphData){
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.marketcap = marketcap;
        this.supply = supply;
        this.volume = volume;
        this.rank = rank;
        this.percent1h = percent1h;
        this.percent24h = percent24h;
        this.percent7d = percent7d;
        this.graphs = new Graph[3];
        this.graphs[0] = new Graph(GraphType.LINE, graphData);
        this.favorite = false;
    }

    public String getSymbol(){
        return this.symbol;
    }
    public String getName(){
        return this.name;
    }
    public double getPrice(){
        return this.price;
    }
    public double getMarketCap(){
        return this.marketcap;
    }
    public long getSupply(){
        return this.supply;
    }
    public long getVolume(){
        return this.volume;
    }
    public int getRank(){
        return this.rank;
    }
    public double getPercent1h(){
        return this.percent1h;
    }
    public double getPercent24h(){
        return this.percent24h;
    }
    public double getPercent7d(){
        return this.percent7d;
    }
    public Graph getGraph(){
        return this.graphs[0];
    }
    public boolean isFavorite(){
        return this.favorite;
    }
    public void setFavorite(boolean fav){
        this.favorite = fav;
    }
}
