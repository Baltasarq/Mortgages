package com.devbaltasarq.hipotecas.View;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.devbaltasarq.hipotecas.Core.Mortgage;
import com.devbaltasarq.hipotecas.R;

/**
 * User interface for mortgage calculations
 * Created by baltasarq on 24/11/15.
 */
public class CalculateMortgage extends Activity {
    public static final String ETQ_BANK_NAME = "bank_name";

    private Mortgage mortgage;

    @Override
    public void onCreate(Bundle data) {
        super.onCreate( data );
        this.setContentView( R.layout.calculate_mortgage );

        // Connect widgets
        TextView lblBankName = (TextView) this.findViewById( R.id.lblBankName );
        TextView lblPercentage = (TextView) this.findViewById( R.id.lblPercentage );
        TextView lblResult = (TextView) this.findViewById( R.id.lblResult );
        EditText edAmount = (EditText) this.findViewById( R.id.edAmount );
        final EditText edNumYears = (EditText) this.findViewById( R.id.edNumYears );
        ImageButton btBack = (ImageButton) this.findViewById( R.id.btBack  );

        // Retrieve mortgage info
        this.mortgage = ( (App ) this.getApplication() ).getDb().getByName(
                                                this.getIntent().getExtras().getString( ETQ_BANK_NAME ) );

        // Set initial info
        lblBankName.setText( mortgage.getName() );
        lblPercentage.setText( Double.toString( mortgage.getPercentage() ) );
        edAmount.setText( Double.toString( Mortgage.TYPICAL_AMOUNT ) );
        edNumYears.setText( Integer.toString( Mortgage.TYPICAL_YEARS ) );
        this.updateResult();

        // Listeners
        btBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculateMortgage.this.finish();
            }
        } );

        edNumYears.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CalculateMortgage.this.updateResult();
            }
        } );

        edAmount.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CalculateMortgage.this.updateResult();
            }
        } );
    }

    private void updateResult() {
        final EditText edAmount = (EditText) this.findViewById( R.id.edAmount );
        final EditText edNumYears = (EditText) this.findViewById( R.id.edNumYears );
        final TextView lblResult = (TextView) this.findViewById( R.id.lblResult );
        final String contentNumYears = edNumYears.getText().toString().trim();
        final String contentAmount = edAmount.getText().toString().trim();

        if ( !contentAmount.isEmpty()
          && !contentNumYears.isEmpty() )
        {
            lblResult.setText( String.format( "%05.2f",
                    CalculateMortgage.this.mortgage.simulateYears(
                            Integer.parseInt( contentNumYears ),
                            Double.parseDouble( contentAmount ) ) ) );
        }
    }
}
