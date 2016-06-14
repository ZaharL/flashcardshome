package com.learningstarz.myflashcards.ui.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.learningstarz.myflashcards.R;
import com.learningstarz.myflashcards.tools.Tools;
import com.learningstarz.myflashcards.types.Deck;
import com.learningstarz.myflashcards.ui.activities.StudyActivity;
import com.learningstarz.myflashcards.ui.components.adapters.StudyCardsAdapter;

/**
 * Created by ZahARin on 02.06.2016.
 */
public class StudyCardFragment extends Fragment {

    public static final String PAGE = "page";

    private static int page;
    private Deck deck;
    private static boolean mShowingBack = false;

    StudyActivity c;

    public static StudyCardFragment newInstance(int page, Deck deck) {
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        args.putParcelable(Tools.deckExtraTag, deck);
        StudyCardFragment fragment = new StudyCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        page = b.getInt(PAGE);
        deck = b.getParcelable(Tools.deckExtraTag);

        c = (StudyActivity) getContext();

        c.findViewById(R.id.StudyActivity_btnFlip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipCard();
            }
        });
        ((TextView) c.findViewById(R.id.StudyActivity_etCurrentCardNum)).setText(String.valueOf(page));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.card_flip_layout, container, false);

        if (savedInstanceState == null) {
            this.getChildFragmentManager()
                .beginTransaction()
                .add(R.id.CardFlipLayout_container, StudyCardFrontFragment.newInstance(deck.getCard(page), deck))
                .commit();
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((TextView) c.findViewById(R.id.StudyActivity_etCurrentCardNum)).setText(String.valueOf(page));
    }

    private void flipCard() {

        Log.d("MyLogs", "FLIP!");
        if (mShowingBack) {
            this.getChildFragmentManager().popBackStack();
            mShowingBack = false;
            return;
        }

        mShowingBack = true;

        this.getChildFragmentManager()
                .beginTransaction()
//              .setCustomAnimations(
//                        R.anim.card_flip_left_in,
//                        R.anim.card_flip_left_out,
//                        R.anim.card_flip_right_in,
//                        R.anim.card_flip_right_out)
                .replace(R.id.CardFlipLayout_container, StudyCardBackFragment.newInstance(deck.getCard(page), deck))
                .addToBackStack(null)
                .commit();

    }
}
