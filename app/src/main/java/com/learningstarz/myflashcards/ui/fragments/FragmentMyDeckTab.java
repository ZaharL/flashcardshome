package com.learningstarz.myflashcards.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.learningstarz.myflashcards.R;

/**
 * Created by ZahARin on 20.01.2016.
 */
public class FragmentMyDeckTab extends Fragment {
    public static final String PAGE = "PAGE";

    private int mPage;

    public static FragmentMyDeckTab newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(PAGE, page);
        FragmentMyDeckTab fragment = new FragmentMyDeckTab();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_deck_tab, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tvSome);
        tv.setText("SOME CHANGED TEXT");
        return view;
    }
}
