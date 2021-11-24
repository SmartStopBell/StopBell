package com.example.smartstopbellproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.HashMap;

public class RouteActivity extends AppCompatActivity {

    ListView listview;
    ListViewAdapter adapter;
    TextView busNum;
    ListViewItem selectedItem;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        busNum = findViewById(R.id.busNum);

        // Adapter 생성
        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter 달기
        listview = findViewById(R.id.listview);
        listview.setAdapter(adapter);

        DatabaseReference route = firebaseDatabase.getReference("busroute");
        DatabaseReference busNumber = firebaseDatabase.getReference("bus");


        //버스 번호 출력
        busNumber.child("_bus").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String busnum = snapshot.getKey();
                    busNum.setText(busnum);

                    route.child(busnum).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String stopId = snapshot.getKey();
                        String stopname = snapshot.child("stopname").getValue(String.class);
                        Integer position = snapshot.child("position").getValue(Integer.class);
                        adapter.addItem(stopId, stopname, position);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /* 정민이꺼
        busNum = findViewById(R.id.busNum);
        busNum.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZZZZZ");
        busNum.setSingleLine();
        busNum.setMarqueeRepeatLimit(-1);
        busNum.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        busNum.setSelected(true);
         */



        //AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(RouteActivity.this);
        builder.setTitle("정류장 이름");
        builder.setMessage("목적지로 예약하시겠습니까?");
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //취소 버튼 클릭시 동작
            }
        });

        builder.setPositiveButton("예약", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //예약 버튼 클릭시 동작
                adapter.setSelectedItem(selectedItem.getPosition());
                Toast.makeText(getApplicationContext(), "예약완료",Toast.LENGTH_LONG).show();

                //db에 예약 정보 추가
                databaseReference.child("reserve").setValue(selectedItem);

                //예약취소 버튼 활성화
                btnCancel.setVisibility(View.VISIBLE);
            }
        });

        //listview 클릭 이벤트
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (btnCancel.getVisibility() == View.VISIBLE) return;

                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                selectedItem = item;

                builder.setTitle(selectedItem.getStopname());
                AlertDialog alertDialog = builder.create(); //빌더 사용해서 alertDialog 객체 생성
                alertDialog.show();//alertDialog창 띄우기
            }
        });

        //예약취소 버튼 클릭 이벤트
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "예약취소",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                //db에서 삭제
                databaseReference.child("reserve").removeValue();
            }
        });

        //뒤로가기버튼 동작
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //뒤로가기 눌렀다와도 안 사라지게 하고싶다..
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    if (data.getKey().equals("reserve")){
                        HashMap hashMap = (HashMap) data.getValue();
                        Integer position = ((Long) hashMap.get("position")).intValue();

                        adapter.setSelectedItem(position);
                        btnCancel.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }
}