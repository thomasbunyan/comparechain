
package qmul.se.g31.comparechain.DataClasses;

import java.util.Comparator;

public class ComparisonComparator implements Comparator<Coin>{
    
    private ComparisonType type;
    private boolean reverse;
    
    public ComparisonComparator(ComparisonType type, boolean reverse){
        this.type = type;
        this.reverse = reverse;
    }

    @Override
    public int compare(Coin o1, Coin o2) {
        if(reverse){
            Coin holder = o1;
            o1 = o2;
            o2 = holder;
        }
        switch(type){
            case PRICE:
                if(o2.getPrice() > o1.getPrice()) return 1;
                else if(o2.getPrice() < o1.getPrice()) return -1;
                else return 0;
                //return (int)(o2.getPrice() - o1.getPrice());
            case NAME:
                return o1.getName().compareTo(o2.getName());
            case SUPPLY:
                return (int)(o2.getSupply()- o1.getSupply());
            case MARKETCAP:
                return (int)(o2.getMarketCap()- o1.getMarketCap());
            case RANK:
                return (o1.getRank() - o2.getRank());
            case PERCENT1H:
                if(o2.getPercent1h() > o1.getPercent1h()) return 1;
                else if(o2.getPercent1h() < o1.getPercent1h()) return -1;
                else return 0;
            case PERCENT24H:
                if(o2.getPercent24h() > o1.getPercent24h()) return 1;
                else if(o2.getPercent24h() < o1.getPercent24h()) return -1;
                else return 0;
            case PERCENT7D:
                if(o2.getPercent7d() > o1.getPercent7d()) return 1;
                else if(o2.getPercent7d() < o1.getPercent7d()) return -1;
                else return 0;
            default:
                return 0;
        }
    }
}
