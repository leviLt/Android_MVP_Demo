package com.mvp.demo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mvp.demo.R;
import com.mvp.demo.entity.ChannelBean.NewsChannelBean;
import com.mvp.demo.interfaces.IOnDragVHListener;
import com.mvp.demo.interfaces.IOnItemMoveListener;
import com.mvp.demo.utils.ToastUtils;

import java.util.List;

/**
 * Created by luotao
 * 2018/3/20
 * emil:luotaosc@foxmail.com
 * qq:751423471
 *
 * @author 罗涛
 */

public class NewsChannelAdapter extends RecyclerView.Adapter implements IOnItemMoveListener {
    // 我的频道 标题部分
    public static final int TYPE_MY_CHANNEL_HEADER = 0;
    // 我的频道
    public static final int TYPE_MY = 1;
    // 其他频道 标题部分
    public static final int TYPE_OTHER_CHANNEL_HEADER = 2;
    // 其他频道
    public static final int TYPE_OTHER = 3;

    /**
     * 我的频道之前的header数量  该demo中 即标题部分 为 1
     */
    private static final int COUNT_PRE_MY_HEADER = 1;
    /**
     * 其他频道之前的header数量  该demo中 即标题部分 为 COUNT_PRE_MY_HEADER + 1
     */
    private static final int COUNT_PRE_OTHER_HEADER = COUNT_PRE_MY_HEADER + 1;

    private static final long ANIM_TIME = 360L;
    /**
     * touch 间隔时间  用于分辨是否是 "点击"
     */
    private static final long SPACE_TIME = 100;
    private final LayoutInflater layoutInflater;
    private final ItemTouchHelper helper;
    private final List<NewsChannelBean> enableItems, disableItems;
    private final Context mContext;
    /**
     * 是否为 编辑 模式
     */
    private boolean isEditMode;
    private long startTime;
    private Handler delayHandler = new Handler();

    public NewsChannelAdapter(Context context, ItemTouchHelper helper, List<NewsChannelBean> enableItems, List<NewsChannelBean> disableItems) {
        this.layoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.helper = helper;
        this.enableItems = enableItems;
        this.disableItems = disableItems;
    }

    public List<NewsChannelBean> getMyChannelItems() {
        return enableItems;
    }

