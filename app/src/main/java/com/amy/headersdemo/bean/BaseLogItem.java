package com.amy.headersdemo.bean;

import android.support.annotation.NonNull;

public class BaseLogItem implements Comparable<BaseLogItem> {
    public static int TYPE_HEADER = 0;
    public static int TYPE_ITEM = 1;

    protected long mLogTime;
    private int mType;

    public void setTYPE(int type) {
        if (type < 0 || type > 1) {
            throw new IllegalArgumentException("Need a legal type");
        }
        mType = type;
    }

    public int getTYPE() {
        return mType;
    }

    @Override
    public int compareTo(@NonNull BaseLogItem logItem) {
        if (this.mLogTime == logItem.mLogTime) {
            return 0;
        } else if (this.mLogTime > logItem.mLogTime) {
            return -1;
        } else {
            return 1;
        }
    }
}
