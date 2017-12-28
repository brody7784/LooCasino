package com.gartmedia.brody.loocasino;

/**
 * Created by brody on 2017-12-04.
 */

public class gamehistoryobject {
    public String gameName;
    public String outcome;
    public String date;

    public gamehistoryobject(String gameName, String outcome, String date)
    {
        this.gameName = gameName;
        this.outcome = outcome;
        this.date = date;
    }
}
