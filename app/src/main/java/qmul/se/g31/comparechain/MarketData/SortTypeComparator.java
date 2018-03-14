
package qmul.se.g31.comparechain.MarketData;

import qmul.se.g31.comparechain.MarketData.SortType;
import java.util.Comparator;
import qmul.se.g31.comparechain.MarketData.Coin;

public class SortTypeComparator implements Comparator<Coin>{
    
    private SortType type;
    
    public SortTypeComparator(SortType type){
        this.type = type;
    }

    @Override
    public int compare(Coin o1, Coin o2) {
        switch(type){
            case PRICE:
                return (int)(o2.getPrice() - o1.getPrice());
            case NAME:
                return o1.getName().compareTo(o2.getName());
            case SUPPLY:
                return (int)(o2.getSupply()- o1.getSupply());
            case MARKETCAP:
                return (int)(o2.getMarketCap()- o1.getMarketCap());
            case RANK:
                return (int)(o2.getRank()- o1.getRank());
            case PERCENT1H:
                return (int)(o2.getPercent1h()- o1.getPercent1h());
            case PERCENT24H:
                return (int)(o2.getPercent24h()- o1.getPercent24h());
            case PERCENT7D:
                return (int)(o2.getPercent7d()- o1.getPercent7d());
            default:
                return 0;
        }
    }
}
