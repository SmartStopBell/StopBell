package com.example.smartstopbellproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class RouteActivity extends AppCompatActivity {

    ListView listview;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        // Adapter 생성
        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter 달기
        listview = findViewById(R.id.listview);
        listview.setAdapter(adapter);

        // 임시로 데이터 추가해놓음
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.route_start), "첫번째 정류장");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.route1), "두번째 정류장");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.route1), "세번째 정류장");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.route_end), "네번째 정류장");




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
                Toast.makeText(getApplicationContext(), "예약완료",Toast.LENGTH_LONG).show();
            }
        });

        //listview 클릭 이벤트
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                String selectStop = item.getStopName();

                builder.setTitle(selectStop);
                AlertDialog alertDialog = builder.create(); //빌더 사용해서 alertDialog 객체 생성
                alertDialog.show();

            }
        });

        //뒤로가기버튼 동작
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}