package com.hdpao.hdpao;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class Report extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        findView();
        showResult();
        setListener();

    }

    Button btn_2main;

    TextView view_result;
    TextView view_fieldsuggest;

    private void findView(){
        btn_2main=findViewById(R.id.btn_2main);
        view_result=findViewById(R.id.result);
        view_fieldsuggest=findViewById(R.id.suggest);
    }

    private void showResult(){
        DecimalFormat nf=new DecimalFormat("0.00");
        Bundle bundle=this.getIntent().getExtras();
        Double bmi;
        try{
            double height=Double.parseDouble(bundle.getString("m_height"));
            double weight=Double.parseDouble(bundle.getString("m_weight"));
            bmi=weight/(height* height)*10000;
        }catch (Exception e){
            Toast.makeText(Report.this,"不能为空且只能输入数字哦！",Toast.LENGTH_LONG).show();
            return;
        }

        view_result.setText("Your BMI is "+nf.format(bmi));
        if(bmi>25){
            view_fieldsuggest.setText(R.string.advice_heavy);
            showNotification(bmi);
        }else if(bmi<20){
            view_fieldsuggest.setText(R.string.advice_ligth);
        }else{
            view_fieldsuggest.setText(R.string.advice_average);
        }
    }

    private void setListener(){
        btn_2main.setOnClickListener(go_2main);
    }

    private View.OnClickListener  go_2main=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent intent=new Intent();
//            intent.setClass(Report.this,MainActivity.class);
//            startActivity(intent);
            Report.this.finish();
        }
    };

    private void showNotification(double BMI){
        NotificationManager notificationManager = (NotificationManager) getSystemService
                (NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        //设置标题
        mBuilder.setContentTitle("哦，你过重了!")
                //设置内容
                .setContentText("您改控制饮食了。")
                //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //设置通知时间
                .setWhen(System.currentTimeMillis())
                //首次进入时显示效果
                .setTicker("我是测试内容")
                //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                .setDefaults(Notification.DEFAULT_SOUND);
        //发送通知请求
        notificationManager.notify(10, mBuilder.build());
    }
}
