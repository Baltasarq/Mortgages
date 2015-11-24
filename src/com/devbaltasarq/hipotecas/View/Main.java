package com.devbaltasarq.hipotecas.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import com.devbaltasarq.hipotecas.Core.Mortgage;
import com.devbaltasarq.hipotecas.Core.SqlIO;
import com.devbaltasarq.hipotecas.R;

import java.util.List;

public class Main extends Activity {
    public static final String LOG_TAG = "Main";
    public final static int NEW_MORTGAGE = 101;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main );

        ListView lItems = (ListView) this.findViewById( R.id.lItems );
        ImageButton btAdd = (ImageButton) this.findViewById( R.id.btAdd );

        lItems.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Main.this.calculateMortgage( position );
            }
        } );

        this.listItems = ( (App) this.getApplication() ).getDb().getAllItems();
        lItems.setAdapter( new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                this.listItems
        ) );
        this.registerForContextMenu( lItems );

        btAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main.this.addNewMortgage();
            }
        } );
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        switch ( v.getId() ) {
            case R.id.lItems:
                super.onCreateContextMenu( menu, v, menuInfo );
                this.getMenuInflater().inflate( R.menu.main_context, menu );
                break;
        }

        return;
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem)
    {
        super.onContextItemSelected( menuItem );

        boolean toret = false;

        switch ( menuItem.getItemId() ) {
            case R.id.main_context_delete:
                toret = true;
                this.deleteMortgage( ( (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo() ).position );
                break;
            case R.id.main_context_calculate:
                toret = true;
                this.calculateMortgage( ( (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo() ).position );
                break;
        }

        return toret;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu( menu );

        this.getMenuInflater().inflate( R.menu.main_properties, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        super.onOptionsItemSelected( menuItem );

        switch( menuItem.getItemId() ) {
            case R.id.main_properties_add:
                Main.this.addNewMortgage();
                break;
            case R.id.main_properties_back:
                Main.this.finish();
                break;
        }

        return true;
    }


    private void addNewMortgage() {
        this.startActivityForResult( new Intent( this, InputMortgage.class ), NEW_MORTGAGE );
    }

    private void deleteMortgage(int pos)
    {
        ListView lItems = (ListView) this.findViewById( R.id.lItems );
        SqlIO db = ( (App) this.getApplication() ).getDb();

        db.remove( this.listItems.get( pos ) );
        this.listItems.remove( pos );
        ( (ArrayAdapter ) lItems.getAdapter() ).notifyDataSetChanged();
    }

    private void calculateMortgage(int pos)
    {
        String bankName = this.listItems.get( pos ).getName();
        Intent data = new Intent( this, CalculateMortgage.class );

        data.putExtra( CalculateMortgage.ETQ_BANK_NAME, bankName );
        Log.v( LOG_TAG, String.format( " in calculateMortgage(): launching mortgage calculator for: '%s'", bankName ) );
        this.startActivity( data );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult( requestCode, resultCode, intent );

        if ( resultCode == RESULT_OK ) {
            ListView lItems = (ListView) this.findViewById( R.id.lItems );

            if ( requestCode == NEW_MORTGAGE ) {
                SqlIO db = ( (App) this.getApplication() ).getDb();

                Mortgage mortgage = new Mortgage(
                                intent.getExtras().getString( InputMortgage.ETQ_BANK ),
                                intent.getExtras().getDouble( InputMortgage.ETQ_PERCENTAGE ) );

                db.add( mortgage );
                this.listItems.add( mortgage );
                ( (ArrayAdapter) lItems.getAdapter() ).notifyDataSetChanged();
            }
        }

        return;
    }

    List<Mortgage> listItems;
}
