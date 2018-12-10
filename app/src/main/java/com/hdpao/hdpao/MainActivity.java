package com.hdpao.hdpao;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DecimalFormat;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {
    Button btn_submit;
    EditText fieldheight;
    EditText fieldweight;
    TextView view_result;
    TextView view_fieldsuggest;

    protected static final int MENU_ABOUT= Menu.FIRST;
    protected static final int MENU_QUIT= Menu.FIRST+1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0,MENU_ABOUT, 0, "关于")
        .setIcon(R.drawable.evernote);
        //.setIcon(R.drawable.common_full_open_on_phone)
        //.setAlphabeticShortcut('c');
        menu.add(0,MENU_QUIT,0,"结束");
        return true;//super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case MENU_ABOUT: openOptionsAboutDialog();break;
            case MENU_QUIT: finish();break;
        }
        return true;
    }

    private void findWiews(){
        btn_submit= findViewById(R.id.submit);
        fieldheight=findViewById(R.id.height);
        fieldweight=findViewById(R.id.weigth);
        view_result=findViewById(R.id.result);
        view_fieldsuggest=findViewById(R.id.suggest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fcmTopicInit();

        findWiews();
        setListener();

    }

    private void fcmTopicInit() {
        FirebaseMessaging.getInstance().subscribeToTopic("huangg")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setListener(){
       // btn_submit.setOnClickListener(calcBMI);
        btn_submit.setOnClickListener(report);
    };

    private View.OnClickListener report=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(MainActivity.this,Report.class);
            Bundle bundle=new Bundle();
            bundle.putString("m_height",fieldheight.getText().toString());
            bundle.putString("m_weight",fieldweight.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    private View.OnClickListener calcBMI=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openOptionsAboutDialog();
            DecimalFormat nf=new DecimalFormat("0.00");
            Double bmi;
            try{
                double height=Double.parseDouble(fieldheight.getText().toString());
                double weight=Double.parseDouble(fieldweight.getText().toString());
                bmi=weight/(height* height)*10000;
            }catch (Exception e){
                Toast.makeText(MainActivity.this,"不能为空且只能输入数字哦！",Toast.LENGTH_LONG).show();
                return;
            }

            view_result.setText("Your BMI is "+nf.format(bmi));
            if(bmi>25){
                view_fieldsuggest.setText(R.string.advice_heavy);
            }else if(bmi<20){
                view_fieldsuggest.setText(R.string.advice_ligth);
            }else{
                view_fieldsuggest.setText(R.string.advice_average);
            }
            openOptionsDialog();
        }
    };

    private void openOptionsAboutDialog(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.about_title)
                .setMessage(R.string.about_msg)
                .setPositiveButton(R.string.ok_lable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.homepage_lable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            Uri uri=Uri.parse(getString( R.string.homepage_url));
                            //Uri uri=Uri.parse("geo: 39.895874, 116.321238");
                            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                            startActivity(intent);
                        }catch (Exception e){

                        }
                    }
                })
                .show();
    }

    private void openOptionsDialog(){
        Toast.makeText(MainActivity.this,"BMI 计算器",Toast.LENGTH_SHORT).show();

       /* new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.about_title)
                .setMessage(R.string.about_msg)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // dialog.cancel();
                    }
                })
                .show();*/

    }
}
