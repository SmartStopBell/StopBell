package com.example.smartstopbellproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class rezcomplicate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezcomplicate);

        Button rezbtn = findViewById(R.id.rezbtn);
        rezbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //예약완료 상태의 노선도화면으로 전환
                Intent intent = new Intent(getApplicationContext(), RouteActivity.class);
                startActivity(intent);

            }
        });
    }
}