    public List<NewsChannelBean> getOtnerChannelItems() {
        return disableItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_MY_CHANNEL_HEADER;
        } else if (position == enableItems.size() + 1) {
            return TYPE_OTHER_CHANNEL_HEADER;
        } else if (position > 0 && position < enableItems.size() + 1) {
            return TYPE_MY;
        } else {
            return TYPE_OTHER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view;
        switch (viewType) {
            case TYPE_MY_CHANNEL_HEADER:
                view = layoutInflater.inflate(R.layout.item_channel_my_header, parent, false);
                final MyChannelHeaderHolder myChannelHeaderHolder = new MyChannelHeaderHolder(view);
                myChannelHeaderHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isEditMode) {
                            startEditModel((RecyclerView) parent);
                            myChannelHeaderHolder.btnEdit.setText(R.string.finish);
                        } else {
                            cancelEditModel((RecyclerView) parent);
                            myChannelHeaderHolder.btnEdit.setText(R.string.edit);
                        }
                    }
                });
                return myChannelHeaderHolder;
            case TYPE_MY:
                view = layoutInflater.inflate(R.layout.item_channel_my, parent, false);
                final MyChannelHolder myChannelHolder = new MyChannelHolder(view);
                //我的频道点击事件
                myChannelHolder.tvChannelName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int position = myChannelHolder.getAdapterPosition();
                        if (isEditMode) {
                            RecyclerView recyclerView = (RecyclerView) parent;
                            View targetView = recyclerView.getLayoutManager().findViewByPosition(enableItems.size() - COUNT_PRE_MY_HEADER);
                            View currentView = recyclerView.getLayoutManager().findViewByPosition(position);
                            // 如果targetView不在屏幕内,则indexOfChild为-1  此时不需要添加动画,因为此时notifyItemMoved自带一个向目标移动的动画
                            // 如果在屏幕内,则添加一个位移动画
                            if (recyclerView.indexOfChild(targetView) >= 0) {
                                int targetX, targetY;
                                GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                                int spanCount = manager.getSpanCount();
                                if (enableItems.size() - COUNT_PRE_MY_HEADER % spanCount == 0) {
                                    View preTargetView = manager.findViewByPosition(enableItems.size() - COUNT_PRE_OTHER_HEADER - 1);
                                    targetX = preTargetView.getLeft();
                                    targetY = preTargetView.getTop();
                                } else {
                                    targetX = targetView.getLeft();
                                    targetY = targetView.getTop();
                                }
                                moveMyToOther(myChannelHolder);
                                //开启动画
//                                startAnimation(recyclerView, currentView, targetX, targetY);
                            } else {
                                moveMyToOther(myChannelHolder);
                            }
                        } else {
                            if (mOnChannelItemClickListener != null) {
                                mOnChannelItemClickListener.onChannelItemClick(v, position - COUNT_PRE_MY_HEADER);
                            }
                        }
                    }
                });
                //我的频道长按事件
                myChannelHolder.tvChannelName.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (!isEditMode) {
                            RecyclerView recyclerView = (RecyclerView) parent;
                            startEditModel(recyclerView);
                            View childAt = parent.getChildAt(0);
                            if (childAt == recyclerView.getLayoutManager().findViewByPosition(0)) {
                                TextView btnView = childAt.findViewById(R.id.tv_btn_edit);
                                btnView.setText(R.string.finish);
                            }
                        }
                        helper.startDrag(myChannelHolder);
                        return true;
                    }
                });
                myChannelHolder.tvChannelName.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (isEditMode) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    startTime = System.currentTimeMillis();
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    //区别点击事件还是touch事件
                                    if (System.currentTimeMillis() - startTime > SPACE_TIME) {
                                        helper.startDrag(myChannelHolder);
                                    }
                                    break;
                                case MotionEvent.ACTION_UP:
                                    startTime = 0;
                                    break;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                });
                return myChannelHolder;
            case TYPE_OTHER_CHANNEL_HEADER:
                view = layoutInflater.inflate(R.layout.item_channel_other_header, parent, false);
                return new OtherChannelHeaderHolder(view);
            case TYPE_OTHER:
                view = layoutInflater.inflate(R.layout.item_channel_other, parent, false);
                final OtherChannelHolder otherChannelHolder = new OtherChannelHolder(view);
                otherChannelHolder.tvChannelName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showToast(mContext, "点击");
                    }
                });
                //其他频道添加到我的频道
                otherChannelHolder.tvChannelName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currenPosition = otherChannelHolder.getAdapterPosition();
                        RecyclerView recyclerView = (RecyclerView) parent;
                        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                        int spanCount = gridLayoutManager.getSpanCount();
                        // 目标位置的前一个item  即当前MyChannel的最后一个
                        View targetPreView = gridLayoutManager.findViewByPosition(enableItems.size() - 1 + COUNT_PRE_MY_HEADER);
                        // 如果RecyclerView滑动到底部,移动的目标位置的y轴 - height
                        View currentView = gridLayoutManager.findViewByPosition(currenPosition);
                        if (recyclerView.indexOfChild(targetPreView) >= 0) {
                            int targetX = targetPreView.getLeft();
                            int targetY = targetPreView.getTop();
                            int targetPosition = enableItems.size() - 1 + COUNT_PRE_OTHER_HEADER;
                            // target 在最后一行第一个
                            if ((targetPosition - 1) % spanCount == 0) {
                                View targetView = gridLayoutManager.findViewByPosition(targetPosition);
                                targetX = targetView.getLeft();
                                targetY = targetView.getTop()+targetView.getWidth();
                            } else {
                                targetX += targetPreView.getWidth();
                                targetY += targetPreView.getWidth();

                                // 最后一个item可见
                                if (gridLayoutManager.findLastVisibleItemPosition() == getItemCount() - 1) {
                                    // 最后的item在最后一行第一个位置
                                    if ((getItemCount() - 1 - enableItems.size() - COUNT_PRE_OTHER_HEADER) % spanCount == 0) {
                                        // RecyclerView实际高度 > 屏幕高度 && RecyclerView实际高度 < 屏幕高度 + item.height
                                        int firstVisiblePosition = gridLayoutManager.findFirstVisibleItemPosition();
                                        if (firstVisiblePosition == 0) {
                                            // FirstCompletelyVisibleItemPosition == 0 即 内容不满一屏幕 , targetY值不需要变化
                                            // // FirstCompletelyVisibleItemPosition != 0 即 内容满一屏幕 并且 可滑动 , targetY值 + firstItem.getTop
                                            if (gridLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) {
                                                int offset = (-recyclerView.getChildAt(0).getTop()) - recyclerView.getPaddingTop();
                                                targetY += offset;
                                            }
                                        } else { // 在这种情况下 并且 RecyclerView高度变化时(即可见第一个item的 position != 0),
                                            // 移动后, targetY值  + 一个item的高度
                                            targetY += targetPreView.getHeight();
                                        }
                                    }
                                } else {
                                    System.out.println("current--No");
                                }

                            }
                            // 如果当前位置是otherChannel可见的最后一个
                            // 并且 当前位置不在grid的第一个位置
                            // 并且 目标位置不在grid的第一个位置

                            // 则 需要延迟250秒 notifyItemMove , 这是因为这种情况 , 并不触发ItemAnimator , 会直接刷新界面
                            // 导致我们的位移动画刚开始,就已经notify完毕,引起不同步问题
                            if (currenPosition == gridLayoutManager.findLastVisibleItemPosition() &&
                                    (currenPosition - enableItems.size() - COUNT_PRE_OTHER_HEADER) % spanCount != 0 &&
                                    (targetPosition - COUNT_PRE_MY_HEADER) % spanCount != 0) {
                                moveMyToOtherWithDelay(otherChannelHolder);
                            } else {
                                moveOtherToMy(otherChannelHolder);
                            }
                            startAnimation(recyclerView, currentView, targetX, targetY);
                        } else {
                            moveOtherToMy(otherChannelHolder);
                        }

                    }
                });
                return otherChannelHolder;
            default:
                return null;
        }
    }

    /**
     * 其他频道  移动到  我的频道
     *
     * @param otherChannelHolder
     */
    private void moveOtherToMy(OtherChannelHolder otherChannelHolder) {
        final int position = processItemRemoveAdd(otherChannelHolder);
        if (position == -1) {
            return;
        }
        notifyItemMoved(position, enableItems.size() - 1 + COUNT_PRE_MY_HEADER);
    }

    private int processItemRemoveAdd(OtherChannelHolder otherChannelHolder) {
        int position = otherChannelHolder.getAdapterPosition();
        int startPosition = position - enableItems.size() - COUNT_PRE_OTHER_HEADER;
        if (startPosition > disableItems.size() - 1) {
            return -1;
        }
        NewsChannelBean item = disableItems.get(startPosition);
        disableItems.remove(item);
        enableItems.add(item);
        return position;
    }

    private void moveMyToOtherWithDelay(OtherChannelHolder otherChannelHolder) {
        final int position = processItemRemoveAdd(otherChannelHolder);
        if (position == -1) {
            return;
        }
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyItemMoved(position, enableItems.size() - 1 + COUNT_PRE_MY_HEADER);
            }
        }, 360L);
    }

    /**
     * 开启动画
     *
     * @param recyclerView
     * @param currentView
     * @param targetX
     * @param targetY
     */
    private void startAnimation(RecyclerView recyclerView, final View currentView, int targetX, int targetY) {
        final ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
        final ImageView mirrorView = addMirrorView(viewGroup, recyclerView, currentView);

        Animation animation = getTranslateAnimator(
                targetX - currentView.getLeft(), targetY - currentView.getTop());
        currentView.setVisibility(View.INVISIBLE);
        mirrorView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewGroup.removeView(mirrorView);
                if (currentView.getVisibility() == View.INVISIBLE) {
                    currentView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private Animation getTranslateAnimator(int targetX, int targetY) {
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetX,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.ABSOLUTE, targetY);
        // RecyclerView默认移动动画250ms 这里设置360ms 是为了防止在位移动画结束后 remove(view)过早 导致闪烁
        translateAnimation.setDuration(ANIM_TIME);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    private ImageView addMirrorView(ViewGroup parent, RecyclerView recyclerView, View currentView) {
        /**
         * 我们要获取cache首先要通过setDrawingCacheEnable方法开启cache，然后再调用getDrawingCache方法就可以获得view的cache图片了。
         buildDrawingCache方法可以不用调用，因为调用getDrawingCache方法时，若果cache没有建立，系统会自动调用buildDrawingCache方法生成cache。
         若想更新cache, 必须要调用destoryDrawingCache方法把旧的cache销毁，才能建立新的。
         当调用setDrawingCacheEnabled方法设置为false, 系统也会自动把原来的cache销毁。
         */
        currentView.destroyDrawingCache();
        currentView.setDrawingCacheEnabled(true);
        final ImageView mirrorView = new ImageView(recyclerView.getContext());
        Bitmap bitmap = Bitmap.createBitmap(currentView.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        currentView.setDrawingCacheEnabled(false);
        int[] locations = new int[2];
        currentView.getLocationOnScreen(locations);
        int[] parenLocations = new int[2];
        recyclerView.getLocationOnScreen(parenLocations);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        params.setMargins(locations[0], locations[1] - parenLocations[1], 0, 0);
        parent.addView(mirrorView, params);

        return mirrorView;
    }

    private void moveMyToOther(MyChannelHolder myChannelHolder) {
        int position = myChannelHolder.getAdapterPosition();
        int startPosition = position - COUNT_PRE_MY_HEADER;
        if (startPosition > enableItems.size() - 1) {
            return;
        }
        NewsChannelBean bean = enableItems.get(startPosition);
        enableItems.remove(bean);
        disableItems.add(0, bean);
        notifyItemMoved(position, enableItems.size() + COUNT_PRE_OTHER_HEADER);
    }

    /**
     * 取消编辑模式
     *
     * @param parent
     */
    private void cancelEditModel(RecyclerView parent) {
        isEditMode = false;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = parent.getChildAt(i);
            View view = childAt.findViewById(R.id.img_edit);
            if (view != null) {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 开启编辑模式
     *
     * @param parent
     */
    private void startEditModel(RecyclerView parent) {
        isEditMode = true;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = parent.getChildAt(i);
            ImageView view = childAt.findViewById(R.id.img_edit);
            if (null != view) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyChannelHolder) {
            MyChannelHolder mHolder = (MyChannelHolder) holder;
            mHolder.tvChannelName.setText(enableItems.get(position - COUNT_PRE_MY_HEADER).getChannelName());
            if (isEditMode) {
                mHolder.ivEdit.setVisibility(View.VISIBLE);
            } else {
                mHolder.ivEdit.setVisibility(View.GONE);
            }
        } else if (holder instanceof OtherChannelHolder) {
            ((OtherChannelHolder) holder).tvChannelName.setText(disableItems.get(
                    position - COUNT_PRE_OTHER_HEADER - enableItems.size()).getChannelName());
        } else if (holder instanceof MyChannelHeaderHolder) {
            MyChannelHeaderHolder headerHolder = (MyChannelHeaderHolder) holder;
            if (isEditMode) {
                headerHolder.btnEdit.setText(R.string.finish);
            } else {
                headerHolder.btnEdit.setText(R.string.edit);
            }
        }
    }

    @Override
    public int getItemCount() {
        return enableItems.size() + disableItems.size() + COUNT_PRE_OTHER_HEADER;
    }

    public void setOnChannelItemClickListener(OnChannelItemClickListener onChannelItemClickListener) {
        mOnChannelItemClickListener = onChannelItemClickListener;
    }


    /**
     * item开始移动
     *
     * @param fromPosition 拖动起始位置
     * @param toPosition   拖动到哪个位置
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        NewsChannelBean item = enableItems.get(fromPosition - COUNT_PRE_MY_HEADER);
        enableItems.remove(item);
        enableItems.add(toPosition - COUNT_PRE_MY_HEADER, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    public class MyChannelHeaderHolder extends RecyclerView.ViewHolder {
        private TextView btnEdit;

        MyChannelHeaderHolder(View itemView) {
            super(itemView);
            btnEdit = itemView.findViewById(R.id.tv_btn_edit);
        }
    }

    public class MyChannelHolder extends RecyclerView.ViewHolder implements IOnDragVHListener {
        private ImageView ivEdit;
        private TextView tvChannelName;

        MyChannelHolder(View itemView) {
            super(itemView);
            ivEdit = itemView.findViewById(R.id.img_edit);
            tvChannelName = itemView.findViewById(R.id.tv);
        }

        /**
         * 开始选中item
         */
        @Override
        public void onItemSelected() {
            tvChannelName.setBackgroundResource(R.color.textColorPrimary);
        }

        /**
         * 拖动或者滑动结束
         */

        @Override
        public void onItemFinish() {
            tvChannelName.setBackgroundResource(R.color.viewBackground);
        }
    }

    public class OtherChannelHeaderHolder extends RecyclerView.ViewHolder {

        OtherChannelHeaderHolder(View itemView) {
            super(itemView);
        }
    }

    public class OtherChannelHolder extends RecyclerView.ViewHolder {
        private TextView tvChannelName;
        private ImageView ivBtnEidt;

        OtherChannelHolder(View itemView) {
            super(itemView);
            tvChannelName = itemView.findViewById(R.id.tv);
            ivBtnEidt = itemView.findViewById(R.id.img_edit);
        }
    }

    public interface OnChannelItemClickListener {
        void onChannelItemClick(View v, int position);
    }

    private OnChannelItemClickListener mOnChannelItemClickListener;
}
