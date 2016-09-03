package com.example.vasu.invitation.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vasu.invitation.R;

import java.util.List;

/**
 * Created by ADMIN on 03-Sep-16.
 */
public class QuoteAdapter extends RecyclerView.Adapter{

    private Context conext;
    private List<String> quotes;
    private LayoutInflater inflater;

    public QuoteAdapter(Context conext, List<String> quotes) {
        this.conext = conext;
        this.quotes = quotes;
        inflater=LayoutInflater.from(conext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuoteHolder(inflater.inflate(R.layout.quote_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QuoteHolder quoteHolder= (QuoteHolder) holder;

        quoteHolder.txtQuote.setText(quotes.get(position));
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    private class QuoteHolder extends RecyclerView.ViewHolder
    {
        TextView txtQuote;
        public QuoteHolder(View itemView) {
            super(itemView);

            txtQuote= (TextView) itemView.findViewById(R.id.quoteitem_txt);
        }
    }
}
