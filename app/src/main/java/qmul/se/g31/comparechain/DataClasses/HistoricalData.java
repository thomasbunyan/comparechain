package qmul.se.g31.comparechain.DataClasses;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Thomas on 25/03/2018.
 */

public class HistoricalData extends MarketData{
    public HistoricalData(){
        super();
    }

    public ArrayList<Double> getHistoricData(double price, double percent, double percent2){
        ArrayList<Double> dataA = new ArrayList<Double>();
        ArrayList<Double> dataB = new ArrayList<Double>();
        Random r = new Random();

        dataA.add(price);
        dataA.add((price / (100.0 + percent)) * 100);

        double newPrice = 0;
        for(int i = 2; i < 30; i++){
            if(i == 6) dataA.add((dataA.get(0) / (100.0 + percent2)) * 100);
            else{
                if(r.nextInt(2) == 0) newPrice = dataA.get(i - 1) + (dataA.get(i - 1) * (r.nextInt(10)/100.0));
                else newPrice = dataA.get(i - 1) - (dataA.get(i - 1) * (r.nextInt(10)/100.0));
                dataA.add(newPrice);
            }
        }

        for(int i = dataA.size() - 1; i >=0; i--){
            dataB.add(dataA.get(i));
        }

        return dataB;
    }
}
