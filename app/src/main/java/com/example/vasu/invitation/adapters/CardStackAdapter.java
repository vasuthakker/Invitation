package com.example.vasu.invitation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vasu.invitation.R;


/**
 * Created by Aradh Pillaion 07/10/15.
 */

/*
* cardStackAdapter which is going to hold list of information and setting it into CardStack
*
* */
public class CardStackAdapter extends BaseAdapter {

    private Context context;
    private int[] imgResource = {R.drawable.one, R.drawable.two, R.drawable.three};
    private LayoutInflater inflater;

    public CardStackAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        if (contentView == null)
            contentView = inflater.inflate(R.layout.card_stack_item, parent, false);

        ImageView imgView = (ImageView) contentView.findViewById(R.id.carditem_img);
        TextView txtView = (TextView) contentView.findViewById(R.id.carditem_txt);


        imgView.setImageResource(imgResource[position % 3]);

        return contentView;
    }

}
