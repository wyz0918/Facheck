package com.fzu.facheck.WorkClass;

import android.os.CountDownTimer;
import android.widget.Button;

public class TimeCount extends CountDownTimer {
    private Button vertifyBtn;
    public TimeCount(long millisInFuture,long countDownIntervval,Button vertifyBtn)
    {
        super(millisInFuture,countDownIntervval);
        this.vertifyBtn=vertifyBtn;
    }
    @Override
    public void onTick(long millisUntilFinished) {
        vertifyBtn.setClickable(false);
        vertifyBtn.setText(millisUntilFinished/1000+"秒");
    }

    @Override
    public void onFinish() {
        vertifyBtn.setText("获取验证码");
        vertifyBtn.setClickable(true);
    }
}
