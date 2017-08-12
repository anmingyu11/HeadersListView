package com.amy.headersdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.amy.headersdemo.util.LogUtil;
import com.amy.headersdemo.util.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private final LinkedHashMap<Integer, String> mHeaderList = new LinkedHashMap<Integer, String>();
    private List<LogItem> mLogItemList;

    private LogAdapter mLogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        setContentView(R.layout.main);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_list);

        mLogAdapter = new LogAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL);
        FloatingBarItemDecoration floatingBarItemDecoration = new FloatingBarItemDecoration(
                this, mHeaderList
        );

        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.addItemDecoration(floatingBarItemDecoration);

        mRecyclerView.setAdapter(mLogAdapter);

        //update
        mLogAdapter.updateAll(mLogItemList);
    }

    public void initData() {
        mLogItemList = fakeLogs(40);
        Collections.sort(mLogItemList);

        LogItem.PERIOD lastPeriod = mLogItemList.get(0).getPeriod();
        mHeaderList.put(lastPeriod.ordinal(), lastPeriod.str);
        for (int i = 0; i < mLogItemList.size(); i++) {
            LogItem item = mLogItemList.get(i);
            LogItem.PERIOD currPeriod = item.getPeriod();

            if (lastPeriod.ordinal() == LogItem.PERIOD.PERIOD_EARLIER.ordinal()) {
                return;
            }
            if (currPeriod.ordinal() > lastPeriod.ordinal()) {
                lastPeriod = currPeriod;
                mHeaderList.put(i, lastPeriod.str);
            }
        }

    }

    public List<LogItem> fakeLogs(int size) {
        List<LogItem> logItemList = new ArrayList<LogItem>();

        for (int i = 0; i < size; i++) {
            String content = "Content : ------ log ====== " + i + "------";
            long millis = System.currentTimeMillis() - i * TimeUtil.MILLIS2DAY;
            //LogUtil.d("millis : " + millis);
            logItemList.add(i, new LogItem(content, millis));
        }
        LogUtil.d(
                "fake : " + logItemList.size()
        );
        return logItemList;
    }


}
