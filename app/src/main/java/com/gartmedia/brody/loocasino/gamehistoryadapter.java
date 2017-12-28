package com.gartmedia.brody.loocasino;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by brody on 2017-11-30.
 */

public class gamehistoryadapter extends ArrayAdapter<gamehistoryobject>
{

    public gamehistoryadapter(Context context, ArrayList<gamehistoryobject> gamehistoryobject, int flag)
    {
        super(context, flag, gamehistoryobject);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        gamehistoryobject historyobject = getItem(position);

        if(convertView== null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.gamehistory, parent, false);
        }

        TextView txtGameName = (TextView) convertView.findViewById(R.id.txtGame);
        TextView txtOutcome = (TextView) convertView.findViewById(R.id.txtOutcome);
        TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);

        txtGameName.setText(historyobject.gameName);
        txtOutcome.setText(historyobject.outcome);
        txtDate.setText(historyobject.date);

        return convertView;
    }
}
