package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.extras.SupportListViewInScrollView;
import com.handmark.pulltorefresh.library.internal.INotifyHeaderView;

public class PullToRefreshSupportListViewInScrollView extends PullToRefreshBase<ScrollView> implements IListViewInScrollView {
	public PullToRefreshSupportListViewInScrollView(Context context) {
		super(context);
	}

	public PullToRefreshSupportListViewInScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshSupportListViewInScrollView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshSupportListViewInScrollView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}
	
	
	@Override
	protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
		ScrollView scrollView;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			scrollView = new InternalScrollViewSDK9(context, attrs);
		} else {
			scrollView = new SupportListViewInScrollView(context, attrs);
		}
		
		scrollView.setId(R.id.scrollview);
		return scrollView;
	}

	@Override
	protected boolean isReadyForPullStart() {
		getLoadingLayoutProxy().setPullLabel(getContext().getString(R.string.pull_to_refresh_from_bottom_pull_label));
//		return (mRefreshableView.getScrollY() == 0);
		return (mRefreshableView.getScrollY() == 0) && ((SupportListViewInScrollView)mRefreshableView).scrollScrollView();
	}

	@Override
	protected boolean isReadyForPullEnd() {
		getLoadingLayoutProxy().setPullLabel(getContext().getString(R.string.pull_to_refresh_from_bottom_end_label));
		View scrollViewChild = mRefreshableView.getChildAt(0);
		if (null != scrollViewChild) {
			return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
		}
		return false;
	}

	@TargetApi(9)
	final class InternalScrollViewSDK9 extends SupportListViewInScrollView {

		public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshSupportListViewInScrollView.this, deltaX, scrollX, deltaY, scrollY, getScrollRange(), isTouchEvent);

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
	public void findListView() {
		((SupportListViewInScrollView)getRefreshableView()).findListView();
	}

    @Override
    public void setIngoreListViewScroll(boolean bIngore) {
        ((SupportListViewInScrollView)getRefreshableView()).setIngoreListViewScroll(bIngore);
    }

	public void setNotifyHeaderView(INotifyHeaderView notifyHeaderView){
		((SupportListViewInScrollView)getRefreshableView()).setNotifyHeaderViewListener(notifyHeaderView);
	}

	@Override
	public void setOnSuperScrollViewScrollListener(SupportListViewInScrollView.onSuperScrollViewScrollListener listener) {
		((SupportListViewInScrollView)getRefreshableView()).setOnSuperScrollViewScrollListener(listener);
	}

	public void setIgnoreHeaderView(View view){
		((SupportListViewInScrollView)getRefreshableView()).setIngoreHeaderView(view);
	}

	public void setOnSingleTapUpListener(SupportListViewInScrollView.onSingeTapUpListener listener){
		((SupportListViewInScrollView)getRefreshableView()).setOnSingleTapUpListener(listener);
	}
}
