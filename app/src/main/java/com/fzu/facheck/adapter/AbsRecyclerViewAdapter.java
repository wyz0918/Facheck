package com.fzu.facheck.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @date: 2019/4/23
 * @author: wyz
 * @version:
 * @description: RecyclerView的基类
 */
public abstract class AbsRecyclerViewAdapter extends
        RecyclerView.Adapter<AbsRecyclerViewAdapter.ClickableViewHolder> {

    private Context context;
    protected RecyclerView mRecyclerView;
    private List<RecyclerView.OnScrollListener> mListeners = new ArrayList<>();


    public AbsRecyclerViewAdapter(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;

    }



    public interface OnItemClickListener {
        void onItemClick(int position, ClickableViewHolder holder);
    }

    interface OnItemLongClickListener {
        boolean onItemLongClick(int position, ClickableViewHolder holder);
    }

    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }


    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }


    public void bindContext(Context context) {
        this.context = context;
    }


    public Context getContext() {
        return this.context;
    }


    @Override
    public void onBindViewHolder(final ClickableViewHolder holder, final int position) {
        holder.getParentView().setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(position, holder);
            }
        });
        holder.getParentView().setOnLongClickListener(v -> itemLongClickListener != null
                && itemLongClickListener.onItemLongClick(position, holder));
    }


    public static class ClickableViewHolder extends RecyclerView.ViewHolder {
        private View parentView;

        public ClickableViewHolder(View itemView) {
            super(itemView);
            this.parentView = itemView;
        }


        View getParentView() {
            return parentView;
        }


        @SuppressWarnings("unchecked")
        public <T extends View> T $(@IdRes int id) {
            return (T) parentView.findViewById(id);
        }
    }
}