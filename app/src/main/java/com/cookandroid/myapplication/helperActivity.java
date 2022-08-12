package com.cookandroid.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class helperActivity extends AppCompatActivity {
    int chPlace =0;
    String places ="선택하세요";
    String kinds="선택하세요";
    TextView hour ,startP , endP, kind;


    @Override protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        setContentView(R.layout.helper);
        LinearLayout end = findViewById(R.id.endShow);
        Intent intent = getIntent();
        int kind=intent.getIntExtra("first",0);
        if(kind==3){
            end.setVisibility(View.VISIBLE);
        }
        final TextView dp = findViewById(R.id.datePickers);
        final DatePicker DP=findViewById(R.id.calendarView1);
        dp.setText(DP.getYear()+"/"+(DP.getMonth()+1)+"/"+DP.getDayOfMonth());
        DP.init(DP.getYear(), DP.getMonth(), DP.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dp.setText(DP.getYear()+"/"+(DP.getMonth()+1)+"/"+DP.getDayOfMonth());
            }
        });
    }
    public void onClick(View v){
        hour =findViewById(R.id.hourPicker);
        startP =findViewById(R.id.startPicker);
        endP= findViewById(R.id.endPicker);
        kind = findViewById(R.id.ServiceKinds);
        LinearLayout BasicLayout = (LinearLayout)findViewById(R.id.basicLayout);
        LinearLayout MainLayout = (LinearLayout)findViewById(R.id.main);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (v.getId()){
            case R.id.cancle:
                finish();
                break;

            case R.id.hourPicker:                //시간정보 입력

                Display display = getWindowManager().getDefaultDisplay();
                final Point resize = new Point();
                display.getSize(resize);
                final Dialog signView = new Dialog(helperActivity.this);
                signView.setContentView(R.layout.hours);

                Window window = signView.getWindow();
                int x = (int)(resize.x *0.99f);
                int y = (int)(resize.y*0.8f);
                Button btn = signView.findViewById(R.id.checking1);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText hs=signView.findViewById(R.id.inputHour);
                        String h =hs.getText().toString();
                        hour.setText(h+"시");
                        signView.dismiss();
                    }
                });
                window.setLayout(x,y);

                signView.show();
                break;
            case R.id.startPicker:               //장소 혹은 출발 지역 정보
                chPlace=1;
                Display displays = getWindowManager().getDefaultDisplay();
                final Point resizes = new Point();
                displays.getSize(resizes);
                final Dialog signViews = new Dialog(helperActivity.this);
                signViews.setContentView(R.layout.bulidingname);

                Window windows = signViews.getWindow();
                int xs = (int)(resizes.x *0.99f);
                int ys = (int)(resizes.y*0.8f);
                Button btns = signViews.findViewById(R.id.checking);
                btns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText hs=signViews.findViewById(R.id.inputPlace);
                        String h =hs.getText().toString();
                        startP.setText(h+"에서");
                        signViews.dismiss();
                    }
                });
                windows.setLayout(xs,ys);

                signViews.show();
                break;
            case R.id.endPicker:           //이동지원서비스시. 도착지역 정보
                chPlace=2;
                Display displaye = getWindowManager().getDefaultDisplay();
                final Point resizee = new Point();
                displaye.getSize(resizee);
                final Dialog signViewe = new Dialog(helperActivity.this);
                signViewe.setContentView(R.layout.bulidingname);

                Window windowe = signViewe.getWindow();
                int xe = (int)(resizee.x *0.99f);
                int ye = (int)(resizee.y*0.8f);
                Button btne = signViewe.findViewById(R.id.checking);
                btne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText hs=signViewe.findViewById(R.id.inputPlace);
                        String h =hs.getText().toString();
                        endP.setText(h+"로");
                        signViewe.dismiss();
                    }
                });
                windowe.setLayout(xe,ye);

                signViewe.show();
                break;
            case R.id.ServiceKinds:               //요청한 서비스의 정보
                Display displayk = getWindowManager().getDefaultDisplay();
                final Point resizek = new Point();
                displayk.getSize(resizek);
                final Dialog signViewk = new Dialog(helperActivity.this);
                signViewk.setContentView(R.layout.servicekind);

                Window windowk = signViewk.getWindow();
                int xk = (int)(resizek.x *0.99f);
                int ky = (int)(resizek.y*0.5f);
                Button btnk = signViewk.findViewById(R.id.checkingS);
                btnk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText hs=signViewk.findViewById(R.id.inputService);
                        String h =hs.getText().toString();
                        kind.setText(h+"서비스");
                        signViewk.dismiss();
                    }
                });
                windowk.setLayout(xk,ky);

                signViewk.show();

                break;
            case R.id.reservation:
                //저거다 서버저.자..ㅇ..ㅎㅎㅅㅎㅅㅎㅅㅎ  화이팅 난 최선을 다햏ㅆ어.. 이런거 읽으면 지워주고 ㅠ
                Toast.makeText(helperActivity.this,"예약완료",Toast.LENGTH_SHORT).show();
                finish();
                break;


            }
        }
    }
