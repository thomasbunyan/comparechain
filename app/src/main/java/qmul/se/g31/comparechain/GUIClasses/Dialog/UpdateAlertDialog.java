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
import android.widget.Toast;

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

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        else coinIcon.setImageResource(getContext().getResources().getIdentifier("missingcoin", "mipmap", "qmul.se.g31.comparechain"));

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
                        double max, min;
                        if(maxPrice.getText().toString().equals("")) max = -1;
                        else max = (Double.parseDouble(maxPrice.getText().toString()));
                        if(minPrice.getText().toString().equals("")) min = -1;
                        else min = (Double.parseDouble(minPrice.getText().toString()));

                        if(max <= 0.0 && min <= 0.0){
                            // Don't update the alert.
                            Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(max != -1 || min != -1){
                                if((max != -1 && max <= coin.getPrice()) || (min != -1 && min >= coin.getPrice()) || max == 0 || min == 0){
                                    // Don't update the alert.
                                    Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    // Update the alert.
                                    fav.updateAlert(new Alert(coin.getSymbol(), max, min));
                                    Toast.makeText(getContext(), "Alert updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                // Don't update the alert.
                                Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        if(fav.hasAlert(coin.getSymbol())){
            if(fav.getAlert(coin.getSymbol()).getMax() != 0) maxPrice.setHint("Current bound: " + priceFormatter.format(fav.getAlert(coin.getSymbol()).getMax()));
            if(fav.getAlert(coin.getSymbol()).getMin() != 0) minPrice.setHint("Current bound: " + priceFormatter.format(fav.getAlert(coin.getSymbol()).getMin()));
            builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Remove the alert
                    fav.removeAlert(coin.getSymbol());
                    Toast.makeText(getContext(), "Alert removed", Toast.LENGTH_SHORT).show();
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
