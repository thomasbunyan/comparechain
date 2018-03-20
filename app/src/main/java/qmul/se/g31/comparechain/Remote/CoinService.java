package qmul.se.g31.comparechain.Remote;

import qmul.se.g31.comparechain.MarketData.CoinObjects;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by M Chowdhury on 19/03/2018.
 */

public interface CoinService {
    @GET("data/price")
    Call<CoinObjects> calculateValue(@Query("fsym") String from, @Query("tsyms") String to);
}
