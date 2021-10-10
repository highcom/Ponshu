package com.highcom.ponshu.ui.searchlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;

import com.highcom.ponshu.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchListFragment extends Fragment implements Filterable {
    private SearchListFragmentListener mListener;
    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;
    private List<String> mBrandsList;
    private List<String> orig;
    public SearchListFragment(List<String> brandsList, SearchListFragmentListener listener) {
        mBrandsList = brandsList;
        mListener = listener;
    }

    public interface SearchListFragmentListener {
        void onAdapterClicked(String name);
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
        mListView = getActivity().findViewById(R.id.search_list_view);
        if (mBrandsList != null) {
            mArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mBrandsList);
            mListView.setAdapter(mArrayAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String brandName = mArrayAdapter.getItem(position);
                    mListener.onAdapterClicked(brandName);
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<String> results = new ArrayList<>();
                if (orig == null)
                    orig = mBrandsList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final String g : orig) {
                            if (g.toLowerCase().contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                } else {
                    oReturn.values = orig;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                mBrandsList = (ArrayList<String>) results.values;
                mArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mBrandsList);
                mListView.setAdapter(mArrayAdapter);
            }
        };
    }
}