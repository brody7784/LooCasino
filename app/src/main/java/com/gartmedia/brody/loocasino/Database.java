package com.gartmedia.brody.loocasino;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brody on 2017-10-27.
 */

public class Database extends SQLiteOpenHelper{
    public static final String DATABASE_NAME= "LooCasino.db";

    public static final String TABLE_NAME_LOGIN= "Login";
    public static final String LOGIN_COL1="Username";
    public static final String LOGIN_COL2="Password";

    public static final String TABLE_NAME_USERINFO= "User_Info";
    public static final String USERINFO_COL1 ="Username";
    public static final String USERINFO_COL2 ="Cash";
    public static final String USERINFO_COL3 ="Email";
    public static final String USERINFO_COL4 ="DateJoined";

    public static final String TABLE_NAME_GAMEHISTORY= "Game_History";
    public static final String GAMEHISTORY_COL1="Username";
    public static final String GAMEHISTORY_COL2="Game";
    public static final String GAMEHISTORY_COL3="Outcome";
    public static final String GAMEHISTORY_COL4="DateTime";





    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_LOGIN + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Username TEXT, Password TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_USERINFO + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Username TEXT, Cash DOUBLE, Email TEXT, DateJoined TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_NAME_GAMEHISTORY + "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Username TEXT, Game TEXT, Outcome TEXT, DateTime TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERINFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_GAMEHISTORY);

        onCreate(db);
    }

    public boolean insertAccount(String username, String password, Double cash, String email, String dateJoined)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValuesLogin = new ContentValues();
        contentValuesLogin.put(LOGIN_COL1, username);
        contentValuesLogin.put(LOGIN_COL2, password);
        long resultLogin = db.insert(TABLE_NAME_LOGIN, null, contentValuesLogin);

        ContentValues contentValuesUserInfo = new ContentValues();
        contentValuesUserInfo.put(USERINFO_COL1, username);
        contentValuesUserInfo.put(USERINFO_COL2, cash);
        contentValuesUserInfo.put(USERINFO_COL3, email);
        contentValuesUserInfo.put(USERINFO_COL4, dateJoined);

        long resultUserInfo = db.insert(TABLE_NAME_USERINFO, null, contentValuesUserInfo);

        return !(resultLogin == -1 || resultUserInfo == -1);
    }




    public String login(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT username, password FROM Login";
        Cursor cursor = db.rawQuery(query, null);
        try {
            String a;
            String b = "Not Found";
            if (cursor.moveToFirst())
            {
                do
                {
                    a = cursor.getString(0);

                    if (a.equals(username))
                    {
                        b = cursor.getString(1);
                    }
                }
                while(cursor.moveToNext());
            }
            return b;
        }
        finally {
            cursor.close();
        }

    }

    public Cursor getCash(String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT cash FROM User_Info WHERE username = '" + username + "'";
        Cursor cashCursor = db.rawQuery(query, null);

        try
        {
            if (cashCursor!=null)
            {
                cashCursor.moveToFirst();
            }
            return cashCursor;
        }
        finally {
            db.close();
        }

    }


    public boolean updateCash(Double cash, String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USERINFO_COL2, cash);
        try
        {
            long resultSuccess = db.update(TABLE_NAME_USERINFO, cv, "username= ?", new String[] {username});

            return resultSuccess != -1;
        }
        finally {
            db.close();
        }


    }

    public boolean insertOutcome(String username, String game, String outcome, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValuesOutcome = new ContentValues();
        contentValuesOutcome.put(GAMEHISTORY_COL1, username);
        contentValuesOutcome.put(GAMEHISTORY_COL2, game);
        contentValuesOutcome.put(GAMEHISTORY_COL3, outcome);
        contentValuesOutcome.put(GAMEHISTORY_COL4, date);
        try
        {
            long resultOutcome = db.insert(TABLE_NAME_GAMEHISTORY, null, contentValuesOutcome);

            return !(resultOutcome == -1);
        }
        finally {
            db.close();
        }
    }

    public Cursor getOutcomes(String usernameQuery)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT game, outcome, datetime FROM Game_History WHERE username = '" + usernameQuery + "'" ;
        Cursor outcomesCursor = db.rawQuery(query, null);
        try
        {
            if (outcomesCursor!=null)
            {
                outcomesCursor.moveToLast();
            }
            return outcomesCursor;
        }
       finally {
            db.close();
        }
    }
}
