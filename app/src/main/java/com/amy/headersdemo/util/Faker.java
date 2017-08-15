package com.amy.headersdemo.util;

import com.amy.headersdemo.LogItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class Faker {

    private static Faker sInstance;

    public final LinkedHashMap<Integer, String> mHeaderList = new LinkedHashMap<Integer, String>();
    public List<LogItem> mLogItemList;

    private Faker() {
    }

    public static Faker getInstance() {
        if (sInstance == null) {
            sInstance = new Faker();
        }
        return sInstance;
    }

    public void initData() {
        mLogItemList = fakeLogs(40);
        Collections.sort(mLogItemList);

    }

    public void updateHeadersList() {
        mHeaderList.clear();

        LogItem.PERIOD lastPeriod = mLogItemList.get(0).getPeriod();
        LogUtil.d("lastPeriod : " + lastPeriod.str);
        mHeaderList.put(0, lastPeriod.str);
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

        LogUtil.e("HeaderList : " + mHeaderList.toString());
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
