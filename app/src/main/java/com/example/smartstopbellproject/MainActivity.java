package com.example.smartstopbellproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button rezbtn = findViewById(R.id.rezbtn);

//        //카드 태그 시 태그요청이미지 GONE
//        ImageView cardtag = findViewById(R.id.cardtag);
//
//        cardtag.setVisibility(View.GONE);

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