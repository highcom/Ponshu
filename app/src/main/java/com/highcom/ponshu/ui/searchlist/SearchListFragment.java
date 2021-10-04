package com.highcom.ponshu.ui.searchlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.highcom.ponshu.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SearchListFragment extends Fragment {
    private List<String> mBrandsList;
    public SearchListFragment(List<String> brandsList) {
        mBrandsList = brandsList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = getActivity().findViewById(R.id.search_list_view);
        if (mBrandsList != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mBrandsList);
            listView.setAdapter(arrayAdapter);
        }
    }
}