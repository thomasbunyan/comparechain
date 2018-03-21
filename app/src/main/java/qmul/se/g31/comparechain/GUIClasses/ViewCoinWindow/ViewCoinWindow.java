package qmul.se.g31.comparechain.GUIClasses.ViewCoinWindow;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.Repository;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 15/03/2018.
 */

public class ViewCoinWindow extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_view_coin);

        Repository repo = Repository.getInstance();
        Intent intent = getIntent();
        Coin coin = (Coin)intent.getSerializableExtra("coin");
        coin = repo.searchCoin(coin.getSymbol());

        ActionsCoinFragment actions = (ActionsCoinFragment) getSupportFragmentManager().findFragmentById(R.id.coinFavoriteFragment);
        actions.setCoin(coin);
        CoinDataFragment data = (CoinDataFragment) getSupportFragmentManager().findFragmentById(R.id.coinDataFragment);
        data.setData(coin);
        CoinGraphFragment graph = (CoinGraphFragment) getSupportFragmentManager().findFragmentById(R.id.coinGraphFragment);
        graph.setData(coin);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
