package com.learningstarz.myflashcards.ui.components.adapters;

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

    public StudyCardsAdapter(FragmentManager fm, Deck d) {
        super(fm);
        deck = d;
    }

    @Override
    public Fragment getItem(int position) {
        return StudyCardFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 0;
    }
}
