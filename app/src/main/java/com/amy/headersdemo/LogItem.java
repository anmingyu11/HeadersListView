package com.amy.headersdemo;

import android.support.annotation.NonNull;

import com.amy.headersdemo.util.TimeUtil;

public class LogItem implements Comparable<LogItem> {

    public final static String PERIOD_1_TODAY = "今天";
    public final static String PERIOD_2_WEEK = "过去七天";
    public final static String PERIOD_3_MONTH = "一个月内";
    public final static String PERIOD_4_EARLIER = "更早";

    public enum PERIOD {
        PERIOD_TODAY(1), PERIOD_WEEK(2), PERIOD_MONTH(3), PERIOD_EARLIER(4);

        public String str;

        PERIOD(int i) {
            if (i == 1) {
                str = PERIOD_1_TODAY;
            } else if (i == 2) {
                str = PERIOD_2_WEEK;
            } else if (i == 3) {
                str = PERIOD_3_MONTH;
            } else if (i == 4) {
                str = PERIOD_4_EARLIER;
            }
        }
    }

    private String mContent;
    private String mDate;
    private long mLogTime;
    private PERIOD mPeriod;

    public LogItem(String content, long logTime) {
        setContent(content);
        setTime(logTime);
        convertPeriod();
        convertDate();
    }

    private void convertPeriod() {
        TimeUtil.TimeStruct logTimeStruct = TimeUtil.getTimeStruct(mLogTime);
        TimeUtil.TimeStruct curTimeStruct = TimeUtil.getCurTimeStruct();
        int diffDay = curTimeStruct.differenceDay(logTimeStruct);
        //LogUtil.d("diffDay : " + diffDay);
        if (diffDay > 30) {
            mPeriod = PERIOD.PERIOD_EARLIER;
        } else if (diffDay > 7 && diffDay <= 30) {
            mPeriod = PERIOD.PERIOD_MONTH;
        } else if (diffDay > 0 && diffDay <= 7) {
            mPeriod = PERIOD.PERIOD_WEEK;
        } else if (diffDay == 0) {
            mPeriod = PERIOD.PERIOD_TODAY;
        } else {
            throw new RuntimeException();
        }
    }

    private void convertDate() {
        mDate = TimeUtil.getDateFormat(mLogTime);
    }

    public void setContent(String content) {
        mContent = content;
    }

    public void setTime(long time) {
        mLogTime = time;
    }

    //======Getters======

    public long getLogTime() {
        return mLogTime;
    }

    public String getDate() {
        return mDate;
    }

    public String getContent() {
        return mContent;
    }

    public PERIOD getPeriod() {
        return mPeriod;
    }

    @Override
    public int compareTo(@NonNull LogItem logItem) {
        if (this.mLogTime == logItem.mLogTime) {
            return 0;
        } else if (this.mLogTime > logItem.mLogTime) {
            return -1;
        } else {
            return 1;
        }
    }
}
