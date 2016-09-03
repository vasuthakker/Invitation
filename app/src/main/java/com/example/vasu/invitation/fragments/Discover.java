package com.example.vasu.invitation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vasu.invitation.R;
import com.example.vasu.invitation.adapters.QuoteAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Aradh Pillai on 1/10/15.
 */
public class Discover extends Fragment {

    private String TAG = "fragment_discover";
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        Log.i(TAG, "fragment_discover: onCreateView");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.discover_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<String> quotes = getQuotes();
        recyclerView.setAdapter(new QuoteAdapter(getActivity(),quotes));
    }

    public List<String> getQuotes() {
        List<String> quotes = new ArrayList<>();

        quotes.add("Love is when the other person's happiness is more important than your own.");
        quotes.add("Love isn't something you find. Love is something that finds you.");
        quotes.add("Real love is when a relationship never ends, through positive and nagative times.");
        quotes.add("In relationships we learn how to love each other but in love, we loearn how to stay with each other forever");
        quotes.add("I love your feet because they have wandered over the earth and through the wind and water until they brought you to me.");
        quotes.add("Some people are worth melting for. Like you KINJAL");
        quotes.add("A flower cannot blossom without sunshine, and man cannot live without love.");
        quotes.add("Love is when he gives you a piece of your soul, that you never knew was missing.");
        quotes.add("Love is like a beautiful flower which I may not touch, but whose fragrance makes the garden a place of delight just the same.");
        quotes.add("Love is of all passions the strongest, for it attacks simultaneously the head, the heart, and the sense.");
        quotes.add("Hold my hand and we are halfway there, hold my hand and I’ll take you there.");
        quotes.add("Stolen kisses are always sweetest.");
        quotes.add("Love cures people - both the ones who give it and the ones who receive it.");

        return quotes;
    }
}
