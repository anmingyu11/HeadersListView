package com.amy.headersdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.amy.headersdemo.animator.SlideInRightAnimator;
import com.amy.headersdemo.inerpolator.CubicInterpolator;
import com.amy.headersdemo.util.Faker;

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

        Faker.getInstance().updateHeader();

        setContentView(R.layout.main);

        CheckBox c = (CheckBox) findViewById(R.id.editModeSwitcher);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mLogAdapter.setEditMode(isChecked);
            }
        });

        initRecyclerView();

    }

    public void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.main_list);

        mLogAdapter = new LogAdapter();
        mLogAdapter.setHost(mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL);
        mFloatingBarItemDecoration = new FloatingBarItemDecoration(
                this, Faker.getInstance().mHeaderList
        );

        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.addItemDecoration(mFloatingBarItemDecoration);
        mRecyclerView.setItemAnimator(new SlideInRightAnimator(CubicInterpolator.OUT));

        mLogAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                Faker.getInstance().updateHeader();
            }

            @Override
            public void onChanged() {
                super.onChanged();
                Faker.getInstance().updateHeader();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                Faker.getInstance().updateHeader();
            }
        });

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
        mLogAdapter.setLogItemList(Faker.getInstance().mLogItemList);
        mLogAdapter.updateAll();

    }

    public void initData() {
        Faker.getInstance().initData(40);
    }

}
