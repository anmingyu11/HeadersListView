package com.amy.headersdemo.bean;

public class LogHeader extends BaseLogItem {

    private String mTitle;

    public LogHeader(String title) {
        setTYPE(TYPE_HEADER);
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public String toString() {
        return "LogHeader{" +
                "mTitle='" + mTitle + '\'' +
                '}';
    }
}
