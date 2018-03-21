package qmul.se.g31.comparechain.GUIClasses.ViewCoinWindow;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Random;

import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.Graph;
import qmul.se.g31.comparechain.DataClasses.Repository;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 19/03/2018.
 */

public class CoinGraphFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_coin_graph, container, false);
        return view;
    }

    public void setData(Coin c){
        Repository repo = Repository.getInstance();
        Coin coin = repo.searchCoin(c.getSymbol());
        Graph graphObj = coin.getGraph();

        ArrayList<Double> graphData = graphObj.getData();

        double maxBound = 0;
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        DataPoint[] dataPoint = new DataPoint[30];
        for(int i = 0; i < dataPoint.length; i++){
            double data = graphData.get(i);
            if(data > maxBound) maxBound = data;
            dataPoint[i] = new DataPoint(i + 1, data);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoint);
        series.setThickness(5);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(31);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(maxBound * 1.1);
        graph.addSeries(series);
    }
}
