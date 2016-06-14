package com.learningstarz.myflashcards.ui.components.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.learningstarz.myflashcards.types.Deck;
import com.learningstarz.myflashcards.ui.fragments.StudyCardFragment;

/**
 * Created by ZahARin on 02.06.2016.
 */
public class StudyCardsAdapter extends FragmentPagerAdapter {

    private Deck deck;
    int pagesCount;

    public StudyCardsAdapter(FragmentManager fm, Deck d) {
        super(fm);
        deck = d;
        pagesCount = deck.getCardsCount();

    }

    @Override
    public Fragment getItem(int position) {
        return StudyCardFragment.newInstance(position, deck);
    }

    @Override
    public int getCount() {
        return pagesCount;
    }
}
