package qmul.se.g31.comparechain.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by M Chowdhury on 19/03/2018.
 */

public class RetrofitClient {
    private static Retrofit retrofit=null;

    public static Retrofit getCient (String baseURL) {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
