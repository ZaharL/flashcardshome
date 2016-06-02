package com.learningstarz.myflashcards.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learningstarz.myflashcards.R;

/**
 * Created by ZahARin on 02.06.2016.
 */
public class StudyCardFragment extends Fragment {


    public static StudyCardFragment newInstance() {
        Bundle args = new Bundle();
        StudyCardFragment fragment = new StudyCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.card_study, null);

        return v;
    }
}
