package com.handmark.pulltorefresh.library;

import com.handmark.pulltorefresh.library.extras.SupportListViewInScrollView;

public interface IListViewInScrollView {
	 void findListView();
     void setIngoreListViewScroll(boolean bIngore);
     void setOnSuperScrollViewScrollListener(SupportListViewInScrollView.onSuperScrollViewScrollListener listener);
}
