package com.amy.headersdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.amy.headersdemo.util.LogUtil;
import com.amy.headersdemo.util.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int mSlop;

    private RecyclerView mRecyclerView;

    private final LinkedHashMap<Integer, String> mHeaderList = new LinkedHashMap<Integer, String>();
    private List<LogItem> mLogItemList;

    private LogAdapter mLogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mSlop = ViewConfiguration.get(this).getScaledTouchSlop();

        initData();

        setContentView(R.layout.main);

        initRecyclerView();
    }

    public void initRecyclerView() {
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
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {

            int dx = 0;
            int dy = 0;

            @Override
            public boolean onTouch(View view, MotionEvent e) {

                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx = (int) e.getX();
                        dy = (int) e.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int tempX = (int) e.getX();
                        int tempY = (int) e.getY();
                        if (Math.abs(dx - tempX) > mSlop && Math.abs(tempY - dy) > mSlop) {
                            closeAllOpenedItem();
                        }
                        break;

                }
                return view.onTouchEvent(e);
            }

            public void closeAllOpenedItem() {
                if (mLogAdapter != null)
                     mLogAdapter.closeOpenedSwipeItemLayoutWithAnim();
            }
        });
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
