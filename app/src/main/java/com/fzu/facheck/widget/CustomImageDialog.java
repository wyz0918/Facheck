package com.fzu.facheck.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fzu.facheck.R;
import com.fzu.facheck.utils.GlideUtil;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import butterknife.BindView;

/**
 * @date: 2019/4/23
 * @author: wyz
 * @version:
 * @description: 自定义ImageDialog
 */
public class CustomImageDialog extends Dialog {

    private ImageView image;
    private File file;
    private Context context;

    public CustomImageDialog(Context context, File file) {
        super(context, R.style.Translucent_NoTitle);
        this.file = file;
        this.context = context;



    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.image_dialog_layout);
        image = (ImageView)findViewById(R.id.image);
        Glide.with(context)
                .load(file)
                .priority(Priority.HIGH)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomImageDialog.this.dismiss();
            }
        });
        setCanceledOnTouchOutside(true);



    }


}
