package com.amy.headersdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    private Context mContext;

    private List<LogItem> mLogItemList = new ArrayList<LogItem>();

    public LogAdapter() {
    }

    public void updateAll(List<LogItem> logItems) {
        mLogItemList = logItems;
        notifyDataSetChanged();
    }

    @Override
    public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.log_item, parent, false);

        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, int position) {
        LogItem item = mLogItemList.get(position);
        holder.mLogContentView.setText(item.getContent());
        holder.mLogDateView.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return mLogItemList.size();
    }

    public final class LogViewHolder extends RecyclerView.ViewHolder {

        private TextView mLogContentView;
        private TextView mLogDateView;

        public LogViewHolder(View itemView) {
            super(itemView);
            mLogDateView = itemView.findViewById(R.id.log_date);
            mLogContentView = itemView.findViewById(R.id.log_content);
        }
    }
}
