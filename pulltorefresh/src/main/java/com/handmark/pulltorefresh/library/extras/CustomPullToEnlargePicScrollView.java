package com.handmark.pulltorefresh.library.extras;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/**
 * Created by liuhe on 2015/6/8.
 */
public class CustomPullToEnlargePicScrollView extends SupportListViewInScrollView {

    public CustomPullToEnlargePicScrollView(Context context) {
        super(context);
    }

    public CustomPullToEnlargePicScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPullToEnlargePicScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

   /* private void init(){
        this.getLocationInWindow(initSelfLocation);
    }*/
    private View inner;// 孩子View

    private final float touchYFlag = 10000;// add by summer
    // 用于判断ontouch事件中,touchY是否有被初始化
    private float touchY = touchYFlag;// 点击时Y坐标
    private float deltaY;// Y轴滑动的距离

    private float initTouchY;// 首次点击的Y坐标

    private boolean shutTouch = false;// 是否关闭ScrollView的滑动.

    private Rect normal = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画.)

    private boolean isMoveing = false;// 是否开始移动.

    // private ImageView imageView;// 背景图控件.
    private RelativeLayout background;// 背景图控件.

    private int initTop, initBottom;// 初始背景高度

    private int initSelfLocation[] = new int[2];

    private int current_Top, current_Bottom;// 拖动时时高度

    private View menuLayout;

    private int mMenuInitTop;

    private int mMenuCurrentLocation[] = new int[2];//当前位置

    // 状态：上部，下部，默认
    private enum State {
        UP, DOWN, NOMAL
    }

    // 默认状态
    private State state = State.NOMAL;

    private onScrollListener scrollListener;

    public void setOnScrollListener(onScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    // 注入背景图
    public void setBackground(RelativeLayout background) {
        this.background = background;
    }

    public RelativeLayout getBackgroundRl(){ return  this.background; }

    /**
     * 这是针对乐库歌手末级页 用来计处下拉的距离的，即歌手-MV菜单Y轴坐标不能低于北景图片的高度
     * 在其它的界面里，如果没有这个菜单View,传一个用来计算的View也是可以的
     *
     * @param menuLayout
     */
    public void setMenuLayout(View menuLayout) {
        this.menuLayout = menuLayout;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (background != null & menuLayout != null) {
            mPicInitTop = background.getTop();
            mPicInitBottom = background.getBottom();
            mMenuInitTop = (menuLayout != null) ? menuLayout.getTop() : 0;
        }
    }

    private int mPicInitTop;
    private int mPicInitBottom;
    // 2014.12.05 guanj 解决：当快速滑动时头图不能完全隐藏问题
    // 判定是否已经隐藏
    private boolean m_bIsHide = false;

    /**
     * 这个滚动监听是有必要的,手势滑动可以在onTouchEvent里面做处理,ScrollView的惯性滑动(即手指离开屏幕,
     * ScrollView还有速度)就只能在这里处理了
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // Log.i("de", "run!!!");
        if (t == 0) {

            state = State.NOMAL;
            // }
        }
        // add by summer +1是让图片往上再多滚一个像素,以保证图片能彻底被滚出屏幕顶消失,万无一失
        // 2014.12.05 guanj 解决：当快速滑动时头图不能完全隐藏问题
        // 当top远大于图的底部时，判定如果没有隐藏就隐藏掉
        if (t < mPicInitBottom + 1) {
            background.scrollTo(0, t);
            m_bIsHide = false;
        } else {
            if (!m_bIsHide) {
                m_bIsHide = true;
                background.scrollTo(0, mPicInitBottom);
            }
        }

        if (null != scrollListener) {
            scrollListener.onScroll();
        }
    }
    /***
     * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
     * 方法，也应该调用父类的方法，使该方法得以执行.
     */
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }

    /** touch 事件处理 **/
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner != null) {
            commOnTouchEvent(ev);
        }
        // ture：禁止控件本身的滑动.
        if (shutTouch) {
            return true;
        } else
            return super.onTouchEvent(ev);
    }

    /***
     * 触摸事件
     *
     * @param ev
     */
    public void commOnTouchEvent(MotionEvent ev) {
        //Log.e("onLayout" , mPicInitTop + " --- " + mPicInitBottom + " ---- " + " --- " + mMenuInitTop);
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                downInit(ev);
                break;
            case MotionEvent.ACTION_UP:

               /**回缩动画 */
                if (isNeedAnimation()) {
                    animation();
                }

                if (getScrollY() == 0) {
                    state = State.NOMAL;
                }

                isMoveing = false;
                touchY = 0;
                shutTouch = false;
                break;

            /***
             * 排除出第一次移动计算，因为第一次无法得知deltaY的高度， 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0.
             * 之后记录准确了就正常执行.
             */
            case MotionEvent.ACTION_MOVE:
                touchY = ev.getY();

                deltaY = touchY - initTouchY;// 滑动距离
                //

                /** 对于首次Touch操作要判断方位：UP OR DOWN **/
                if (deltaY < 0 && state == State.NOMAL) {
                    state = State.UP;
                } else if (deltaY > 0 && state == State.NOMAL) {
                    state = State.DOWN;

                    initTouchY = ev.getY(); // add by summer 2014-1-18 :23:59
                    // 这里加个判断,当往下滑动,滑到初始位置时,要对触点和距离初始化,否则会在后面的Layout里面,突然惊变位置
                    deltaY = 0; // 理论上在state == State.UP的状态理判断
                    // getScrollY==0,设置此两项也是可以的，但是慢慢滑动可以，滑快了就不行，根据日志判断是：快速滑动和慢慢滑动生成的“MOVE事件”move的距离不同，一滑快就极可能在onTouch方法里拿不到getScroll()
                    // == 0对应的事件
                    // 所以，在onTouch()方法里，尽量不要用“==”进行状态的判断

                }

                if (state == State.UP) {
                    deltaY = deltaY < 0 ? deltaY : 0;
                    isMoveing = false;
                    shutTouch = false;

                    /** line_up **/

                } else if (state == State.DOWN) {
                    if (getScrollY() <= deltaY) {

                        shutTouch = true;
                        isMoveing = true;
                    }

                    deltaY = deltaY < 0 ? 0 : deltaY; // 祸害在这里，推上去一半 ,松开,再下拉
                    // 这个deltaY值是有一定的值的,就在move事件里,突然变大

                }

                if (isMoveing) {

                    // 初始化头部矩形
                    if (normal.isEmpty()) {
                        // 保存正常的布局位置
                        normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
                    }
                    // 移动布局(手势移动的1/3)
                    int image_move_H = (int) deltaY / 4;

                    int inner_move_H = (int) deltaY / 2;

                    /** image_bg **/
                    // float image_move_H = deltaY / 10;
                    image_move_H = image_move_H < Math.abs(mPicInitTop) ? image_move_H : Math.abs(mPicInitTop);
                    if (background.getTop() < 0) {

                        current_Top = initTop + image_move_H;
                        current_Bottom = initBottom + image_move_H;

                        background.layout(background.getLeft(), current_Top, background.getRight(), current_Bottom);
                    }

                    /** inner **/
                    inner_move_H = (inner_move_H < mPicInitBottom - mPicInitTop - mMenuInitTop) ? inner_move_H : (mPicInitBottom - mPicInitTop - mMenuInitTop);

                   if (inner.getTop() < mPicInitBottom - mPicInitTop) {// mPicInitBottom
                        // -
                        // mPicInitTop
                        // 即背景图片的高度//
                        // ，
                        inner.layout(normal.left, normal.top + inner_move_H, normal.right, normal.bottom + inner_move_H);

                    } else {

                    }
                }
                break;

            default:
                break;

        }
    }

    public void downInit(MotionEvent ev) {
        initTouchY = ev.getY();
        current_Top = initTop = mPicInitTop = background.getTop();
        current_Bottom = initBottom = mPicInitBottom = background.getBottom();
    }

    /***
     * 回缩动画
     */
    public void animation() {
        TranslateAnimation image_Anim = new TranslateAnimation(0, 0, Math.abs(initTop - current_Top), 0);
        image_Anim.setDuration(700);
        background.startAnimation(image_Anim);
        background.layout(background.getLeft(), initTop, background.getRight(), initBottom);

        TranslateAnimation inner_Anim = new TranslateAnimation(0, 0, inner.getTop(), normal.top);
        inner_Anim.setDuration(700);
        inner.startAnimation(inner_Anim);
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);

        normal.setEmpty();
    }

    /** 是否需要开启动画 **/
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    /***
     * 执行翻转
     *
     * @author jia
     *
     */
    public interface onTurnListener {

        /** 必须达到一定程度才执行 **/
        void onTurn();
    }

    /**
     * 监听ScrollView 滑动 回调 当滑到顶部,显示假的遮盖menu
     */
    public interface onScrollListener {
        void onScroll();
    }

    /**
     * add by summer 2014-01-15 : 14:51
     *
     * @param yDistance
     */
    // 滑动距离及坐标

    /**
     * 判断手势方向,水平传递给ViewPager，不中断事件的传递
     */
    private float xDistance;

    private float xLast;

    private float yLast;

    private float yDistance;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        downInit(ev);

        switch (ev.getAction()) { // x轴移动大于 y轴移动 就将事件传递给ViewPager 进行左右切换
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);

    }

    public void onFinishInflateScroll(){
        onFinishInflate();
    }
}
