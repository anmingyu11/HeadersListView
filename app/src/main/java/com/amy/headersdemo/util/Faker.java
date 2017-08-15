package com.amy.headersdemo.util;

import com.amy.headersdemo.bean.BaseLogItem;
import com.amy.headersdemo.bean.LogHeader;
import com.amy.headersdemo.bean.LogItem;
import com.amy.headersdemo.bean.PERIOD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class Faker {

    private static Faker sInstance;

    public final LinkedHashMap<Integer, String> mHeaderList = new LinkedHashMap<Integer, String>();
    public List<BaseLogItem> mLogItemList;

    private Faker() {
    }

    public static Faker getInstance() {
        if (sInstance == null) {
            sInstance = new Faker();
        }
        return sInstance;
    }

    public void initData(int logs) {
        mLogItemList = fakeLogs(logs);
    }

    public void updateHeader() {
        mHeaderList.clear();
        for (BaseLogItem item : mLogItemList) {
            if (item.getTYPE() == BaseLogItem.TYPE_HEADER) {
                LogHeader logHeader = (LogHeader) item;
                mHeaderList.put(mLogItemList.indexOf(logHeader), logHeader.getTitle());
            }
        }
    }

    public List<BaseLogItem> fakeLogs(int size) {
        List<BaseLogItem> logItemList = new ArrayList<BaseLogItem>();

        //Faker logs
        for (int i = 0; i < size; i++) {
            String content = "Content : ------ log ====== " + i + "------";
            long millis = System.currentTimeMillis() - i * TimeUtil.MILLIS2DAY;
            BaseLogItem item = new LogItem(content, millis);

            logItemList.add(item);
        }

        mLogItemList = logItemList;
        Collections.sort(mLogItemList);

        //Add headers
        //First header
        LogItem lastLogItem = (LogItem) mLogItemList.get(0);
        mLogItemList.add(0, new LogHeader(lastLogItem.getPeriod().str));
        //The others header
        for (int i = 1; i < mLogItemList.size(); i++) {
            LogItem currentLogItem = (LogItem) mLogItemList.get(i);

            PERIOD lastPeriod = lastLogItem.getPeriod();
            PERIOD currentPeriod = currentLogItem.getPeriod();
            if (currentPeriod.ordinal() > lastPeriod.ordinal()) {
                mLogItemList.add(i, new LogHeader(currentPeriod.str));
            }

            lastLogItem = currentLogItem;
            if (lastPeriod.ordinal() == PERIOD.PERIOD_EARLIER.ordinal()) {
                break;
            }
        }

        LogUtil.d(
                "fake : " + logItemList.size()
        );
        return logItemList;
    }

    public void print() {
        for (BaseLogItem baseLogItem : mLogItemList) {
            if (baseLogItem instanceof LogHeader) {
                LogHeader header = (LogHeader) baseLogItem;
                LogUtil.e("------" + header.toString() + "------");
                continue;
            }
            if (baseLogItem instanceof LogItem) {
                LogItem logItem = (LogItem) baseLogItem;
                LogUtil.d(logItem.toString());
                continue;
            }
        }
    }

}
