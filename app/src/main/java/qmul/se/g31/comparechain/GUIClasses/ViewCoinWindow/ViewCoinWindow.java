package qmul.se.g31.comparechain.GUIClasses.ViewCoinWindow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.Repository;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 15/03/2018.
 */

public class ViewCoinWindow extends AppCompatActivity{

    private Repository repo;
    private Coin coin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_view_coin);

        Intent intent = getIntent();
        repo = Repository.getInstance();
        coin = (Coin)intent.getSerializableExtra("coin");
        coin = repo.searchCoin(coin.getSymbol());

        final ActionsCoinFragment actions = (ActionsCoinFragment) getSupportFragmentManager().findFragmentById(R.id.coinFavoriteFragment);
        actions.setCoin(coin);
        final CoinDataFragment data = (CoinDataFragment) getSupportFragmentManager().findFragmentById(R.id.coinDataFragment);
        data.setData(coin);
        final CoinGraphFragment graph = (CoinGraphFragment) getSupportFragmentManager().findFragmentById(R.id.coinGraphFragment);
        graph.setData(coin);

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.swipe);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                coin = repo.searchCoin(coin.getSymbol());
                actions.setCoin(coin);
                data.setData(coin);
                graph.setData(coin);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl.setRefreshing(false);
                    }
                }, 300);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
