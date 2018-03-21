package qmul.se.g31.comparechain.DataClasses;

/**
 * Created by Thomas on 19/03/2018.
 */

public class Alert {

    private boolean isMax;
    private boolean isMin;
    private double max;
    private double min;
    private String symbol;

    private Repository repo;

    public Alert(String symbol, double max, double min){
        repo = Repository.getInstance();
        this.symbol = symbol;

        if(max <= 0.0) this.isMax = false;
        else this.isMax = true;
        if(min <= 0.0) this.isMin = false;
        else this.isMin = true;

        if(isMax) this.max = max;
        if(isMin) this.min = min;
    }

    public boolean checkAlert(Coin coin){
        if(isMax && isMin){
            if(this.repo.searchCoin(coin.getSymbol()).getPrice() >= this.max || this.repo.searchCoin(coin.getSymbol()).getPrice() <= this.min) return true;
            else return false;
        }
        if(isMax){
            if(this.repo.searchCoin(coin.getSymbol()).getPrice() >= this.max) return true;
            else return false;
        }
        else{
            if(this.repo.searchCoin(coin.getSymbol()).getPrice() <= this.min) return true;
            else return false;
        }
    }

    public String getSymbol(){ return this.symbol; }
    public double getMax(){ return this.max; }
    public double getMin(){ return this.min; }
    public void setMax(double max){ this.max = max; }
    public void setMin(double min){ this.min = min; }

}
