package com.highcom.ponshu.ui.timeline;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.highcom.ponshu.R;

public class TimelineViewHolder extends RecyclerView.ViewHolder {
    private String mId;
    private ImageView mBrandImage;
    private TextView mBrandTitle;
    private TextView mSubBrandTitle;

    public TimelineViewHolder(@NonNull View itemView) {
        super(itemView);
        mBrandImage = itemView.findViewById(R.id.brand_image_view);
        mBrandTitle = itemView.findViewById(R.id.brand_text_view);
        mSubBrandTitle = itemView.findViewById(R.id.sub_brand_text_view);
    }

    public void bind(String id, String brand, String subBrand) {
        mId = id;
        mBrandImage.setImageResource(R.drawable.ic_ponshu);
        mBrandTitle.setText(brand);
        mSubBrandTitle.setText(subBrand);
    }
}
