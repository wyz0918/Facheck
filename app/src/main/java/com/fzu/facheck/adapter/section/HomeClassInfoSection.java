package com.fzu.facheck.adapter.section;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fzu.facheck.R;
import com.fzu.facheck.entity.RollCall.ClassInfo;
import com.fzu.facheck.widget.sectioned.StatelessSection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeClassInfoSection extends StatelessSection {

    private static final String STUDENT = "student";
    private static final String RECORDS = "records";
    private List<ClassInfo.RecordSignInfo.Student> students;
    private List<ClassInfo.RecordSignInfo.Record> records;
    private String type;
    private Context mContext;


    public HomeClassInfoSection(ClassInfo classInfo,String type,Context context){
        super(R.layout.layout_home_class_header,R.layout.layout_per_info,R.layout.layout_per_info);
        students=classInfo.getRecordsInfo().getStudents();
        records=classInfo.getRecordsInfo().getRecord();
        this.type=type;
        this.mContext=context;
    }
    @Override
    public int getContentItemsTotal() {
        switch (type){
            case STUDENT:
                return students.size();
            case RECORDS:
                return records.size();
            default:
                return 1;
        }
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
    public RecyclerView.ViewHolder getFooterViewHolder(View view) {
        switch (type){
            case STUDENT:
                if(students.size()!=0)
                    return null;
                else
                    return new ItemViewHolder(view);
            case RECORDS:
                if(records.size()!=0)
                    return null;
                else
                    return  new ItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder=(ItemViewHolder)holder;
        switch (this.type){
            case STUDENT:
                final ClassInfo.RecordSignInfo.Student student=students.get(position);
                itemViewHolder.LeftnameText.setText(student.getName());
                break;
            case RECORDS:
                final ClassInfo.RecordSignInfo.Record record=records.get(position);
                itemViewHolder.LeftnameText.setText(record.getTime());
                itemViewHolder.RightnameText.setText(record.getAttendratio());
                break;
        }
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder=(HeaderViewHolder)holder;
        switch (this.type){
            case STUDENT:
                headerViewHolder.tileText.setText("总人数："+students.size()+"人");
                break;
            case RECORDS:
                headerViewHolder.tileText.setText("点名记录");
                break;
        }
    }
    @Override
    public void onBindFooterViewHolder(RecyclerView.ViewHolder holder) {
        if(holder!=null){
            ItemViewHolder footerViewHolder=(ItemViewHolder) holder;
            switch (this.type){
                case STUDENT:
                    footerViewHolder.LeftnameText.setText("请让学生通过班级号加入班级");
                    break;
                case RECORDS:
                    footerViewHolder.LeftnameText.setText("暂无！");
                    break;
            }
        }
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
