package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
import com.handmark.pulltorefresh.library.extras.CustomPullToEnlargePicScrollView;
import com.handmark.pulltorefresh.library.extras.SupportListViewInScrollView;
import com.handmark.pulltorefresh.library.internal.INotifyHeaderView;

/**
 * Created by new on 2015/6/8.
 */
public class PullToRefreshSupportListViewInScrollViewToEnlargePic extends PullToRefreshBase<ScrollView> implements IListViewInScrollView {

    public PullToRefreshSupportListViewInScrollViewToEnlargePic(Context context) {
        super(context);
    }

    public PullToRefreshSupportListViewInScrollViewToEnlargePic(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshSupportListViewInScrollViewToEnlargePic(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshSupportListViewInScrollViewToEnlargePic(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }


    @Override
    protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
        ScrollView scrollView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            scrollView = new InternalScrollViewSDK9(context, attrs);
        } else {
            scrollView = new CustomPullToEnlargePicScrollView(context, attrs);
        }

        scrollView.setId(R.id.scrollview);
        return scrollView;
    }

    @Override
    protected boolean isReadyForPullStart() {
        getLoadingLayoutProxy().setPullLabel(getContext().getString(R.string.pull_to_refresh_from_bottom_pull_label));
        return mRefreshableView.getScrollY() == 0;
    }

    @Override
    protected boolean isReadyForPullEnd() {
        if(!((CustomPullToEnlargePicScrollView)mRefreshableView).canFooterRefresh()){
            return false;
        }
        getLoadingLayoutProxy().setPullLabel(getContext().getString(R.string.pull_to_refresh_from_bottom_end_label));
        View scrollViewChild = mRefreshableView.getChildAt(0);
        if (null != scrollViewChild) {
            return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
        }
        return false;
    }

    @TargetApi(9)
    final class InternalScrollViewSDK9 extends CustomPullToEnlargePicScrollView {

        public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshSupportListViewInScrollViewToEnlargePic.this, deltaX, scrollX, deltaY, scrollY, getScrollRange(), isTouchEvent);

            return returnValue;
        }

        /**
         * Taken from the AOSP ScrollView source
         */
        private int getScrollRange() {
            int scrollRange = 0;
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
            }
            return scrollRange;
        }
    }


    @Override
    public void findListView() { ((CustomPullToEnlargePicScrollView)getRefreshableView()).findListView(); }

    @Override
    public void setIngoreListViewScroll(boolean bIngore) {
        ((CustomPullToEnlargePicScrollView)getRefreshableView()).setIngoreListViewScroll(bIngore);
    }

    public ScrollView getChild(){
        return mRefreshableView;
    }

    public void setNotifyHeaderView(INotifyHeaderView notifyHeaderView){
        ((CustomPullToEnlargePicScrollView)getRefreshableView()).setNotifyHeaderViewListener(notifyHeaderView);
    }

    @Override
    public void setOnSuperScrollViewScrollListener(SupportListViewInScrollView.onSuperScrollViewScrollListener listener) {
        ((CustomPullToEnlargePicScrollView)getRefreshableView()).setOnSuperScrollViewScrollListener(listener);
    }
}
