package com.devbaltasarq.hipotecas.View;

import android.app.Application;
import com.devbaltasarq.hipotecas.Core.SqlIO;

/**
 * Created by baltasarq on 23/11/15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        this.sqlDb = new SqlIO( this.getApplicationContext() );
    }

    /** @return the database handler */
    public SqlIO getDb() {
        return this.sqlDb;
    }

    private SqlIO sqlDb;
}
