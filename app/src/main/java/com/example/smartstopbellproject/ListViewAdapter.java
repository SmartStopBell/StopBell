package com.example.smartstopbellproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    //Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    int selectedPosition = -1;
    private ListViewItem userN;

    //ListViewAdapter 생성자
    public ListViewAdapter(){

    }

    //Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    //지정한 위치(position)에 있는 데이터 리턴
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    //position에 위치한 데이터를 화면에 출력하는 데 사용할 View 리턴
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        //"listview_item" layout을 inflate하여 convertView 참조 획득
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        //화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.img);
        TextView StopTextView = (TextView) convertView.findViewById(R.id.tvStop);

        //Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        //아이템 내 각 위젯에 데이터 반영
        if (position == 0) {
            iconImageView.setImageResource(R.drawable.route_start);
        } else if (position == listViewItemList.size() -1) {
            iconImageView.setImageResource(R.drawable.route_end);
        } else {
            iconImageView.setImageResource(R.drawable.route1);
        }
        StopTextView.setText(listViewItem.getStopname());

        //위젯에 대한 이벤트리스너 작성 여기다가가


        // 선택된 포지션과 현재 그리려는 포지션이 동일하다면 백그라운드느 핑크
        if (selectedPosition == position) {
            convertView.setBackgroundResource(R.color.pink);
            notifyDataSetChanged();

        }else {
            convertView.setBackgroundResource(R.color.white);
            notifyDataSetChanged();
        }

        return convertView;
    }

    //아이템 데이터 추가를 위한 함수. 원하는 대로 작성
    public void addItem(String stopId, String stopname, int position){
        ListViewItem item = new ListViewItem(stopId, stopname, position);

        item.setStopId(stopId);
        item.setStopname(stopname);
        item.setPosition(position);

        listViewItemList.add(item);
    }

    public void setSelectedItem(int position) {
        selectedPosition = position;
        this.notifyDataSetChanged();
    }

}
