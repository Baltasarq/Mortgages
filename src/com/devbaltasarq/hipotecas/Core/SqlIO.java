package com.devbaltasarq.hipotecas.Core;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * The database manager.
 * Created by baltasarq on 23/11/15.
 */
public class SqlIO extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mortgages";
    public static final int DATABASE_VERSION = 2;


    public SqlIO(Context context)
    {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        db.beginTransaction();
        try {
            db.execSQL( "DELETE FROM mortgages_banks WHERE bank IS NULL" );
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.beginTransaction();

        try {
            db.execSQL( "CREATE TABLE IF NOT EXISTS mortgages_banks("
                    + "bank string(255) PRIMARY KEY,"
                    + "percentage real NOT NULL"
                    + ")"  );

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.beginTransaction();

        try {
            db.execSQL( "DROP TABLE IF EXISTS mortgages_banks" );
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        this.onCreate( db  );
    }

    public List<Mortgage> getAllItems()
    {
        ArrayList<Mortgage> toret = new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().rawQuery( "SELECT * FROM mortgages_banks", null );

        if ( cursor.moveToFirst() ) {
            do {
                toret.add( new Mortgage( cursor.getString( 0 ), cursor.getDouble( 1 ) ) );
            } while( cursor.moveToNext()  );
        }

        return toret;
    }

    public int getCountItems() {
        return this.getReadableDatabase().rawQuery( "SELECT * FROM mortgages_banks", null ).getCount();
    }

    public void add(Mortgage mortgage) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            db.execSQL( "INSERT INTO mortgages_banks(bank, percentage) VALUES(?, ?)",
                    new String[] { mortgage.getName(), Double.toString( mortgage.getPercentage() ) } );
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return;
    }

    /**
     * Returns a mortgage,given the banks name.
     * @param bankName the bankname to look for, as a String
     * @return The mortgage, as a Mortgage object
     */
    public Mortgage getByName(String bankName)
    {
        Mortgage toret = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM mortgages_banks WHERE bank = ?",
                new String[]{ bankName } );

        if ( cursor.moveToFirst() ) {
            toret = new Mortgage( cursor.getString( 0 ), cursor.getDouble( 1 ) );
        }

        return toret;
    }

    public void remove(Mortgage mortgage)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            db.execSQL( "DELETE FROM mortgages_banks WHERE bank = ?",
                    new String[] { mortgage.getName() } );
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return;
    }
}
