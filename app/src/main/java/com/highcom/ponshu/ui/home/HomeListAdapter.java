package com.highcom.ponshu.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.highcom.ponshu.R;
import com.highcom.ponshu.datamodel.BrandIdentifier;

import org.jetbrains.annotations.NotNull;

public class HomeListAdapter extends ListAdapter<BrandIdentifier, HomeViewHolder> {
    public interface HomeListAdapterListener {
        void onAdapterClicked(String name);
    }
    HomeListAdapterListener mHomeListAdapterListener;

    protected HomeListAdapter(@NonNull @NotNull DiffUtil.ItemCallback<BrandIdentifier> diffCallback, HomeListAdapterListener listener) {
        super(diffCallback);
        mHomeListAdapterListener = listener;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_myitem, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        String id = getItem(position).getId();
        String name = getItem(position).getTitle();
        holder.bind(id, name);
        holder.itemView.setOnClickListener(v -> mHomeListAdapterListener.onAdapterClicked(id));
    }

    public static class HomeListDiff extends DiffUtil.ItemCallback<BrandIdentifier> {

        @Override
        public boolean areItemsTheSame(@NonNull @NotNull BrandIdentifier oldItem, @NonNull @NotNull BrandIdentifier newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull BrandIdentifier oldItem, @NonNull @NotNull BrandIdentifier newItem) {
            return false;
        }
    }
}
