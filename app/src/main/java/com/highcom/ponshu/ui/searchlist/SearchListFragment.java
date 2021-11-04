package com.highcom.ponshu.ui.searchlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;

import com.highcom.ponshu.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchListFragment extends Fragment implements Filterable {
    private String mTitle;
    private boolean mEditTitleVisible;
    private SearchListFragmentListener mListener;
    private EditText mTitleEditText;
    private ListView mListView;
    private ArrayAdapter<String> mArrayAdapter;
    private List<String> mBrandsList;
    private List<String> orig;
    public SearchListFragment(List<String> brandsList, SearchListFragmentListener listener) {
        mTitle = null;
        mEditTitleVisible = false;
        mBrandsList = brandsList;
        mListener = listener;
    }

    public SearchListFragment(List<String> brandsList, String title, boolean visible, SearchListFragmentListener listener) {
        mTitle = title;
        mEditTitleVisible = visible;
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
        if (mEditTitleVisible) {
            mTitleEditText = getActivity().findViewById(R.id.detail_edit_title);
            mTitleEditText.setVisibility(View.VISIBLE);
            mTitleEditText.setText(mTitle);
            mTitleEditText.setFocusable(true);
            mTitleEditText.setFocusableInTouchMode(true);
            mTitleEditText.requestFocus();
            mTitleEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Filter filter = getFilter();
                    if (TextUtils.isEmpty(s)) {
                        filter.filter(null);
                    } else {
                        filter.filter(s);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
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

    public void setTitle(String title) {
        mTitle = title;
    }
}