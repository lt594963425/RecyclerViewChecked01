package com.qihu.recyclerviewchoice.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qihu.recyclerviewchoice.R;
import com.qihu.recyclerviewchoice.adapter.MulipleRecyAdapter;
import com.qihu.recyclerviewchoice.bean.Person;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceActivity extends AppCompatActivity {

    private static final String TAG = "多选框";
    private RecyclerView recyclerView;
    private List<Person> list = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private Button btn;
    private List<Integer> posiList = new ArrayList<>();
    private MulipleRecyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_recyclerview);

        for (int i = 0; i < 100; i++) {
            Person person = new Person();
            person.setName("张" + i);
            person.setAge(i);
            person.setChecked(false);
            list.add(person);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new MulipleRecyAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MulipleRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "position=" + position);

                MulipleRecyAdapter.MyHolder holder = (MulipleRecyAdapter.MyHolder) recyclerView.getChildViewHolder(view);
                holder.checkBox.toggle();
//                list.get(position).setChecked(holder.checkBox.isChecked());//不需要

                if (!posiList.contains(position) && holder.checkBox.isChecked()) {
                    posiList.add(position);
                } else if (posiList.contains(position) || !holder.checkBox.isChecked()) {
                    posiList.remove(posiList.indexOf(position));
                }
            }
        });

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < posiList.size(); i++) {
                    sb.append(posiList.get(i) + ",");
                }
                Log.d(TAG, "sb=" + sb.toString());
                Toast.makeText(MultipleChoiceActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //方法一：错误，holder=null
//                MultipleChoiceHeadFootAdapter.MyHolder holder = (MultipleChoiceHeadFootAdapter.MyHolder) view.getTag();
    //方法二：checkBox= null
//               View view2 = recyclerView.getChildAt(position - layoutManager.findFirstVisibleItemPosition());
//                MultipleChoiceHeadFootAdapter.MyHolder holder = (MultipleChoiceHeadFootAdapter.MyHolder) recyclerView.getChildViewHolder(view2);
}
