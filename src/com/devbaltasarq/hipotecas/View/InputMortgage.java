package com.devbaltasarq.hipotecas.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.devbaltasarq.hipotecas.R;

/**
 * Inputs a new or existing mortgage
 * Created by baltasarq on 23/11/15.
 */
public class InputMortgage extends Activity {
    public static final String ETQ_BANK = "bank";
    public static final String ETQ_PERCENTAGE = "percentage";

    @Override
    public void onCreate(Bundle data) {
        super.onCreate(data);
        this.setContentView( R.layout.input_mortgage );

        ImageButton btSave = (ImageButton) this.findViewById( R.id.btSave );
        ImageButton btCancel = (ImageButton) this.findViewById( R.id.btCancel );
        final EditText edBank = (EditText) this.findViewById( R.id.edBank );
        final EditText edPercentage = (EditText) this.findViewById( R.id.edPercentage );

        btCancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMortgage.this.setResult( RESULT_CANCELED );
                InputMortgage.this.finish();
            }
        } );

        btSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                String bankName = edBank.getText().toString().trim();
                double percentage = Double.parseDouble( edPercentage.getText().toString() );

                if ( !bankName.isEmpty()
                  || percentage > 0.0 )
                {
                    data.putExtra( ETQ_BANK, bankName );
                    data.putExtra( ETQ_PERCENTAGE, percentage );
                    InputMortgage.this.setResult( RESULT_OK, data );
                    InputMortgage.this.finish();
                } else {
                    InputMortgage.this.setResult( RESULT_CANCELED );
                    InputMortgage.this.finish();
                }
            }
        } );
    }
}
