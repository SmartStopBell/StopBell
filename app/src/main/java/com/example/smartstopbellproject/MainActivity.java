package com.example.smartstopbellproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    TextView getonBus;
    Button rezbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        rezbtn = findViewById(R.id.rezbtn);
        getonBus = findViewById(R.id.getonBus);


        //카드 태그 창 띄우기
        CustomDialog.getInstance(this).showDefaultDialog();


        //rfid로 받은 버스 정보를 가지고 승차한 버스번호 출력
        databaseReference.child("bus").child("_bus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String busNum = dataSnapshot.getKey();
                    getonBus.setText(busNum);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        DatabaseReference reserve = firebaseDatabase.getReference("reserve");
        
        //하차 예약시 예약 버튼에 목적지 이름 출력
        databaseReference.child("reserve").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("stopname").getValue(String.class)!=null){
                    String goalStop = snapshot.child("stopname").getValue(String.class);
                    rezbtn.setText(goalStop);
                }else {
                    //하차 예약을 안 했을 시 예약 버튼 그대로
                    rezbtn.setText("예약");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

            

        //하차벨 클릭 시(즉시하차)
        ImageButton bell = findViewById(R.id.bell);
        bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bell.setImageResource(R.drawable.bell_after);
            }
        });

        //예약 버트 클릭 시 노선도 화면으로 전환
        rezbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RouteActivity.class);
                startActivity(intent);
            }
        });


    }
}