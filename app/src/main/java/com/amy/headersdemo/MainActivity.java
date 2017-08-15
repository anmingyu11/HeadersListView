package com.amy.headersdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.amy.headersdemo.animator.SlideInRightAnimator;
import com.amy.headersdemo.inerpolator.CubicInterpolator;
import com.amy.headersdemo.util.Faker;
import com.amy.headersdemo.util.LogUtil;

import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    private int mSlop;

    private RecyclerView mRecyclerView;

    private LogAdapter mLogAdapter;

    private FloatingBarItemDecoration mFloatingBarItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mSlop = ViewConfiguration.get(this).getScaledTouchSlop();

        initData();

        updateHeadersList();

        setContentView(R.layout.main);

        initRecyclerView();

    }

    public void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.main_list);

        mLogAdapter = new LogAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL);
        mFloatingBarItemDecoration = new FloatingBarItemDecoration(
                this, Faker.getInstance().mHeaderList
        );

        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.addItemDecoration(mFloatingBarItemDecoration);
        mRecyclerView.setItemAnimator(new SlideInRightAnimator(CubicInterpolator.OUT));

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
        mLogAdapter.updateAll(Faker.getInstance().mLogItemList);
        mLogAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                int logSize = Faker.getInstance().mLogItemList.size();
                Faker.getInstance().updateHeadersList();
                LinkedHashMap<Integer, String> headersList = Faker.getInstance().mHeaderList;
                LogUtil.d("logSize : " + logSize);
                LogUtil.d("headerSize : " + headersList.size());
            }
        });
    }

    public void initData() {
        Faker.getInstance().initData();
    }

    public void updateHeadersList() {
        Faker.getInstance().updateHeadersList();
    }

}
