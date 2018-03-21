package qmul.se.g31.comparechain.DataClasses;

import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Thomas on 21/03/2018.
 */

public class Graph implements Serializable{

    private GraphType graphType;
    private ArrayList<Double> prices;

    public Graph(GraphType type, ArrayList<Double> prices){
        this.graphType = type;
        if(prices.size() == 30) this.prices = prices;
        else blankData();
    }

    public ArrayList<Double> getData(){
        return this.prices;
    }

    private void blankData(){
        this.prices = new ArrayList<Double>();
        for(int i = 0; i < 30; i++){
            this.prices.add(0.0);
        }
    }
}
