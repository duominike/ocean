package com.handmark.pulltorefresh.library.extras;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.R;
import com.handmark.pulltorefresh.library.internal.INotifyHeaderView;
import com.handmark.pulltorefresh.library.internal.Utils;

import java.lang.reflect.Field;

public class SupportListViewInScrollView extends ScrollView {
	private String Tag = "SupportListViewInScrollView";

	/**
	 *  is should scroll ScrollView
	 */
	private boolean m_bSelfScroll = true;
	private boolean m_bMakeEvent = false;

	private boolean m_bIsForceScrollListView = false;
	private boolean m_bIsFroceScrollSelf = false;
	private boolean m_bIsonFiling = false;

	private boolean m_bIngoreListView = false;
	private INotifyHeaderView m_notify;
	private boolean m_bNotifyValidate = true;

	private boolean is_ListViewScrollToBottom = false;

	/**
	 * listview included in ScrollView
	 */
	private AbsListView m_ListView;

	private View m_IgnoreView;

	/**
	 * is scroll from up to down
	 */
	private boolean m_bIsUptoDown = false;

	private GestureDetector mGeDetector;

	private View m_HeaderView;

	private enum GestureState {
		gsEMPTY, gsONDOWN, gsONSCROLLVertical, gsONSCROLLHORIZONTAL, gsONSINGLETAPUP,
	}

	private GestureState m_gestureState = GestureState.gsEMPTY;
	private onSingeTapUpListener m_singleTapUpListener;
	public SupportListViewInScrollView(Context context) {
		super(context);
		init(context, null);
	}

	public SupportListViewInScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SupportListViewInScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private int m_HeaderViewId = 0;
	private int m_IgnoreViewId = 0;
	private float m_lastY = -1;

	private void init(Context context, AttributeSet attrs) {
		m_bHaveFind = false;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefresh);
		if (a.hasValue(R.styleable.PullToRefresh_ptrSlideHeaderView)) {
			m_HeaderViewId = a.getResourceId(R.styleable.PullToRefresh_ptrSlideHeaderView, 0);
			m_HeaderView = findViewById(m_HeaderViewId);
		}
		if (a.hasValue(R.styleable.PullToRefresh_ptrIgnoreHeaderView)) {
			m_IgnoreViewId = a.getResourceId(R.styleable.PullToRefresh_ptrIgnoreHeaderView, 0);
			m_IgnoreView = findViewById(m_IgnoreViewId);
		}
		a.recycle();

		mGeDetector = new GestureDetector(new OnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent arg0) {
				m_gestureState = GestureState.gsONSINGLETAPUP;
				return false;
			}

			@Override
			public void onShowPress(MotionEvent arg0) {
			}

