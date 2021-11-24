package com.example.smartstopbellproject;

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


        //CustomDialog.getInstance(this).showDefaultDialog();


        //승차 버스번호 출력
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

            databaseReference.child("reserve").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("stopname").getValue(String.class)!=null){
                        String goalStop = snapshot.child("stopname").getValue(String.class);
                        rezbtn.setText(goalStop);

                    }else {
                        rezbtn.setText("예약");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });





        CustomDialog.getInstance(MainActivity.this).showDefaultDialog();

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