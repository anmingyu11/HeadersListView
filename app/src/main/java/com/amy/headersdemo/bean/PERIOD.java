package com.amy.headersdemo.bean;

public enum PERIOD {

    PERIOD_TODAY(1), PERIOD_WEEK(2), PERIOD_MONTH(3), PERIOD_EARLIER(4);

    public String str;

    public final static String PERIOD_1_TODAY = "今天";
    public final static String PERIOD_2_WEEK = "过去七天";
    public final static String PERIOD_3_MONTH = "一个月内";
    public final static String PERIOD_4_EARLIER = "更早";

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

