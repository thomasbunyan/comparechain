package qmul.se.g31.comparechain.GUIClasses.Dialog;

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

import qmul.se.g31.comparechain.DataClasses.Alert;
import qmul.se.g31.comparechain.DataClasses.Coin;
import qmul.se.g31.comparechain.DataClasses.Favorites;
import qmul.se.g31.comparechain.DataClasses.Repository;
import qmul.se.g31.comparechain.R;

/**
 * Created by Thomas on 19/03/2018.
 */

public class UpdateAlertDialog extends DialogFragment {

    private Coin coin;
    private Repository repo;
    private Favorites fav;
    private DialogInterface.OnDismissListener onDismissListener;

    private EditText maxPrice;
    private EditText minPrice;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        repo = Repository.getInstance();
        fav = Favorites.getInstance();

        NumberFormat priceFormatter = new DecimalFormat("$#,##0.00");
        Bundle mArgs = getArguments();
        coin = repo.searchCoin(mArgs.getString("coin"));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_change_alert, null);

        TextView coinName = (TextView) view.findViewById(R.id.coinName);
        TextView coinSymbol = (TextView) view.findViewById(R.id.coinSymbol);
        TextView coinPrice = (TextView) view.findViewById(R.id.coinPrice);
        ImageView coinIcon = (ImageView) view.findViewById(R.id.coinIcon);
        maxPrice = (EditText) view.findViewById(R.id.maxPrice);
        minPrice = (EditText) view.findViewById(R.id.minPrice);

        coinName.setText(coin.getName());
        coinSymbol.setText(coin.getSymbol());
        coinPrice.setText("Price: " + priceFormatter.format(coin.getPrice()));

        String imageName = coin.getSymbol();
        imageName = imageName.toLowerCase();
        int resID = getContext().getResources().getIdentifier(imageName, "mipmap", "qmul.se.g31.comparechain");
        if(resID != 0) coinIcon.setImageResource(resID);

        builder.setView(view)
                .setTitle("Add alert")
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing if canceled.
                    }
                })
                .setPositiveButton("Alert", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Update the alert.
                        double max, min;
                        if(maxPrice.getText().toString().equals("")) max = 0.0;
                        else max = (Double.parseDouble(maxPrice.getText().toString()));
                        if(minPrice.getText().toString().equals("")) min = 0.0;
                        else min = (Double.parseDouble(minPrice.getText().toString()));

                        fav.updateAlert(new Alert(coin.getSymbol(), max, min));
                    }
                });
        if(fav.hasAlert(coin.getSymbol())){
            builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Remove the alert
                    fav.removeAlert(coin.getSymbol());
                }
            })
            .setTitle("Update alert");
        }

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
