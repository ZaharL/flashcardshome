package com.learningstarz.myflashcards.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.tools.DownloadImageTask;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.Card;
import com.learningstarz.myflashcards.types.Deck;

/**
 * Created by ZahARin on 02.06.2016.
 */
public class StudyCardBackFragment extends Fragment {

    private Card card;
    private Deck deck;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.card_study, container, false);

        ((TextView) v.findViewById(R.id.CardStudy_tvCardType)).setText("BACK");
        TextView tvAnswer = (TextView) v.findViewById(R.id.CardStudy_tvContent);
        tvAnswer.setText(card.getAnswer());
        new DownloadImageTask((ImageView) v.findViewById(R.id.CArdStudy_ivContentImage)).execute(String.format(getString(R.string.url_host), card.getImagePath() + card.getImage2URL()));
        ((TextView) v.findViewById(R.id.CardStudy_etAuthor)).setText(deck.getAuthor());
        ((TextView) v.findViewById(R.id.CardStudy_etTitle)).setText(deck.getTitle());

        return v;
    }

    public static StudyCardBackFragment newInstance(Card c, Deck d) {

        Bundle args = new Bundle();
        args.putParcelable(Tools.cardsExtraTag, c);
        args.putParcelable(Tools.deckExtraTag, d);
        StudyCardBackFragment fragment = new StudyCardBackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        card = b.getParcelable(Tools.cardsExtraTag);
        deck = b.getParcelable(Tools.deckExtraTag);
    }
}
