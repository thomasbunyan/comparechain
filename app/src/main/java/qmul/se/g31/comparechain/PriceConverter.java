package qmul.se.g31.comparechain;


import android.support.annotation.Nullable;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import qmul.se.g31.comparechain.MarketData.CoinObjects;
import qmul.se.g31.comparechain.Remote.CoinService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PriceConverter extends Fragment {

    CoinService mService;

    RadioButton coin2coin, money2coin, coin2money;
    MaterialSpinner fromSpinner, toSpinner;
    RadioGroup radioGroup;

    Button btnConvert;

    ImageView coinImage;
    TextView toTextView;

    String[] money = {"USD","EUR","GBP"};
    String[] coin = {"BTC","ETH","LTC","BCH","NEO","ETC","DASH","MAID","XEM","AUR","XMR"};


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mService = Common.getCoinService();

        fromSpinner = (MaterialSpinner)view.findViewById(R.id.fromSpinner);
        toSpinner = (MaterialSpinner)view.findViewById(R.id.toSpinner);

        toTextView = (TextView)view.findViewById(R.id.toTextView);
        btnConvert = (Button)view.findViewById(R.id.btnConvert);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                calculateValue();
            }
        });

        radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.coin2coin)
                    setCoin2CoinSource();
                else if (i == R.id.money2coin)
                    setMoney2CoinSource();
                else if (i == R.id.coin2money)
                    setCoin2MoneySource();
            }
        });

        coin2coin = (RadioButton)view.findViewById(R.id.coin2coin);
        money2coin = (RadioButton)view.findViewById(R.id.money2coin);
        coin2money = (RadioButton)view.findViewById(R.id.coin2money);

        coinImage = (ImageView)view.findViewById(R.id.coinImage);

        loadCoinList();



    }

    private void loadCoinList() {
        if(coin2money.isSelected())
            setCoin2MoneySource();
        else if(coin2coin.isSelected())
            setCoin2CoinSource();
        else if(money2coin.isSelected())
            setMoney2CoinSource();
    }

    private void setCoin2MoneySource() {
        fromSpinner.setItems(coin);
        toSpinner.setItems(money);
    }

    private void setMoney2CoinSource() {
        fromSpinner.setItems(money);
        toSpinner.setItems(coin);
    }

    private void setCoin2CoinSource() {
        fromSpinner.setItems(coin);
        toSpinner.setItems(coin);
    }

    private void calculateValue(){
        final ProgressDialog mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("please waiting...");
        mDialog.show();
        
        final String coinName = toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString();
        
        String fromCoin = fromSpinner.getItems().get(fromSpinner.getSelectedIndex()).toString();
        
        mService.calculateValue(fromCoin,coinName).enqueue(new Callback<CoinObjects>() {
            @Override
            public void onResponse(Call<CoinObjects> call, Response<CoinObjects> response) {
                mDialog.dismiss();
                
                if(coinName.equals("BTC"))
                    showData(response.body().getBTC());
                else if(coinName.equals("ETH"))
                    showData(response.body().getETH());
                else if(coinName.equals("LTC"))
                    showData(response.body().getLTC());
                else if(coinName.equals("BCH"))
                    showData(response.body().getBCH());
                else if(coinName.equals("NEO"))
                    showData(response.body().getNEO());
                else if(coinName.equals("ETC"))
                    showData(response.body().getETC());

                else if(coinName.equals("DASH"))
                    showData(response.body().getDASH());
                else if(coinName.equals("MAID"))
                    showData(response.body().getMAID());
                else if(coinName.equals("XEM"))
                    showData(response.body().getXEM());
                else if(coinName.equals("AUR"))
                    showData(response.body().getAUR());
                else if(coinName.equals("XMR"))
                    showData(response.body().getXMR());
                else if(coinName.equals("USD"))
                    showData(response.body().getUSD());
                else if(coinName.equals("EUR"))
                    showData(response.body().getEUR());
                else if(coinName.equals("GBP"))
                    showData(response.body().getGBP());
            }
            
            @Override
            public void onFailure(Call<CoinObjects> call, Throwable t) {
                Log.e("ERROR",t.getMessage());
                mDialog.dismiss();
            }
        });
    }

    private void showData(String value) {
        if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("BTC")){
            Picasso.with(getActivity()).load(Common.BTC_IMAGE)
                    .into(coinImage);
            toTextView.setText(value);
        }
        else if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("ETH")){
            Picasso.with(getActivity()).load(Common.ETH_IMAGE)
                    .into(coinImage);
            toTextView.setText(value);
        }
        else if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("LTC")){
            Picasso.with(getActivity()).load(Common.LTC_IMAGE)
                    .into(coinImage);
            toTextView.setText(value);
        }
        else if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("BCH")){
            Picasso.with(getActivity()).load(Common.BCH_IMAGE)
                    .into(coinImage);
            toTextView.setText(value);
        }
        else if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("NEO")){
            Picasso.with(getActivity()).load(Common.NEO_IMAGE)
                    .into(coinImage);
            toTextView.setText(value);
        }
        else if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("ETC")){
            Picasso.with(getActivity()).load(Common.ETC_IMAGE)
                    .into(coinImage);
            toTextView.setText(value);
        }
        else if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("DASH")){
            Picasso.with(getActivity()).load(Common.DASH_IMAGE)
                    .into(coinImage);
            toTextView.setText(value);
        }
        else if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("MAID")){
            Picasso.with(getActivity()).load(Common.MAID_IMAGE)
                    .into(coinImage);
            toTextView.setText(value);
        }
        else if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("XEM")){
            Picasso.with(getActivity()).load(Common.XEM_IMAGE)
                    .into(coinImage);
            toTextView.setText(value);
        }
        else if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("AUR")){
            Picasso.with(getActivity()).load(Common.AUR_IMAGE)
                    .into(coinImage);
            toTextView.setText(value);
        }
        else if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("XMR")){
            Picasso.with(getActivity()).load(Common.XMR_IMAGE)
                    .into(coinImage);
            toTextView.setText(value);
        }
//
        else if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("USD")){
            Picasso.with(getActivity()).load("http://www.iconsplace.com/white-icons/android-2-icon").into(coinImage);
            toTextView.setText("$ "+value);
        }
        else if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("EUR")){
            Picasso.with(getActivity()).load("http://www.iconsplace.com/white-icons/android-2-icon").into(coinImage);
            toTextView.setText("€ "+value);
        }
        else if(toSpinner.getItems().get(toSpinner.getSelectedIndex()).toString().equals("GBP")){
            Picasso.with(getActivity()).load("http://www.iconsplace.com/white-icons/android-2-icon").into(coinImage);
            toTextView.setText("£ "+value);
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_price_converter, container, false);


    }

}
