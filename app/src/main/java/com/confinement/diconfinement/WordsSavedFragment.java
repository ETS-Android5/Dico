package com.confinement.diconfinement;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;

public class WordsSavedFragment extends Fragment {
    Integer index, top;
    ListView listView = null;
    public WordsSavedFragment() {
        super(R.layout.savedwords_list);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        listView = getActivity().findViewById(R.id.savedWords_list);
        displaySavedWords(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = listView.getItemAtPosition(position);
                //Save list index and top when exiting activity
                index = listView.getFirstVisiblePosition();
                View v = listView.getChildAt(0);
                top = (v == null) ? 0 : (v.getTop() - listView.getPaddingTop());
                SpannableString savedWord = (SpannableString) obj;
                if (savedWord != null) {
                    Intent intent = FileUtils.createSearchIntent(savedWord, position);
                    startActivity(intent);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
         //Set saved list position when returning to activity
         if (index != null && top != null){
            listView.setSelectionFromTop(index, top);
         }
        DisplayUtils.changeFragmentTitle(getActivity(), Globals.saved_words, getActivity().getResources());
        displaySavedWords(listView);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if (toolbar != null && toolbar.getMenu() != null && toolbar.getMenu().findItem(R.id.help) != null) {
            toolbar.getMenu().findItem(R.id.help).setVisible(false);
        }
        super.onResume();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void displaySavedWords(ListView listView) {
        ArrayList<String> savedWords = FileUtils.retrieveSavedWords(getActivity());
        ArrayList<SpannableString> savedWordsSorted = FileUtils.sortAndConvertToSpannableList(savedWords);
        listView.setAdapter(new WordsSavedAdapter(getActivity(), savedWordsSorted));
    }



}
