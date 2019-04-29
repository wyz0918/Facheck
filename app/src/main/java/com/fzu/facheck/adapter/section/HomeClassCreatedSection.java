package com.fzu.facheck.adapter.section;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fzu.facheck.R;
import com.fzu.facheck.entity.RollCall.RollCallInfo;
import com.fzu.facheck.widget.sectioned.StatelessSection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeClassCreatedSection extends StatelessSection {
    private Context mContext;
    private List<RollCallInfo.ClassInfoBean.ManagedClassDataBean> datas;




    public HomeClassCreatedSection(Context context, List<RollCallInfo.ClassInfoBean.ManagedClassDataBean> datas) {
        super(R.layout.layout_home_class, R.layout.layout_home_class_header);
        this.mContext = context;
        this.datas = datas;
    }



    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        final RollCallInfo.ClassInfoBean.ManagedClassDataBean bodyBean = datas.get(position);
        itemViewHolder.mClassName.setText(bodyBean.getManagedClassName());
        itemViewHolder.mClassDuration.setText(bodyBean.getManagedClassName());
        if(bodyBean.isAbleRollCall()){
            itemViewHolder.mBtn.setText(R.string.roll_call_on);
            itemViewHolder.mBtn.setBackgroundResource(R.drawable.btn_rollcall_gray);


        }else{
            itemViewHolder.mBtn.setText(R.string.roll_call_off);
            itemViewHolder.mBtn.setBackgroundResource(R.drawable.btn_rollcall_green);

        }
    }


    @Override
    public int getContentItemsTotal() {
        return 1;
    }



    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {

    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.class_name)
        TextView mClassName;
        @BindView(R.id.class_duration)
        TextView mClassDuration;
        @BindView(R.id.item_btn_rollcall)
        Button mBtn;



        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
