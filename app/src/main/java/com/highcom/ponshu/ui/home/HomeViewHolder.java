package com.highcom.ponshu.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.highcom.ponshu.R;

import org.jetbrains.annotations.NotNull;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    ImageView mMyItemImage;
    TextView mMyItemText;

    public HomeViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        mMyItemImage = (ImageView) itemView.findViewById(R.id.myitem_image_view);
        mMyItemText = (TextView) itemView.findViewById(R.id.myitem_text_view);
    }

    public void bind(String name) {
        mMyItemImage.setImageResource(R.drawable.ic_ponshu);
        mMyItemText.setText(name);
    }
}
