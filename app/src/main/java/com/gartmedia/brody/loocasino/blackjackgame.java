package com.gartmedia.brody.loocasino;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class blackjackgame extends AppCompatActivity implements View.OnClickListener {

    private ImageButton chip1;
    private ImageButton chip5;
    private ImageButton chip25;
    private ImageButton chip50;
    private ImageButton chip100;
    private ImageButton clearButton;
    private TextView betAmount;
    private Double bet = 0.00;
    private TextView playerValue;
    private TextView dealerValue;


    private ImageButton bjButton;
    private ImageButton hitButton;
    private ImageButton standButton;
    private ImageButton ddButton;

    private ImageView imagePlayerCard1;
    private ImageView imageDealerCard1;
    private ImageView imagePlayerCard2;
    private ImageView imageDealerCard2;

    List<Integer> playerCards = new ArrayList<Integer>();
    List<Integer> dealerCards = new ArrayList<Integer>();


    private RelativeLayout playerCardsLayout;
    private RelativeLayout dealerCardsLayout;

    int hitCardNumPlayer = 1;
    int hitCardNumDealer = 1;

    Integer playerScore = 0;
    Integer dealerScore = 0;
    Boolean aceExistsPlayer = false;
    Boolean aceExistsDealer = false;
    Boolean highAcePlayer = false;
    Boolean highAceDealer = false;
    Boolean dealerStands = false;
    Boolean dealerBusts = false;
    Boolean playerBusts = false;

    Integer amountAcesPlayer=0;

    int doShuffle = 0;

    Integer[] dealerCard2;

    Database db;

    Integer[][] cards = {{R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4},
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blackjackgame);

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
        betAmount = (TextView)
                findViewById(R.id.betAmount);
        bjButton = (ImageButton)
                findViewById(R.id.bjButton);
        hitButton = (ImageButton)
                findViewById(R.id.hitButton);
        standButton = (ImageButton)
                findViewById(R.id.standButton);
        ddButton = (ImageButton)
                findViewById(R.id.ddButton);

        imagePlayerCard1 = (ImageView)
                findViewById(R.id.playerCard1);
        imagePlayerCard2 = (ImageView)
                findViewById(R.id.playerCard2);
        imageDealerCard1 = (ImageView)
                findViewById(R.id.dealerCard1);
        imageDealerCard2 = (ImageView)
                findViewById(R.id.dealerCard2);


        playerValue= (TextView)
                findViewById(R.id.playerValue);
        dealerValue= (TextView)
                findViewById(R.id.dealerValue);

        imageDealerCard2 = (ImageView)
                findViewById(R.id.dealerCard2);

        playerCardsLayout = (RelativeLayout)
                findViewById(R.id.playerHitCards);
        dealerCardsLayout = (RelativeLayout)
                findViewById(R.id.dealerHitCards);

        chip1.setOnClickListener(this);
        chip5.setOnClickListener(this);
        chip25.setOnClickListener(this);
        chip50.setOnClickListener(this);
        chip100.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        betAmount.setOnClickListener(this);
        bjButton.setOnClickListener(this);
        hitButton.setOnClickListener(this);
        standButton.setOnClickListener(this);
        ddButton.setOnClickListener(this);

    }

    public void onStart() {
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
        if (cashCursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "Accessing cash value failed, Please log out and try again"
                    + username, Toast.LENGTH_LONG).show();
        } else {

            String cashText = buffer.append(cashCursor.getInt(0)).toString();
            Integer cashInt = Integer.parseInt(cashText);
            editor.putInt("cash", cashInt);
            cash.setText("Balance: $" + cashText);
            editor.apply();
        }

        refreshGameHistory();
    }


    public void refreshGameHistory() {
        SharedPreferences sharedPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String username = sharedPrefs.getString("username", "Logout and try again");

        ListView listView = (ListView)
                findViewById(R.id.outcomesList);

        ArrayList<gamehistoryobject> arrayList = new ArrayList<>();

        Cursor cursor = db.getOutcomes(username);
        Boolean isThereNext = true;

        if (!cursor.moveToLast()) {
            Toast.makeText(getApplicationContext(), "No history, Play some games :)", Toast.LENGTH_SHORT).show();
        } else {
            while (isThereNext) {
                gamehistoryobject history = new gamehistoryobject(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                arrayList.add(history);

                isThereNext = cursor.moveToPrevious();
            }

            gamehistoryadapter adapter = new gamehistoryadapter(this.getApplicationContext(), arrayList, 0);

            listView.setAdapter(adapter);
        }

    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Double cash = (double)sharedPrefs.getInt("cash", 0);
        String username = sharedPrefs.getString("username", "");
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        switch (v.getId()) {
            case R.id.chip1:
                if (bet + 1 <= cash) {
                    bet += 1;
                    betAmount.setText("Bet: $" + bet.toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry not enough cash", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chip5:
                if (bet + 5 <= cash) {
                    bet += 5;
                    betAmount.setText("Bet: $" + bet.toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry not enough cash", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chip25:
                if (bet + 25 <= cash) {
                    bet += 25;
                    betAmount.setText("Bet: $" + bet.toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry not enough cash", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chip50:
                if (bet + 50 <= cash) {
                    bet += 50;
                    betAmount.setText("Bet: $" + bet.toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry not enough cash", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chip100:
                if (bet + 100 <= cash) {
                    bet += 100;
                    betAmount.setText("Bet: $" + bet.toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry not enough cash", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.clearButton:
                bet = 0.00;
                betAmount.setText("Bet: $" + bet.toString());
                break;

            case R.id.bjButton:
                if (doShuffle>=40)
                {
                    doShuffle();
                }
                playerCards.clear();
                dealerCards.clear();
                doShuffle+=4;
                hitCardNumPlayer = 1;
                hitCardNumDealer = 1;
                aceExistsPlayer = false;
                aceExistsDealer = false;
                highAcePlayer = false;
                highAceDealer = false;
                playerScore=0;
                dealerScore=0;
                if (cash-bet>=0&&bet!=0) {
                    Double afterBetCash = cash - bet;
                    Boolean cashTaken = db.updateCash(afterBetCash, username);
                    cash = (double)sharedPrefs.getInt("cash", 0);
                    if (cashTaken) {
                        afterStartButtons();

                        Integer [] playerCard1 = dealCardsRandom();
                        imagePlayerCard1.setBackgroundResource(cards[playerCard1[0]][playerCard1[1]]);
                        playerCards.add(returnCardValue(playerCard1[2]));
                        if(returnCardValue(playerCard1[2])==1)
                        {
                            playerCards.set(0, 11);
                            highAcePlayer = true;
                            aceExistsPlayer =true;
                        }
                        cards[playerCard1[0]][playerCard1[1]] = 0;

                        Integer [] dealerCard1 = dealCardsRandom();
                        imageDealerCard1.setBackgroundResource(cards[dealerCard1[0]][dealerCard1[1]]);
                        dealerCards.add(returnCardValue(dealerCard1[2]));
                        if(returnCardValue(dealerCard1[2])==1)
                        {
                            dealerCards.set(0, 11);
                            highAceDealer = true;
                            aceExistsDealer =true;
                        }
                        cards[dealerCard1[0]][dealerCard1[1]] = 0;

                        Integer [] playerCard2 = dealCardsRandom();
                        imagePlayerCard2.setBackgroundResource(cards[playerCard2[0]][playerCard2[1]]);
                        playerCards.add(returnCardValue(playerCard2[2]));
                        if(returnCardValue(playerCard2[2])==1 && !aceExistsPlayer)
                        {
                            playerCards.set(1, 11);
                            highAcePlayer = true;
                            aceExistsPlayer =true;
                        }
                        cards[playerCard2[0]][playerCard2[1]]=0;

                        dealerCard2 = dealCardsRandom();
                        imageDealerCard2.setBackgroundResource(R.drawable.backcard);
                        dealerCards.add(returnCardValue(dealerCard2[2]));
                        if(returnCardValue(dealerCard2[2])==1 && !aceExistsDealer)
                        {
                            dealerCards.set(1, 11);
                            highAceDealer = true;
                            aceExistsDealer =true;
                        }
                        cards[dealerCard2[0]][dealerCard2[1]]=0;


                        Integer pv =playerCards.get(0)+playerCards.get(1);
                        Integer dv =dealerCards.get(0);

                        playerValue.setText(pv.toString());
                        dealerValue.setText(dv.toString());

                        if (dealerCards.get(0)+dealerCards.get(1)==21 && aceExistsDealer)
                        {
                            boolean isInserted = db.insertOutcome(username, "Blackjack", "BJLOSS", currentDateTimeString);
                            if (isInserted==false)
                            {
                                Toast.makeText(getApplicationContext(), "Game data not inserted in game history", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Game data recorded in history", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(getApplicationContext(), "dealer has blackjack", Toast.LENGTH_SHORT).show();
                            imageDealerCard2.setBackgroundResource(dealerCard2[2]);
                            dv =dealerCards.get(0) + dealerCards.get(1);
                            dealerValue.setText(dv.toString());
                            final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms
                                        newGameButtons();

                                    }
                                }, 4500);
                        }

                        else if (playerCards.get(0)+playerCards.get(1)==21 && aceExistsPlayer)
                        {
                            boolean isInserted = db.insertOutcome(username, "Blackjack", "BJWIN", currentDateTimeString);
                            if (isInserted==false)
                            {
                                Toast.makeText(getApplicationContext(), "Game data not inserted in game history", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Game data recorded in history", Toast.LENGTH_SHORT).show();
                            }

                            Boolean newCash = db.updateCash(cash+(bet*1.5), username);
                            if(newCash)
                            {
                                Toast.makeText(getApplicationContext(), "BLACKJACK FOR PLAYER", Toast.LENGTH_SHORT).show();
                                imageDealerCard2.setBackgroundResource(dealerCard2[2]);
                                dv =dealerCards.get(0) + dealerCards.get(1);
                                dealerValue.setText(dv.toString());
                                updateCashValues();
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms
                                        newGameButtons();

                                    }
                                }, 4500);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "We apologize, something went, wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        updateCashValues();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Sorry transaction did not go through", Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid bet", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.hitButton:
                hitCardNumPlayer +=1;
                doShuffle+=1;
                Integer[] cardPlayer = dealCardsRandom();
                Integer newCardPlayer = returnCardValue(cardPlayer[2]);
                cards[cardPlayer[0]][cardPlayer[1]] = 0;
                addCard(hitCardNumPlayer, cardPlayer[2]);
                playerCards.add(newCardPlayer);
                playerScore = sum(playerCards);
                amountAcesPlayer = checkAces(playerCards);

                if (amountAcesPlayer>0 && highAcePlayer && playerScore>21)
                {
                    playerCards.add(-10);
                    highAcePlayer=false;
                }

                if (newCardPlayer == 1 && playerScore+10<=21)
                {
                    playerCards.add(10);
                }

                playerScore = sum(playerCards);
                playerValue.setText(playerScore.toString());


                if(playerScore>21)
                {
                    standBustButtons();
                    playerBusts=true;
                    Toast.makeText(getApplicationContext(), "and that's a bust, Sorry", Toast.LENGTH_SHORT).show();

                    boolean isInserted = db.insertOutcome(username, "Blackjack", "LOSS", currentDateTimeString);
                    if (isInserted==false)
                    {
                        Toast.makeText(getApplicationContext(), "Game data not inserted in game history", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Game data recorded in history", Toast.LENGTH_SHORT).show();
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            newGameButtons();
                        }
                    }, 4500);
                }

                else if(playerScore==21)
                {

                    standBustButtons();
                    standButton.performClick();
                    Toast.makeText(getApplicationContext(), "21, good hand", Toast.LENGTH_SHORT).show();
                }

                ddButton.setEnabled(false);
                break;

            case R.id.standButton:
                standBustButtons();
                imageDealerCard2.setBackgroundResource(dealerCard2[2]);
                dealerScore = sum(dealerCards);

                if (dealerScore <17)
                {
                    for(;;)
                    {
                        hitCardNumDealer+=1;
                        doShuffle+=1;
                        if (dealerScore<17)
                        {
                            dealerScore=dealerHit();

                        }
                        else if (dealerScore>=17 && dealerScore<=21)
                        {
                            dealerStands = true;
                            break;
                        }
                        else if (dealerScore>21)
                        {
                            dealerBusts = true;

                            break;
                        }
                    }
                }
                dealerValue.setText(dealerScore.toString());

                playerScore = sum(playerCards);
                if (playerScore==dealerScore)
                {
                    Boolean cashGivin = db.updateCash(cash + bet, username);
                    if (cashGivin)
                    {
                        updateCashValues();
                        Toast.makeText(getApplicationContext(), "Thats a draw, money returned", Toast.LENGTH_SHORT).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                newGameButtons();
                            }
                        }, 4500);
                        boolean isInserted = db.insertOutcome(username, "Blackjack", "DRAW", currentDateTimeString);
                        if (isInserted==false)
                        {
                            Toast.makeText(getApplicationContext(), "Game data not inserted in game history", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Game data recorded in history", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "We appologize, something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (playerScore<=21 && dealerBusts)
                {
                    boolean isInserted = db.insertOutcome(username, "Blackjack", "WIN", currentDateTimeString);
                    if (isInserted==false)
                    {
                        Toast.makeText(getApplicationContext(), "Game data not inserted in game history", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Game data recorded in history", Toast.LENGTH_SHORT).show();
                    }

                    Boolean cashGivin = db.updateCash(cash + (bet*2), username);
                    if (cashGivin)
                    {
                        updateCashValues();
                        Toast.makeText(getApplicationContext(), "Player wins by dealer bust", Toast.LENGTH_SHORT).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                newGameButtons();
                            }
                        }, 4500);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "We appologize, something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (playerScore>dealerScore && playerScore<=21)
                {
                    boolean isInserted = db.insertOutcome(username, "Blackjack", "WIN", currentDateTimeString);
                    if (isInserted==false)
                    {
                        Toast.makeText(getApplicationContext(), "Game data not inserted in game history", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Game data recorded in history", Toast.LENGTH_SHORT).show();
                    }

                    Boolean cashTaken = db.updateCash(cash+(bet*2), username);
                    if (cashTaken)
                    {
                        updateCashValues();
                        Toast.makeText(getApplicationContext(), "Player wins by score", Toast.LENGTH_SHORT).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                newGameButtons();
                            }
                        }, 4500);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "We appologize, something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (!dealerBusts && playerBusts)
                {
                    updateCashValues();

                    boolean isInserted = db.insertOutcome(username, "Blackjack", "LOSS", currentDateTimeString);
                    if (isInserted==false)
                    {
                        Toast.makeText(getApplicationContext(), "Game data not inserted in game history", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Game data recorded in history", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(getApplicationContext(), "dealer wins by player bust", Toast.LENGTH_SHORT).show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            newGameButtons();
                        }
                    }, 4500);

                }
                else if (dealerScore>playerScore && !dealerBusts)
                {
                    updateCashValues();

                    boolean isInserted = db.insertOutcome(username, "Blackjack", "LOSS", currentDateTimeString);
                    if (isInserted==false)
                    {
                        Toast.makeText(getApplicationContext(), "Game data not inserted in game history", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Game data recorded in history", Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(getApplicationContext(), "dealer wins by score", Toast.LENGTH_SHORT).show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            newGameButtons();
                        }
                    }, 4500);

                }
                break;

            case R.id.ddButton:

                Boolean cashTaken = db.updateCash(cash - bet, username);
                bet*=2;
                betAmount.setText("Bet: $" +bet.toString());
                updateCashValues();

                hitCardNumPlayer +=1;
                doShuffle+=1;
                Integer[] cardPlayerDD = dealCardsRandom();
                Integer newCardPlayerDD = returnCardValue(cardPlayerDD[2]);
                cards[cardPlayerDD[0]][cardPlayerDD[1]] = 0;
                addCard(hitCardNumPlayer, cardPlayerDD[2]);
                playerCards.add(newCardPlayerDD);
                playerScore = sum(playerCards);
                amountAcesPlayer = checkAces(playerCards);

                if (amountAcesPlayer>0 && highAcePlayer && playerScore>21)
                {
                    playerCards.add(-10);
                    highAcePlayer=false;
                }

                if (newCardPlayerDD == 1 && playerScore+10<=21)
                {
                    playerCards.add(10);
                }

                playerScore = sum(playerCards);
                playerValue.setText(playerScore.toString());


                if(playerScore>21)
                {
                    standBustButtons();
                    playerBusts=true;
                    Toast.makeText(getApplicationContext(), "and that's a bust, Sorry", Toast.LENGTH_SHORT).show();

                    boolean isInserted = db.insertOutcome(username, "Blackjack", "LOSS", currentDateTimeString);
                    if (isInserted==false)
                    {
                        Toast.makeText(getApplicationContext(), "Game data not inserted in game history", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Game data recorded in history", Toast.LENGTH_SHORT).show();
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            newGameButtons();
                        }
                    }, 4500);
                }

                else if(playerScore==21)
                {

                    standBustButtons();
                    standButton.performClick();
                    Toast.makeText(getApplicationContext(), "21, good hand", Toast.LENGTH_SHORT).show();
                }

                if (!playerBusts)
                {
                    standBustButtons();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            standButton.performClick();
                        }
                    }, 3000);
                }

                break;
        }
    }

    public Integer[] dealCardsRandom ()
    {
        Random a = new Random();
        Random b = new Random();

        Integer c = a.nextInt(13);
        Integer d = b.nextInt(4);

        Integer cardNumber = cards[c][d];

        if (cardNumber == 0) {
            for (; ; ) {
                c = a.nextInt(13);
                d = b.nextInt(4);
                cardNumber = cards[c][d];
                if (cardNumber != 0) {
                    break;
                }
            }
        }
        Integer[] numbers =  {c, d, cardNumber};
        return (numbers);
    }

    public  Integer returnCardValue(Integer cardNumber)
    {
        Integer cardValue = 0;


        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 4; j++) {
                if (cards[i][j].equals(cardNumber)) {
                    cardValue = i;
                }
            }
        }

        if (cardValue+2 == 11||cardValue+2 ==12||cardValue+2 ==13)
        {
            cardValue=8;
        }
        else if (cardValue+2 == 14)
        {
            cardValue = -1;
        }

        return cardValue+2;
    }

    public void newGameButtons()
    {
        clearButton.setEnabled(true);
        chip1.setEnabled(true);
        chip5.setEnabled(true);
        chip25.setEnabled(true);
        chip50.setEnabled(true);
        chip100.setEnabled(true);
        bjButton.setVisibility(View.VISIBLE);
        hitButton.setVisibility(View.GONE);
        standButton.setVisibility(View.GONE);
        ddButton.setVisibility(View.GONE);
        playerScore = 0;
        dealerScore = 0;
        aceExistsPlayer = false;
        aceExistsDealer = false;
        highAcePlayer = false;
        highAceDealer = false;
        dealerStands = false;
        dealerBusts = false;
        playerBusts = false;

        playerCards.clear();
        dealerCards.clear();

        playerCardsLayout.removeAllViews();
        dealerCardsLayout.removeAllViews();

        bet=0.00;
        betAmount.setText("Bet: $" + bet);
        dealerScore = sum(dealerCards);
        dealerValue.setText(dealerScore.toString());
        playerScore = sum(dealerCards);
        playerValue.setText(dealerScore.toString());

        amountAcesPlayer=0;

    }

    public void afterStartButtons()
    {
        clearButton.setEnabled(false);
        chip1.setEnabled(false);
        chip5.setEnabled(false);
        chip25.setEnabled(false);
        chip50.setEnabled(false);
        chip100.setEnabled(false);
        hitButton.setEnabled(true);
        standButton.setEnabled(true);
        ddButton.setEnabled(true);

        bjButton.setVisibility(View.GONE);
        hitButton.setVisibility(View.VISIBLE);
        standButton.setVisibility(View.VISIBLE);
        ddButton.setVisibility(View.VISIBLE);
    }

    public void standBustButtons()
    {
        bjButton.setVisibility(View.GONE);
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        ddButton.setEnabled(false);
    }

    public void updateCashValues()
    {
        SharedPreferences sharedPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        TextView cashTxt = (TextView)
                findViewById(R.id.txtCash);
        String username = sharedPrefs.getString("username", "");
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

    public void addCard(int hitCardNum, Integer image)
    {
        ImageView newCard = new ImageView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imagePlayerCard1.getWidth(), imagePlayerCard1.getHeight());
        layoutParams.setMargins(imagePlayerCard2.getLeft() * hitCardNum,0,0,0);
        newCard.setBackgroundResource(image);
        newCard.setLayoutParams(layoutParams);
        newCard.requestLayout();
        playerCardsLayout.addView(newCard);
    }

    public void addCardDealer(int hitCardNum, Integer image)
    {
        ImageView newCard = new ImageView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imageDealerCard1.getWidth(), imageDealerCard1.getHeight());
        layoutParams.setMargins(imageDealerCard2.getLeft() * hitCardNum,0,0,0);
        newCard.setBackgroundResource(image);
        newCard.setLayoutParams(layoutParams);
        newCard.requestLayout();
        dealerCardsLayout.addView(newCard);
    }

    public static int sum (List<Integer> list) {
        int i;
        Integer sum = 0;
        for(i = 0; i < list.size(); i++)
        {
            sum += list.get(i);
        }
        return sum;
    }

    public Integer checkAces(List<Integer> list)
    {
        Integer amountAces = 0;
        for(int i = 0; i < list.size(); i++)
        {
            if (list.get(i) == 1 || list.get(i) == 11)
            {
                amountAces+=1;
            }
        }

        return amountAces;
    }

    public Integer dealerHit()
    {
        Integer[] cardDealer = dealCardsRandom();
        Integer newCardDealer = returnCardValue(cardDealer[2]);
        cards[cardDealer[0]][cardDealer[1]] = 0;
        addCardDealer(hitCardNumDealer, cardDealer[2]);
        dealerCards.add(newCardDealer);
        dealerScore = sum(dealerCards);
        Integer amountAcesDealer = checkAces(dealerCards);

        if (amountAcesDealer>0 && highAceDealer && dealerScore>21)
        {
            dealerCards.add(-10);
            highAcePlayer=false;
        }

        if (newCardDealer == 1 && dealerScore+10<=21)
        {
            dealerCards.add(10);
        }
        dealerScore = sum(dealerCards);
        dealerValue.setText(dealerScore.toString());

        return dealerScore;
    }
    public void doShuffle()
    {
        Integer[][] cardsA = {{R.drawable.card1, R.drawable.card2, R.drawable.card3, R.drawable.card4},
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
        cards = cardsA;
        doShuffle=0;
        Toast.makeText(getApplicationContext(), "Shuffling...", Toast.LENGTH_SHORT).show();
    }
}
