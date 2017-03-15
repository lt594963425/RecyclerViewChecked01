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
import com.qihu.recyclerviewchoice.adapter.BgSingleChoiceRecyAdapter;
import com.qihu.recyclerviewchoice.bean.Person;

import java.util.ArrayList;
import java.util.List;

public class BgSingleChoiceActivity extends AppCompatActivity {

    private static final String TAG = "单选按钮背景色";
    private RecyclerView recyclerView;
    private List<Person> list = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private Button btn;
    private List<Integer> posiList = new ArrayList<>();
    private BgSingleChoiceRecyAdapter adapter;
    private int checkedPosition;

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
        adapter = new BgSingleChoiceRecyAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BgSingleChoiceRecyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "position=" + position);

                for (int i = 0; i < list.size(); i++) {
                    if (i == position) {
                        list.get(i).setChecked(true);
                    } else {
                        list.get(i).setChecked(false);
                    }
                }
                checkedPosition = position;
                adapter.notifyDataSetChanged();
            }
        });

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "单选=" + checkedPosition);
                Toast.makeText(BgSingleChoiceActivity.this, "" + checkedPosition, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
