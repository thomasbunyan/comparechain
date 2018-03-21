package qmul.se.g31.comparechain.GUIClasses.PriceConverterWindow;

import qmul.se.g31.comparechain.Remote.CoinService;
import qmul.se.g31.comparechain.Remote.RetrofitClient;

/**
 * Created by M Chowdhury on 19/03/2018.
 */

public class Common {
    private static final String API_URL = "https://min-api.cryptocompare.com";
    private static final String BASE_URL = "https://cryptocompare.com";

    public static final String BTC_IMAGE = new StringBuilder(BASE_URL).append("/media/19633/btc.png").toString();
    public static final String ETH_IMAGE = new StringBuilder(BASE_URL).append("/media/20646/eth2.png").toString();
    public static final String LTC_IMAGE = new StringBuilder(BASE_URL).append("/media/19782/litecoin-logo.png").toString();
    public static final String BCH_IMAGE = new StringBuilder(BASE_URL).append("/media/1383919/bch.jpg").toString();
    public static final String NEO_IMAGE = new StringBuilder(BASE_URL).append("/media/1383858/neo.jpg").toString();
    public static final String ETC_IMAGE = new StringBuilder(BASE_URL).append("/media/20275/eth2.png").toString();
    public static final String DASH_IMAGE = new StringBuilder(BASE_URL).append("/media/20026/dash.png").toString();
    public static final String MAID_IMAGE = new StringBuilder(BASE_URL).append("/media/352247/maid.png").toString();
    public static final String XEM_IMAGE = new StringBuilder(BASE_URL).append("/media/20490/xem.png").toString();
    public static final String AUR_IMAGE = new StringBuilder(BASE_URL).append("/media/19608/aur.png").toString();
    public static final String XMR_IMAGE = new StringBuilder(BASE_URL).append("/media/19969/xmr.png").toString();

    public static CoinService getCoinService(){
        return RetrofitClient.getCient(API_URL).create(CoinService.class);
    }
}
