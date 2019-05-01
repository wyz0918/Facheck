package com.fzu.facheck.adapter.section;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fzu.facheck.R;
import com.fzu.facheck.entity.RollCall.RollCallInfo;
import com.fzu.facheck.widget.sectioned.StatelessSection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeClassSection extends StatelessSection {
    private List<RollCallInfo.ClassInfoBean.JoinedClassDataBean> jointedData;
    private List<RollCallInfo.ClassInfoBean.ManagedClassDataBean> createdData;
    private String type;
    private static final String JOINTED_DATA = "jointed_data";
    private static final String CREATE_DATA = "created_data";




    public HomeClassSection( RollCallInfo.ClassInfoBean data,String type) {
        super(R.layout.layout_home_class_header, R.layout.layout_home_class);
        this.jointedData = data.getJoinedClassData();
        this.createdData = data.getManagedClassData();
        this.type = type;
    }



    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;


        switch (type){
            case JOINTED_DATA:
                final RollCallInfo.ClassInfoBean.JoinedClassDataBean joinedClassDataBean = jointedData.get(position);
                itemViewHolder.mClassName.setText(joinedClassDataBean.getJoinedClassName());
                itemViewHolder.mClassDuration.setText(joinedClassDataBean.getJoinedClassTime());
                if(joinedClassDataBean.isSignable()){
                    itemViewHolder.mBtn.setText(R.string.not_signed_in);
                    itemViewHolder.mBtn.setBackgroundResource(R.drawable.btn_rollcall_gray);


                }else{
                    itemViewHolder.mBtn.setText(R.string.signed_in);
                    itemViewHolder.mBtn.setBackgroundResource(R.drawable.btn_rollcall_green);

                }
                break;
            case CREATE_DATA:
                final RollCallInfo.ClassInfoBean.ManagedClassDataBean managedClassData = createdData.get(position);
                itemViewHolder.mClassName.setText(managedClassData.getManagedClassName());
                itemViewHolder.mClassDuration.setText(managedClassData.getManagedClassTime());
                if(managedClassData.isAbleRollCall()){
                    itemViewHolder.mBtn.setText(R.string.roll_call_on);
                    itemViewHolder.mBtn.setBackgroundResource(R.drawable.btn_rollcall_gray);


                }else{
                    itemViewHolder.mBtn.setText(R.string.roll_call_off);
                    itemViewHolder.mBtn.setBackgroundResource(R.drawable.btn_rollcall_green);

                }
                break;

        }




    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {

        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

        switch (type){
            case JOINTED_DATA:
                headerViewHolder.tileClass.setText(R.string.class_I_jointed);
                break;
            case CREATE_DATA:
                headerViewHolder.tileClass.setText(R.string.class_I_created);
                break;

        }
    }




    @Override
    public int getContentItemsTotal() {
        switch (type) {
            case JOINTED_DATA:
                return jointedData.size();
            case CREATE_DATA:
                return createdData.size();
            default:
                return 1;
        }
    }





    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.class_name)
        TextView mClassName;
        @BindView(R.id.class_duration)
        TextView mClassDuration;
        @BindView(R.id.item_btn_rollcall)
        TextView mBtn;



        ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_class)
        TextView tileClass;


        HeaderViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }


}
