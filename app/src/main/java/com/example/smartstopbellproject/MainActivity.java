package com.example.smartstopbellproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rezbtn = findViewById(R.id.rezbtn);

        /*
        if(카드 태그됨) {
            CustomDialog.getInstance(this).dismiss();
        } else {
            CustomDialog.getInstance(this).showDefaultDialog();
        }
        */

        //RouteActivity에서 값 가져오기
        try {
            Intent intent = getIntent();
            String route = intent.getExtras().getString("route"); //선택한 목적지 가져옴
            rezbtn.setText(route);
        }catch (Exception e){
            //ignore
        }


        //하차벨 클릭 시
        ImageButton bell = findViewById(R.id.bell);
        bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bell.setImageResource(R.drawable.bell_after);
            }
        });

        rezbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //예약버튼 클릭 시 노선도 화면으로 전환
                Intent intent = new Intent(getApplicationContext(), RouteActivity.class);
                startActivity(intent);

            }
        });


    }
}