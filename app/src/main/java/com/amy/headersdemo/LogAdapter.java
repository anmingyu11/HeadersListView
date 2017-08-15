package com.amy.headersdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amy.headersdemo.bean.BaseLogItem;
import com.amy.headersdemo.bean.LogHeader;
import com.amy.headersdemo.bean.LogItem;
import com.amy.headersdemo.widget.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private List<BaseLogItem> mLogItemList = new ArrayList<BaseLogItem>();
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

    public LogAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == BaseLogItem.TYPE_HEADER) {
            viewHolder = new LogHeaderViewHolder(inflater.inflate(R.layout.log_header, parent, false));
        } else if (viewType == BaseLogItem.TYPE_ITEM) {
            viewHolder = new LogItemViewHolder(inflater.inflate(R.layout.log_item, parent, false));
        }

        return viewHolder;
    }

    public void updateAll(List<BaseLogItem> logItems) {
        mLogItemList = logItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        BaseLogItem baseLogItem = mLogItemList.get(position);
        if (baseLogItem instanceof LogHeader) {
            return BaseLogItem.TYPE_HEADER;
        } else if (baseLogItem instanceof LogItem) {
            return BaseLogItem.TYPE_ITEM;
        } else {
            throw new IllegalArgumentException("Wrong type");
        }
    }

    public void removeItem(LogItem item) {
        //Todo : this is a problem logic
        int position = mLogItemList.indexOf(item);
        mLogItemList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItems(List<LogItem> logItems) {
        for (LogItem logItem : logItems) {
            int position = mLogItemList.indexOf(logItem);
            mLogItemList.remove(position);
        }
    }

    public void removeItems(LogItem... logItems) {
        for (LogItem logItem : logItems) {
            int position = mLogItemList.indexOf(logItem);
            mLogItemList.remove(position);
        }
    }

    private void bindLogItemView(LogItemViewHolder holder, final LogItem item) {
        holder.mContent.setText(item.getContent());
        holder.mDate.setText(item.getDate());
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(item);
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

    private void bindLogHeaderView(LogHeaderViewHolder holder, final LogHeader item) {
        holder.mHeaderTitle.setText(item.getTitle());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        BaseLogItem baseLogItem = mLogItemList.get(position);
        if (holder instanceof LogItemViewHolder) {
            bindLogItemView((LogItemViewHolder) holder, (LogItem) baseLogItem);
        } else if (holder instanceof LogHeaderViewHolder) {
            bindLogHeaderView((LogHeaderViewHolder) holder, (LogHeader) baseLogItem);
        } else {
            throw new IllegalArgumentException("View holder type illegal");
        }
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

    private final class LogHeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView mHeaderTitle;

        public LogHeaderViewHolder(View itemView) {
            super(itemView);
            mHeaderTitle = (TextView) itemView.findViewById(R.id.log_header_title);
        }
    }

    private final class LogItemViewHolder extends RecyclerView.ViewHolder {

        private SwipeItemLayout mRoot;
        private TextView mContent;
        private TextView mDate;
        private ImageView mDelete;

        public LogItemViewHolder(View itemView) {
            super(itemView);
            mRoot = (SwipeItemLayout) itemView;
            mDelete = (ImageView) mRoot.findViewById(R.id.log_delete);
            mDate = (TextView) mRoot.findViewById(R.id.log_date);
            mContent = (TextView) mRoot.findViewById(R.id.log_content);
        }
    }
}
