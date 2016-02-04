package com.learningstarz.myflashcards.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.Types.Deck;
import com.learningstarz.myflashcards.ui.components.adapters.MyDeckCardAdapter;

import java.util.ArrayList;

/**
 * Created by ZahARin on 20.01.2016.
 */
public class FragmentMyDeckTab extends Fragment {
    public static final String PAGE = "PAGE";
    public static final String DECKS = "dcks";

    private int mPage;
    private ArrayList<Deck> decks;

    public static FragmentMyDeckTab newInstance(int page, ArrayList<Deck> decks) {
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        args.putParcelableArrayList(DECKS, decks);
        FragmentMyDeckTab fragment = new FragmentMyDeckTab();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(PAGE);
        decks = getArguments().getParcelableArrayList(DECKS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_deck_tab, container, false);
//        final Animation fabAnimOpen = new AnimationUtils().loadAnimation(getContext(), R.anim.fab_anim_open);
//        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.MyDeckFragmentTab_fab);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.MyDeckActivity_recyclerView);
        rv.hasFixedSize();
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        rv.setAdapter(new MyDeckCardAdapter(getContext(), decks));
        return view;
    }
}
