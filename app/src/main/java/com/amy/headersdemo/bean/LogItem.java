package com.amy.headersdemo.bean;

import com.amy.headersdemo.util.TimeUtil;

public class LogItem extends BaseLogItem {

    private boolean isChecked;
    private String mContent;
    private String mDate;
    private PERIOD mPeriod;

    public LogItem(String content, long logTime) {
        setTYPE(TYPE_ITEM);
        setChecked(false);
        setContent(content);
        setTime(logTime);
        convertPeriod();
        convertDate();
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return this.isChecked;
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
    public String toString() {
        return "LogItem{" +
                ", mDate='" + mDate + '\'' +
                ", mPeriod=" + mPeriod +
                '}';
    }
}