			@Override
			public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
				if (m_lastY == -1) {
					m_bIsUptoDown = arg0.getY() + 5 < arg1.getY();
					m_lastY = arg1.getY();
				} else {
					m_bIsUptoDown = m_lastY + 1.2 < arg1.getY();
					m_lastY = arg1.getY();
				}
				float angle = Math.abs(arg0.getY() - arg1.getY()) / Math.abs(arg0.getX() - arg1.getX());
				if (angle > 1.2) {
					m_gestureState = GestureState.gsONSCROLLVertical;
				} else if (angle < 0.8) {
					m_gestureState = GestureState.gsONSCROLLHORIZONTAL;
				} else {
					m_gestureState = GestureState.gsEMPTY;
				}
				//				if (Math.abs(arg0.getY() - arg1.getY()) >= Math.abs(arg0.getX() - arg1.getX())) {
				//					m_gestureState = GestureState.gsONSCROLLVertical;
				//				}else{
				//					m_gestureState = GestureState.gsONSCROLLHORIZONTAL;
				//				}
				m_bIsonFiling = false;
				return false;
			}

			@Override
			public void onLongPress(MotionEvent arg0) {
			}

			@Override
			public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
				m_bIsonFiling = true;
				return false;
			}

			@Override
			public boolean onDown(MotionEvent arg0) {
				m_gestureState = GestureState.gsONDOWN;
				m_bIsonFiling = true;
				return false;
			}
		});

	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (null != m_IgnoreView) {
			if (Utils.isInView(event, m_IgnoreView)) {
				return false;
			}
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE && m_gestureState == GestureState.gsONSCROLLVertical) {
			handleActionMove(event);
			if (m_bSelfScroll) {
				return true;
			} else {
				return false;
			}
		}
		return super.onInterceptTouchEvent(event);
	}

	float x = 0;
	float y = 0;
	float x1 = 0;
	float y1 = 0;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (null != m_IgnoreView) {
			if (Utils.isInView(ev, m_IgnoreView)) {
				return false;
			}
		}
		if (ev.getAction() == MotionEvent.ACTION_MOVE && m_gestureState == GestureState.gsONSCROLLVertical) {
			if (m_bSelfScroll) {
				if (shouldScrollListView(ev)) {
					m_bSelfScroll = false;
					float _x = ev.getX();
					float _y = ev.getY();
					ev.setAction(MotionEvent.ACTION_DOWN);
					ev.setLocation(x1, y1);
					m_bMakeEvent = true;
					dispatchTouchEvent(ev);
					ev.setAction(MotionEvent.ACTION_MOVE);
					dispatchTouchEvent(ev);
					ev.setLocation(x, y);
					dispatchTouchEvent(ev);
					ev.setLocation(_x, _y);
					dispatchTouchEvent(ev);
					m_bMakeEvent = false;
					return false;
				}
			}
		}
		x1 = x;
		y1 = y;
		x = ev.getX();
		y = ev.getY();
		try{
			return super.onTouchEvent(ev);
		} catch (Exception e){
			return m_bSelfScroll;
		}

	}

	float dx;
	float dy;
	float dx1;
	float dy1;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (!m_bMakeEvent) {
			mGeDetector.onTouchEvent(ev);
		}

		if(ev.getAction() == MotionEvent.ACTION_DOWN && m_singleTapUpListener != null){
			m_singleTapUpListener.onSingleTapUp();
		}
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			m_gestureState = GestureState.gsEMPTY;
			m_lastY = -1;
		}

		if (ev.getAction() == MotionEvent.ACTION_MOVE && !m_bSelfScroll && m_gestureState == GestureState.gsONSCROLLVertical) {
			if (shouldScrollSelf(ev)) {
				m_bSelfScroll = true;
				if (!m_bMakeEvent) {
					float _x = ev.getX();
					float _y = ev.getY();
					ev.setAction(MotionEvent.ACTION_DOWN);
					m_bMakeEvent = true;
					ev.setLocation(dx1, dy1);
					dispatchTouchEvent(ev);
					ev.setAction(MotionEvent.ACTION_MOVE);
					dispatchTouchEvent(ev);
					ev.setLocation(dx, dy);
					dispatchTouchEvent(ev);
					ev.setLocation(_x, _y);
					dispatchTouchEvent(ev);
					m_bMakeEvent = false;
				} else {
					m_bMakeEvent = false;
				}

			}
		}
		dx1 = dx;
		dy1 = dy;
		dx = ev.getX();
		dy = ev.getY();
		return super.dispatchTouchEvent(ev);
	}

	private void handleActionMove(MotionEvent ev) {
		if (m_bSelfScroll) {
			if (shouldScrollListView(ev)) {
				m_bSelfScroll = false;
			}
		} else {
			if (shouldScrollSelf(ev)) {
				m_bSelfScroll = true;
			}
		}
	}

	/**
	 * 
	 * @param ev
	 * @return
	 */
	private boolean shouldScrollSelf(MotionEvent ev) {
		return !shouldScrollListView(ev);

	}

	private int[] location = new int[2];
	/**
	 * ListView location in window
	 */
	private int[] listViewLoc = new int[2];

	/**
	 *  header init location in window
	 */
	private int[] initHeaderViewLocation = new int[2];

	/**
	 * judge is ListView scroll TO Top
	 * @return
	 */
	public boolean isListViewScrollToTop() {
		if (null == m_ListView){
			return false;
		}
		if (m_ListView.getFirstVisiblePosition() <= 0) {
			final View firstVisiableChild = m_ListView.getChildAt(0);
			if (firstVisiableChild != null) {
				firstVisiableChild.getLocationInWindow(location);
				m_ListView.getLocationInWindow(listViewLoc);
				return location[1] >= listViewLoc[1];
			}
		}
		return false;

	}

	public boolean scrollScrollView(){
		return m_bSelfScroll;
	}
	/**
	 * ListView是否滚动到底部
	 * @return
	 */
	private boolean isListViewScrollToBottom() {
		final int lastItemPos = m_ListView.getCount() - 1;
		final int lastVisiblePos = m_ListView.getLastVisiblePosition();
		if (lastVisiblePos >= lastItemPos - 1) {
			final int childIndex = lastVisiblePos - m_ListView.getFirstVisiblePosition();
			final View lastVisableChild = m_ListView.getChildAt(childIndex);
			if (lastVisableChild != null) {
				lastVisableChild.getLocationInWindow(location);
				m_ListView.getLocationInWindow(listViewLoc);
				return location[1] + lastVisableChild.getHeight() <= listViewLoc[1] + m_ListView.getBottom();
			}
		}
		return false;
	}

	/**
	 * 是否应该滑动ListView
	 * @param ev
	 * @return
	 */
	private boolean shouldScrollListView(MotionEvent ev) {
		if (m_bIngoreListView) {
			return false;
		}
		if (m_ListView == null) {
			return false;
		}

		if (!Utils.isInView(ev, m_ListView)) {
			return false;
		}

		if (isListViewScrollToTop()) {
			if (m_HeaderView != null) {
				m_HeaderView.getLocationInWindow(location);
				if (initHeaderViewLocation[1] - location[1] < m_HeaderView.getHeight() - 4) {
					return false;
				}
			}
			if (!m_bIsUptoDown) {//下-->上
				if (m_bSelfScroll) {
					//通知的 headerview 隐藏
					if (m_notify != null) {
						m_notify.updateHeaderViewState(false);
					}
				}
				return true;
			} else {//上-->下
				if (!m_bSelfScroll) {
					//通知的 headerview 显示
					if (m_notify != null) {
						m_notify.updateHeaderViewState(true);
					}
				}
				return false;
			}
		}

		if (isListViewScrollToBottom()) {
			if (m_bIsUptoDown) {
				return true;
			} else {
				return true;
			}
		}
		return true;
	}

	/**
	 * find ListView
	 * @param vg
	 * @return
	 */
	private AbsListView findListViewFromChild(ViewGroup vg) {
		AbsListView listView = null;
		for (int i = 0; i < vg.getChildCount(); i++) {
			if (vg.getChildAt(i) instanceof ViewGroup && !(vg.getChildAt(i) instanceof AbsListView)) {
				listView = findListViewFromChild((ViewGroup) vg.getChildAt(i));
				if (listView != null) {
					break;
				}
			} else if (vg.getChildAt(i) instanceof AbsListView && Utils.isInWindow(getContext(), vg.getChildAt(i))) {
				listView = (AbsListView) vg.getChildAt(i);
				break;
			}
		}
		return listView;
	}

	private boolean m_bHaveFind = false;

	/**
	 * 
	 * find ListView in ScrollView
	 */
	public void findListView() {
		m_ListView = findListViewFromChild(this);
		if (m_HeaderView == null) {
			m_HeaderView = findViewById(m_HeaderViewId);
		}
		if (m_IgnoreView == null) {
			if (0 != m_IgnoreViewId) {
				m_IgnoreView = findViewById(m_IgnoreViewId);
			}
		}

		if (m_HeaderView != null && !m_bHaveFind) {
			m_bHaveFind = true;
			m_HeaderView.postDelayed(new Runnable() {
				@Override
				public void run() {
					m_HeaderView.getLocationInWindow(initHeaderViewLocation);
				}
			}, 100);
		}
		if (m_ListView != null) {
			try {
				Field onScrollF = AbsListView.class.getDeclaredField("mOnScrollListener");
				onScrollF.setAccessible(true);
				final AbsListView.OnScrollListener oonScrollListener = (AbsListView.OnScrollListener) onScrollF.get(m_ListView);
				m_ListView.setOnScrollListener(new AbsListView.OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						switch (scrollState) {
							case SCROLL_STATE_TOUCH_SCROLL:
								break;
							case SCROLL_STATE_FLING:
								if (m_bIsUptoDown && m_bIsFroceScrollSelf) {
									m_bIsFroceScrollSelf = false;
								}
								break;
							case SCROLL_STATE_IDLE:
								break;
						}
						if(oonScrollListener != null)
							oonScrollListener.onScrollStateChanged(view, scrollState);
					}
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
						if (m_bIsUptoDown && firstVisibleItem <= 0 && m_bIsonFiling && !m_bIsFroceScrollSelf) {
							m_bIsFroceScrollSelf = true;
							if (m_notify != null) {
								m_notify.updateHeaderViewState(true);
							}
							if (m_HeaderView != null) {
								SupportListViewInScrollView.this.smoothScrollBy(0, -(int) (m_HeaderView.getHeight()));
							}
						}
						if(oonScrollListener != null)
							oonScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
					}
				});
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				Toast.makeText(getContext(), "NoSuchFieldException", Toast.LENGTH_SHORT).show();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				Toast.makeText(getContext(), "IllegalAccessException", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static String MotionEventToString(MotionEvent me) {
		String result = "";
		switch (me.getAction()) {
		case MotionEvent.ACTION_DOWN:
			result = "MotionEvent.ACTION_DOWN";
			break;
		case MotionEvent.ACTION_MOVE:
			result = "MotionEvent.ACTION_MOVE";
			break;
		case MotionEvent.ACTION_CANCEL:
			result = "MotionEvent.ACTION_CANCEL";
			break;
		case MotionEvent.ACTION_UP:
			result = "MotionEvent.ACTION_UP";
			break;

		}
		return result;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (m_notify != null) {
			if (isListViewScrollToTop()) {
				m_HeaderView.getLocationInWindow(location);
				if (initHeaderViewLocation[1] - location[1] <= m_HeaderView.getHeight() - 2) {
					if (m_bSelfScroll && m_bIsUptoDown) {
						m_notify.updateHeaderViewState(true);
					}
				} else {
					if (!m_bIsUptoDown && m_bSelfScroll) {
						m_notify.updateHeaderViewState(false);
					}
					if (!m_bIsUptoDown && m_bSelfScroll && !m_bIsForceScrollListView && m_bIsonFiling) {//惯性滑动
						m_bIsForceScrollListView = true;
						m_notify.updateHeaderViewState(false);
						m_ListView.smoothScrollBy((int) (m_HeaderView.getHeight() * 1.8), 500);
						m_ListView.postDelayed(new Runnable() {

							@Override
							public void run() {
								m_bIsForceScrollListView = false;
								m_bIsonFiling = false;
							}
						}, 500);
					}
				}
			}
		}

		//		if (m_HeaderView != null && m_HeaderView.getHeight() - oldt < 10 && m_bSelfScroll &&  oldt < t && !m_bIsForceScrollListView && m_bIsonFiling) {
		//			m_bIsForceScrollListView = true;
		//          if (m_notify != null) {
		//				m_notify.updateHeaderViewState(false);
		//			}
		//		    m_ListView.smoothScrollBy((int) (m_HeaderView.getHeight()), 500);
		//			m_ListView.postDelayed(new Runnable() {
		//
		//				@Override
		//				public void run() {
		//					m_bIsForceScrollListView = false;
		//					m_bIsonFiling = false;
		//				}
		//			}, 510);
		//		}
		if (m_bSelfScroll && m_bIsUptoDown && m_bIsonFiling) {
			if (m_notify != null) {
				m_notify.updateHeaderViewState(true);
			}
		}
		if (m_onScrollViewListener != null) {
			// && 优先级高于 ||
			if ((m_bSelfScroll || m_bIsFroceScrollSelf) && m_HeaderView != null) {
				int[] tmp_location = new int[2];
				m_HeaderView.getLocationInWindow(tmp_location);
				m_onScrollViewListener.onScrollChange(m_bIsUptoDown, initHeaderViewLocation[1], tmp_location[1], m_HeaderView.getHeight());
//				m_onScrollViewListener.onScrollChange(m_bIsUptoDown, t, oldt, m_He);
			}
		}
	}

	private onSuperScrollViewScrollListener m_onScrollViewListener = null;

	public void setOnSuperScrollViewScrollListener(onSuperScrollViewScrollListener listener) {
		this.m_onScrollViewListener = listener;
	}

	public interface onSuperScrollViewScrollListener {
		void onScrollChange(boolean isUpToDown, int initHeadViewLocation, int nowHeadViewLocation, int headViewHeight);
	}

	public interface onSingeTapUpListener{
		void onSingleTapUp();
	}

	public void setIngoreListViewScroll(boolean bIngore) {
		this.m_bIngoreListView = bIngore;
	}

	public void setNotifyHeaderViewListener(INotifyHeaderView notify) {
		this.m_notify = notify;
	}

	public void setNotifyValidate(boolean bValidate) {
		this.m_bNotifyValidate = bValidate;
	}

	public boolean canFooterRefresh() {
		if (m_ListView == null) {
			return true;
		}
		return isListViewScrollToBottom();
	}

	public void setIngoreHeaderView(View view) {
		this.m_IgnoreView = view;
	}

	public void setOnSingleTapUpListener(onSingeTapUpListener listener){
		this.m_singleTapUpListener = listener;
	}
}
