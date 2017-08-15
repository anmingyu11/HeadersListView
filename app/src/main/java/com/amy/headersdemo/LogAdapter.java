package com.amy.headersdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amy.headersdemo.util.LogUtil;
import com.amy.headersdemo.widget.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {

    private Context mContext;

    private List<LogItem> mLogItemList = new ArrayList<LogItem>();
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

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

    public void remove(LogItem item) {
        //Todo : this is a problem logic
        int position = mLogItemList.indexOf(item);
        mLogItemList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(LogViewHolder holder, final int position) {
        final LogItem item = mLogItemList.get(position);
        holder.mContent.setText(item.getContent());
        holder.mDate.setText(item.getDate());
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.d("onDelete");
                remove(item);
            }
        });
        holder.mRoot.setDelegate(new SwipeItemLayout.SwipeItemLayoutDelegate() {
            @Override
            public void onSwipeItemLayoutOpened(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
                mOpenedSil.add(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutClosed(SwipeItemLayout swipeItemLayout) {
                mOpenedSil.remove(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutStartOpen(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLogItemList.size();
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }

    public final class LogViewHolder extends RecyclerView.ViewHolder {

        private SwipeItemLayout mRoot;
        private TextView mContent;
        private TextView mDate;
        private ImageView mDelete;

        public LogViewHolder(View itemView) {
            super(itemView);
            mRoot = (SwipeItemLayout) itemView;
            mDelete = mRoot.findViewById(R.id.log_delete);
            mDate = mRoot.findViewById(R.id.log_date);
            mContent = mRoot.findViewById(R.id.log_content);
        }
    }
}
