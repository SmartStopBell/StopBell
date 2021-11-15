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

public class RouteActivity extends AppCompatActivity {

    ListView listview;
    ListViewAdapter adapter;
    TextView busNum;
    String selectStop;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    ChildEventListener ChildEventListener;

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
        route.child("2222").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String str = snapshot.child("stopname").getValue(String.class);
                    adapter.addItem(R.drawable.route1, str);
                    busNum.setText(str);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 임시로 데이터 추가해놓음
//        adapter.addItem(R.drawable.route_start, "첫번째 정류장");
//        adapter.addItem(R.drawable.route1, "두번째 정류장");
//        adapter.addItem(R.drawable.route1, "세번째 정류장");
//        adapter.addItem(R.drawable.route_end, "네번째 정류장");

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
                listview.setSelector(R.color.pink);
                Toast.makeText(getApplicationContext(), "예약완료",Toast.LENGTH_LONG).show();

                //db에 예약 정보 추가
                databaseReference.child("reserve").setValue(selectStop);

                //예약취소 버튼 활성화
                btnCancel.setVisibility(View.VISIBLE);

            }
        });

        //listview 클릭 이벤트
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                selectStop = item.getStopName();

                builder.setTitle(selectStop);
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

        //예약취소 하지 않은 경우
//        DatabaseReference reserve = firebaseDatabase.getReference("reserve");
//        if(reserve!= null){
//            listview.setSelector(R.color.pink);
//            btnCancel.setVisibility(View.VISIBLE);
//        }
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()){
                    if (data.child("reserve").exists()){
                        listview.setSelector(R.color.pink);
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