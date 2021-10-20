package com.highcom.ponshu.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.highcom.ponshu.R;
import com.highcom.ponshu.databinding.FragmentHomeBinding;
import com.highcom.ponshu.datamodel.BrandIdentifier;
import com.highcom.ponshu.ui.detailitem.ItemDetailFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements HomeListAdapter.HomeListAdapterListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView recyclerView = binding.myitemRecyclerView;
        HomeListAdapter homeListAdapter = new HomeListAdapter(new HomeListAdapter.HomeListDiff(), this::onAdapterClicked);
        recyclerView.setAdapter(homeListAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, RecyclerView.VERTICAL, false));

        homeViewModel.getBrandIdentifierList().observe(getViewLifecycleOwner(), new Observer<List<BrandIdentifier>>() {
            @Override
            public void onChanged(List<BrandIdentifier> brandIdentifiers) {
                homeListAdapter.submitList(brandIdentifiers);
            }
        });

        binding.addFab.setOnClickListener(v -> {
            ItemDetailFragment fragment = new ItemDetailFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, fragment).commit();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAdapterClicked(String id) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putString("BRAND_ID", id);
        fragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, fragment).commit();
    }
}