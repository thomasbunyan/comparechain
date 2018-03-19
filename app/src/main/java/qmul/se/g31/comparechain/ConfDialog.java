package qmul.se.g31.comparechain;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
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

public class ConfDialog extends DialogFragment {

    private Coin coin;
    private Repository repo;
    private SimulatedPortfolio sim;
    private DialogInterface.OnDismissListener onDismissListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        repo = Repository.getInstance();
        sim = SimulatedPortfolio.getInstance();

        Bundle mArgs = getArguments();
        coin = repo.searchCoin(mArgs.getString("coin"));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_confirm, null);

        builder.setView(view)
                .setTitle("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Remove the coin.
                        sim.removeCoin(repo.searchCoin(coin.getSymbol()));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing.
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
