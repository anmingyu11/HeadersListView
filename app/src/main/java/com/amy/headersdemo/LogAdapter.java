package com.amy.headersdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amy.headersdemo.bean.BaseLogItem;
import com.amy.headersdemo.bean.LogHeader;
import com.amy.headersdemo.bean.LogItem;
import com.amy.headersdemo.util.LogUtil;
import com.amy.headersdemo.widget.SwipeItemLayout;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private boolean isEditMode = false;

    private List<BaseLogItem> mLogItemList = new ArrayList<BaseLogItem>();

    private final List<SwipeItemLayout> mOpenedSil = new ArrayList<SwipeItemLayout>();
    private final List<LogItemViewHolder> mLogItemViewHolders = new ArrayList<LogItemViewHolder>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == BaseLogItem.TYPE_HEADER) {
            viewHolder = new LogHeaderViewHolder(inflater.inflate(R.layout.log_item_header, parent, false));
        } else if (viewType == BaseLogItem.TYPE_ITEM) {
            viewHolder = new LogItemViewHolder(inflater.inflate(R.layout.log_item, parent, false));
        }

        return viewHolder;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        boolean instanceOfItem = holder.getItemViewType() == BaseLogItem.TYPE_ITEM;
        if (instanceOfItem) {
            LogItemViewHolder logItemViewHolder = (LogItemViewHolder) holder;
            if (!mLogItemViewHolders.contains(logItemViewHolder)) {
                mLogItemViewHolders.add(logItemViewHolder);
                LogUtil.d("attach  : " + mLogItemViewHolders.size() + " holder : " + Integer.toHexString(holder.hashCode()));
            }
        }
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

    public void setLogItemList(List<BaseLogItem> logItemList) {
        mLogItemList = logItemList;
    }

    public void updateAll() {
        notifyDataSetChanged();
    }

    public void removeItem(LogItem item) {
        int position = mLogItemList.indexOf(item);
        mLogItemList.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItems(List<LogItem> logItems) {
        for (LogItem logItem : logItems) {
            removeItem(logItem);
        }
    }

    public void removeItems(LogItem... logItems) {
        for (LogItem logItem : logItems) {
            removeItem(logItem);
        }
    }

    private void bindEditModeOpen(LogItemViewHolder holder, final LogItem item) {
        holder.mRoot.setSwipeAble(false);
        //EditCheck
        holder.mTopLayerEditCheck.setAlpha(1f);
        holder.mTopLayerEditCheck.setChecked(item.isChecked());
        holder.mTopLayerLogContainer.setTranslationX(196);
    }

    private void bindEditModeClose(LogItemViewHolder holder, final LogItem item) {
        holder.mRoot.setSwipeAble(true);
        holder.mTopLayerEditCheck.setAlpha(0f);
        holder.mTopLayerLogContainer.setTranslationX(0f);
    }

    private void bindLogItemView(final LogItemViewHolder holder, final LogItem item) {
        holder.mHolderItem = item;
        holder.mTopLayerLogContent.setText(item.getContent());
        holder.mTopLayerLogDate.setText(item.getDate());

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

        holder.mBottomLayerLogDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(item);
            }
        });

        if (isEditMode) {
            bindEditModeOpen(holder, item);
        } else {
            bindEditModeClose(holder, item);
        }
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

    private void openEditMode() {
        closeOpenedSwipeItemLayoutWithAnim();
        for (LogItemViewHolder holder : mLogItemViewHolders) {
            holder.animOpenEditMode();
        }
    }

    private void closeEditMode() {
        closeOpenedSwipeItemLayoutWithAnim();

        for (BaseLogItem baseLogItem : mLogItemList) {
            if (baseLogItem instanceof LogItem) {
                LogItem item = (LogItem) baseLogItem;
                item.setChecked(false);
            }
        }
        for (LogItemViewHolder holder : mLogItemViewHolders) {
            holder.animCloseEditMode();
        }
    }

    private RecyclerView mHost;

    public void setHost(RecyclerView host) {
        mHost = host;
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        mHost.stopScroll();
        if (isEditMode) {
            //Open edit mode
            openEditMode();
        } else {
            //Close edit mode
            closeEditMode();
        }
    }

    private final class LogHeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView mHeaderTitle;

        public LogHeaderViewHolder(View itemView) {
            super(itemView);
            mHeaderTitle = (TextView) itemView.findViewById(R.id.log_item_header_title);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private void swiftChildTouchable(LogItemViewHolder logItemViewHolder, boolean isTouchable) {
        for (LogItemViewHolder holder : mLogItemViewHolders) {
            if (holder == logItemViewHolder) {
                return;
            }
            holder.mRoot.setTouchable(isTouchable);
            holder.mRoot.setSwipeAble(isTouchable);
        }
    }

    private final static Interpolator ANIM_OPEN_INTERPOLATOR = new DecelerateInterpolator();
    private final static Interpolator ANIM_CLOSE_INTERPOLATOR = new DecelerateInterpolator();

    private final class LogItemViewHolder extends RecyclerView.ViewHolder {

        private static final int ANIM_OPEN = 300;
        private static final int ANIM_CLOSE = 300;

        public LogItem mHolderItem;

        private SwipeItemLayout mRoot;

        //TopLayer
        private CheckBox mTopLayerEditCheck;
        private TextView mTopLayerButton;
        //TopLayerContent
        private ViewGroup mTopLayerLogContainer;
        private TextView mTopLayerLogContent;
        private TextView mTopLayerLogDate;
        //BottomLayer
        private ImageView mBottomLayerLogDelete;

        public LogItemViewHolder(View itemView) {
            super(itemView);
            mRoot = (SwipeItemLayout) itemView;
            mRoot.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    LogUtil.e(mHolderItem.getDate() + " action event : " + MotionEvent.actionToString(event.getAction()));
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            swiftChildTouchable(LogItemViewHolder.this, false);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL: {
                            swiftChildTouchable(LogItemViewHolder.this, true);
                            break;
                        }
                    }
                    return false;
                }
            });

            //Bottom
            mBottomLayerLogDelete = (ImageView) mRoot.findViewById(R.id.log_bottomLayer_delete);
            //Top
            mTopLayerLogContainer = (ViewGroup) mRoot.findViewById(R.id.log_item_topLayer_logContainer);
            mTopLayerButton = (TextView) mRoot.findViewById(R.id.log_item_topLayer_button);
            //Edit Check
            mTopLayerEditCheck = (CheckBox) mRoot.findViewById(R.id.log_item_topLayer_editCheck);
            mTopLayerEditCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mHolderItem.setChecked(isChecked);
                }
            });
            //TopContent
            mTopLayerLogContent = (TextView) mRoot.findViewById(R.id.log_item_topLayer_logContent);
            mTopLayerLogDate = (TextView) mRoot.findViewById(R.id.log_item_topLayer_logDate);
        }

        public void animOpenEditMode() {
            mTopLayerEditCheck.clearAnimation();
            mTopLayerEditCheck
                    .animate()
                    .setDuration(ANIM_OPEN)
                    .setInterpolator(ANIM_OPEN_INTERPOLATOR)
                    .alpha(1f);
            mTopLayerLogContainer.clearAnimation();
            mTopLayerLogContainer
                    .animate()
                    .setDuration(ANIM_OPEN)
                    .setInterpolator(ANIM_OPEN_INTERPOLATOR)
                    .translationX(70 + 126);
        }

        public void animCloseEditMode() {
            mTopLayerEditCheck.clearAnimation();
            mTopLayerEditCheck
                    .animate()
                    .setDuration(ANIM_CLOSE)
                    .setInterpolator(ANIM_CLOSE_INTERPOLATOR)
                    .alpha(0f);
            mTopLayerLogContainer.clearAnimation();
            mTopLayerLogContainer
                    .animate()
                    .setDuration(ANIM_CLOSE)
                    .setInterpolator(ANIM_CLOSE_INTERPOLATOR)
                    .translationX(0f);
        }
    }
}
