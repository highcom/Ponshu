package com.highcom.ponshu.ui.timeline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.highcom.ponshu.R;
import com.highcom.ponshu.datamodel.BrandIdentifier;

public class TimelineListAdapter extends ListAdapter<BrandIdentifier, TimelineViewHolder> {
    public interface TimelineListAdapterListener {
        void onAdapterClicked(String id);
    }
    TimelineListAdapterListener mTimelineListAdapterListener;

    protected TimelineListAdapter(@NonNull DiffUtil.ItemCallback<BrandIdentifier> diffCallback, TimelineListAdapterListener listener) {
        super(diffCallback);
        mTimelineListAdapterListener = listener;
    }

    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_branditem, parent, false);
        return new TimelineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {
        String id = getItem(position).getId();
        String name = getItem(position).getTitle();
        holder.bind(id, name, "サブ銘柄名");
        holder.itemView.setOnClickListener(v -> mTimelineListAdapterListener.onAdapterClicked(id));
    }

    public static class TimelineDiff extends DiffUtil.ItemCallback<BrandIdentifier> {

        @Override
        public boolean areItemsTheSame(@NonNull BrandIdentifier oldItem, @NonNull BrandIdentifier newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull BrandIdentifier oldItem, @NonNull BrandIdentifier newItem) {
            return false;
        }
    }
}
