package com.gartmedia.brody.loocasino;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Console;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class wargame extends AppCompatActivity implements View.OnClickListener {

    private ImageButton chip1;
    private ImageButton chip5;
    private ImageButton chip25;
    private ImageButton chip50;
    private ImageButton chip100;
    private ImageButton clearButton;
    private ImageButton warStart;
    private ImageView playerCard;
    private ImageView dealerCard;
    private TextView betAmount;
    private Integer bet = 0;
    private Integer doShuffle = 0;

    Integer [][] cards = {{R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4},
            {R.drawable.card5, R.drawable.card6, R.drawable.card7, R.drawable.card8},
            {R.drawable.card9, R.drawable.card10, R.drawable.card11, R.drawable.card12},
            {R.drawable.card13, R.drawable.card14, R.drawable.card15, R.drawable.card16},
            {R.drawable.card17, R.drawable.card18, R.drawable.card19, R.drawable.card20},
            {R.drawable.card21, R.drawable.card22, R.drawable.card23, R.drawable.card24},
            {R.drawable.card25, R.drawable.card26, R.drawable.card27, R.drawable.card28},
            {R.drawable.card29, R.drawable.card30, R.drawable.card31, R.drawable.card32},
            {R.drawable.card33, R.drawable.card34, R.drawable.card35, R.drawable.card36},
            {R.drawable.card37, R.drawable.card38, R.drawable.card39, R.drawable.card40},
            {R.drawable.card45, R.drawable.card46, R.drawable.card47, R.drawable.card48},
            {R.drawable.card41, R.drawable.card42, R.drawable.card43, R.drawable.card44},
            {R.drawable.card49, R.drawable.card50, R.drawable.card51, R.drawable.card52}};


    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wargame);

        db = new Database(this);

        chip1 = (ImageButton)
                findViewById(R.id.chip1);
        chip5 = (ImageButton)
                findViewById(R.id.chip5);
        chip25 = (ImageButton)
                findViewById(R.id.chip25);
        chip50 = (ImageButton)
                findViewById(R.id.chip50);
        chip100 = (ImageButton)
                findViewById(R.id.chip100);
        clearButton = (ImageButton)
                findViewById(R.id.clearButton);
        warStart = (ImageButton)
                findViewById(R.id.warStart);
        betAmount = (TextView)
                findViewById(R.id.betAmount);
        betAmount = (TextView)
                findViewById(R.id.betAmount);

        playerCard = (ImageView)
                findViewById(R.id.warPlayerCard);

        dealerCard = (ImageView)
                findViewById(R.id.warDealerCard);

        chip1.setOnClickListener(this);
        chip5.setOnClickListener(this);
        chip25.setOnClickListener(this);
        chip50.setOnClickListener(this);
        chip100.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        warStart.setOnClickListener(this);
        betAmount.setOnClickListener(this);
    }

    public void onStart()
    {
        super.onStart();

        TextView cash = (TextView)
                findViewById(R.id.txtCash);
        TextView usernameText = (TextView)
                findViewById(R.id.txtUsername);

        Database db = new Database(this);

        SharedPreferences sharedPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String username = sharedPrefs.getString("username", "Logout and try again");

        SharedPreferences.Editor editor = sharedPrefs.edit();

        usernameText.setText("User: " + username);

        Cursor cashCursor = db.getCash(username);
        StringBuffer buffer = new StringBuffer();
        if (cashCursor.getCount()==0)
        {
            Toast.makeText(getApplicationContext(), "Accessing cash value failed, Please log out and try again"
                    + username, Toast.LENGTH_LONG).show();
        }

        else
        {

            String cashText = buffer.append(cashCursor.getInt(0)).toString();
            Integer cashInt = Integer.parseInt(cashText);
            editor.putInt("cash", cashInt);
            cash.setText("Balance: $" + cashText);
            editor.apply();
        }

        refreshGameHistory();
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Double cash = (double)sharedPrefs.getInt("cash", 0);
        String username = sharedPrefs.getString("username", "");
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        switch (v.getId()) {
            case R.id.chip1:
                if (bet+1 <= cash)
                {
                    bet+=1;
                    betAmount.setText("Bet: $" + bet.toString());
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry not enough cash", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chip5:
                if (bet+5 <= cash)
                {
                    bet+=5;
                    betAmount.setText("Bet: $" + bet.toString());
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry not enough cash", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chip25:
                if (bet+25 <= cash)
                {
                    bet+=25;
                    betAmount.setText("Bet: $" + bet.toString());
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry not enough cash", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chip50:
                if (bet+50 <= cash)
                {
                    bet+=50;
                    betAmount.setText("Bet: $" + bet.toString());
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry not enough cash", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chip100:
                if (bet+100 <= cash)
                {
                    bet+=100;
                    betAmount.setText("Bet: $" + bet.toString());
                }
                else{
                    Toast.makeText(getApplicationContext(), "Sorry not enough cash", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.clearButton:
                    bet=0;
                    betAmount.setText("Bet: $" + bet.toString());
                break;

            case R.id.warStart:
                if (cash-bet>=0&&bet!=0) {
                    doShuffle += 1;
                    Double winnings;

                    Random a = new Random();
                    Random b = new Random();

                    Integer c = a.nextInt(13);
                    Integer d = b.nextInt(4);

                    Integer playerCardNumber = cards[c][d];

                    if (playerCardNumber == 0) {
                        for (; ; ) {
                            c = a.nextInt(13);
                            d = b.nextInt(4);
                            playerCardNumber = cards[c][d];
                            if (playerCardNumber != 0) {
                                break;
                            }
                        }
                    }

                    playerCard.setImageResource(playerCardNumber);


                    Random e = new Random();
                    Random f = new Random();

                    Integer g = e.nextInt(13);
                    Integer h = f.nextInt(4);

                    Integer dealerCardNumber = cards[g][h];

                    if (dealerCardNumber == 0) {
                        for (; ; ) {
                            g = a.nextInt(13);
                            h = b.nextInt(4);
                            dealerCardNumber = cards[g][h];
                            if (dealerCardNumber != 0) {
                                break;
                            }
                        }

                    }

                    dealerCard.setImageResource(dealerCardNumber);


                    Integer playerCardValue = 0;
                    Integer dealerCardValue = 0;


                    for (int i = 0; i < 13; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (cards[i][j].equals(playerCardNumber)) {
                                playerCardValue = i;
                            }
                        }
                    }


                    for (int i = 0; i < 13; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (cards[i][j].equals(dealerCardNumber)) {
                                dealerCardValue = i;
                            }
                        }
                    }


                    if (playerCardValue > dealerCardValue) {
                        winnings = cash + bet;

                        Boolean updatedCashWin = db.updateCash(winnings, username);
                        if (!updatedCashWin) {
                            Toast.makeText(getApplicationContext(), "Sorry something happened", Toast.LENGTH_SHORT).show();

                        }

                        Toast.makeText(getApplicationContext(), "You Won", Toast.LENGTH_SHORT).show();

                        boolean isInserted = db.insertOutcome(username, "War", "WIN", currentDateTimeString);
                        if (isInserted==false)
                        {
                            Toast.makeText(getApplicationContext(), "Game data not inserted in game history", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Game data recorded in history", Toast.LENGTH_SHORT).show();
                        }

                    } else if (dealerCardValue > playerCardValue) {
                        winnings = cash-bet;
                        Boolean updatedCashWin = db.updateCash(winnings, username);
                        if (!updatedCashWin) {
                            Toast.makeText(getApplicationContext(), "Sorry something happened", Toast.LENGTH_SHORT).show();

                        }

                        Toast.makeText(getApplicationContext(), "You Lost", Toast.LENGTH_SHORT).show();

                        boolean isInserted = db.insertOutcome(username, "War", "LOSS", currentDateTimeString);
                        if (isInserted==false)
                        {
                            Toast.makeText(getApplicationContext(), "Game data not inserted in game history", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Game data recorded in history", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        winnings = cash;
                        Boolean updatedCashWin = db.updateCash(winnings, username);
                        if (!updatedCashWin) {
                            Toast.makeText(getApplicationContext(), "Sorry something happened", Toast.LENGTH_SHORT).show();

                        }

                        Toast.makeText(getApplicationContext(), "It's a draw", Toast.LENGTH_SHORT).show();

                        boolean isInserted = db.insertOutcome(username, "War", "DRAW", currentDateTimeString);
                        if (isInserted==false)
                        {
                            Toast.makeText(getApplicationContext(), "Game data not inserted in game history", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Game data recorded in history", Toast.LENGTH_SHORT).show();

                        }
                    }
                    cards[c][d] = 0;
                    cards[g][h] = 0;


                    TextView cashTxt = (TextView)
                            findViewById(R.id.txtCash);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Cursor cashCursor = db.getCash(username);
                    StringBuffer buffer = new StringBuffer();
                    if (cashCursor.getCount() == 0) {
                        Toast.makeText(getApplicationContext(), "Accessing cash value failed, Please log out and try again"
                                + username, Toast.LENGTH_LONG).show();
                    } else {

                        String cashText = buffer.append(cashCursor.getInt(0)).toString();
                        Integer cashInt = Integer.parseInt(cashText);
                        editor.putInt("cash", cashInt);
                        cashTxt.setText("Balance: $" + cashText);
                        editor.apply();
                    }
                }

                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid bet", Toast.LENGTH_SHORT).show();
                }

                if (doShuffle==26)
                {
                    Integer [][] cardsA = {{R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4},
                            {R.drawable.card5, R.drawable.card6, R.drawable.card7, R.drawable.card8},
                            {R.drawable.card9, R.drawable.card10, R.drawable.card11, R.drawable.card12},
                            {R.drawable.card13, R.drawable.card14, R.drawable.card15, R.drawable.card16},
                            {R.drawable.card17, R.drawable.card18, R.drawable.card19, R.drawable.card20},
                            {R.drawable.card21, R.drawable.card22, R.drawable.card23, R.drawable.card24},
                            {R.drawable.card25, R.drawable.card26, R.drawable.card27, R.drawable.card28},
                            {R.drawable.card29, R.drawable.card30, R.drawable.card31, R.drawable.card32},
                            {R.drawable.card33, R.drawable.card34, R.drawable.card35, R.drawable.card36},
                            {R.drawable.card37, R.drawable.card38, R.drawable.card39, R.drawable.card40},
                            {R.drawable.card45, R.drawable.card46, R.drawable.card47, R.drawable.card48},
                            {R.drawable.card41, R.drawable.card42, R.drawable.card43, R.drawable.card44},
                            {R.drawable.card49, R.drawable.card50, R.drawable.card51, R.drawable.card52}};
                    cards=cardsA;
                    doShuffle=0;
                    Toast.makeText(getApplicationContext(), "Shuffling...", Toast.LENGTH_SHORT).show();
                }

                bet=0;
                betAmount.setText("Bet: $" + bet.toString());
                break;
        }
    }

    public void refreshGameHistory()
    {
        SharedPreferences sharedPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String username = sharedPrefs.getString("username", "Logout and try again");

        ListView listView = (ListView)
                findViewById(R.id.outcomesList);

        ArrayList<gamehistoryobject> arrayList = new ArrayList<>();

        Cursor cursor = db.getOutcomes(username);
        Boolean isThereNext = true;

        if(!cursor.moveToLast())
        {
            Toast.makeText(getApplicationContext(), "No history, Play some games :)", Toast.LENGTH_SHORT).show();
        }
        else
        {
            while (isThereNext)
            {
                gamehistoryobject history = new gamehistoryobject(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                arrayList.add(history);

                isThereNext= cursor.moveToPrevious();
            }

            gamehistoryadapter adapter = new gamehistoryadapter(this.getApplicationContext(), arrayList, 0);

            listView.setAdapter(adapter);
        }

    }
}
