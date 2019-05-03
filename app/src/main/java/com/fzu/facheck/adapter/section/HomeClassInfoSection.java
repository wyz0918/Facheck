package com.fzu.facheck.adapter.section;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fzu.facheck.R;
import com.fzu.facheck.widget.sectioned.StatelessSection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeClassInfoSection extends StatelessSection {

    public HomeClassInfoSection(){
        super(R.layout.layout_home_class_header,R.layout.layout_per_info);
    }
    @Override
    public int getContentItemsTotal() {
        return 0;
    }


    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }
    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HomeClassSection.HeaderViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder=(ItemViewHolder)holder;
        itemViewHolder.LeftnameText.setText("");
        itemViewHolder.RightnameText.setText("");
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder=(HeaderViewHolder)holder;
        ((HeaderViewHolder) holder).tileText.setText("总人数："+"");
    }


    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_class)
        TextView tileText;


        HeaderViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.left_name)
        TextView LeftnameText;
        @BindView(R.id.right_name)
        TextView RightnameText;
        ItemViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
