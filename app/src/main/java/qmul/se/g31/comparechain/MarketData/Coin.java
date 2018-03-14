
package qmul.se.g31.comparechain.MarketData;

public class Coin {
    
    private String symbol;
    private String name;
    private double price;
    private long marketcap;
    private long supply;
    private int rank;
    private double percent1h;
    private double percent24h;
    private double percent7d;
    
    public Coin(String symbol, String name, double price, long marketcap, long supply, int rank, double percent1h, double percent24h, double percent7d){
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.marketcap = marketcap;
        this.supply = supply;
        this.rank = rank;
        this.percent1h = percent1h;
        this.percent24h = percent24h;
        this.percent7d = percent7d;
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
}
