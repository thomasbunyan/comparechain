package qmul.se.g31.comparechain;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import qmul.se.g31.comparechain.MarketData.Coin;
import qmul.se.g31.comparechain.MarketData.Repository;
import qmul.se.g31.comparechain.MarketData.SimulatedPortfolio;

/**
 * Created by Thomas on 17/03/2018.
 */

public class ChangeBalanceDialog extends DialogFragment {

    private Coin coin;
    private Repository repo;
    private SimulatedPortfolio sim;
    private DialogInterface.OnDismissListener onDismissListener;

    private EditText input;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        repo = Repository.getInstance();
        sim = SimulatedPortfolio.getInstance();

        Bundle mArgs = getArguments();
        coin = repo.searchCoin(mArgs.getString("coin"));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_change_balance, null);

        NumberFormat volumeFormatter = new DecimalFormat("#0.00000000");

        ImageView coinIcon = (ImageView) view.findViewById(R.id.coinIcon);
        TextView curVol = (TextView) view.findViewById(R.id.curVol);
        input = (EditText) view.findViewById(R.id.userInput);


        curVol.setText("Current holdings: " + volumeFormatter.format(sim.getCoinVolume(coin)) + " " + coin.getSymbol());

        String imageName = coin.getSymbol();
        imageName = imageName.toLowerCase();
        int resID = getContext().getResources().getIdentifier(imageName, "mipmap", "qmul.se.g31.comparechain");
        if(resID != 0) coinIcon.setImageResource(resID);

        builder.setView(view)
                .setTitle("Change coin balance")
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing if canceled.
                    }
                })
                .setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Buy the value.
                        double changeBy = (Double.parseDouble(input.getText().toString()));
                        if((sim.getCoinVolume(coin) + changeBy) > coin.getSupply()){
                            // Error. Trying to buy more than available.
                        }
                        else{
                            sim.changeVolume(coin, changeBy);
                        }
                    }
                })
                .setNegativeButton("Sell", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Sell the value.
                        double changeBy = (Double.parseDouble(input.getText().toString())) * -1;
                        if((sim.getCoinVolume(coin) + changeBy) < 0){
                            // Error. Trying to sell more than in simulation.
                        }
                        else{
                            sim.changeVolume(coin, changeBy);
                        }
                    }
                });

        return builder.create();
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }
}
