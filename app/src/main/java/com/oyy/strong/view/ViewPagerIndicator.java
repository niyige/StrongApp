package com.oyy.strong.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oyy.strong.R;
import com.oyy.strong.utils.Utils;


/**
 * ViewPager +tab栏
 * Created by ouyangyi
 * on 2016/5/13.
 */
public class ViewPagerIndicator extends HorizontalScrollView implements
        OnPageChangeListener, OnClickListener {

    private RelativeLayout[] tabArr;// tab数组
    private String[] tabTexts;// tab标题数组
    private int[] tabTextWidthArr;// tab标题宽度数组

    private int currTabPosition;// 当前选中的tab
    private float fromTransX;// 游标在启动动画之前的X轴坐标
    private int tabSpacing;// tab之间的间距
    private int cursorPadding;// tab默认和cursor长度一样
    private int mTextColor = getResources().getColor(R.color.gray_666666);
    private float mTextSize = 15;
    private int tabLength;// tab个数
    private int diviceWidth;// 设备宽度px
    private float fromScaleX = 1.0f;
    private int baseCursorWidth;// 初始化的游标宽度, 缩放动画每次以此为标准
    private Boolean autoArrange = true;
    private Boolean isPageFirstScrolled = true;
    private int unSelectTextColor = getResources().getColor(R.color.gray_999999); //设置未选中字体的颜色
    private int selectTextColor = getResources().getColor(R.color.red_ff3254); //设置选中字体的颜色


    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout tabLy;// tab的横向线性布局
    private ImageView cursor;// 游标
    private Handler handler = new Handler();
    private View line, bottomLine;
    private RelativeLayout mainLayout;

    private int txtColor = -1;  //tab文字颜色


    public ViewPagerIndicator(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    /**
     * 初始化基本布局
     */
    private void init() {
        // TODO Auto-generated method stub
        mContext = getContext();
        diviceWidth = Utils.getInstance().getWidth(mContext);
        // 设置属性
        this.setBackgroundColor(getResources().getColor(R.color.white));
        this.setHorizontalScrollBarEnabled(false);// 去掉滚动条
        this.setVisibility(View.GONE);
        // 加载ScrollView的内容布局
        View v = View.inflate(mContext, R.layout.viewpager_indicator_layout, null);
        tabLy = (LinearLayout) v.findViewById(R.id.main_tab);// Tab布局
        cursor = (ImageView) v.findViewById(R.id.main_cursor);// 游标
        mainLayout = (RelativeLayout) v.findViewById(R.id.mainLayout);
        line = v.findViewById(R.id.line); //获得中间那条线
        bottomLine = v.findViewById(R.id.bottom_Line); //底部那条线
       // bottomLine.setLayoutParams(new LinearLayout.LayoutParams(diviceWidth, Utils.getInstance().dipTopx(mContext, 1)));
        this.addView(v);
    }


    public void setLinevisibility(boolean isShow) {
        bottomLine.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 修改字体样式
     *
     * @param tabBg
     * @param unSelectTextColor * @param selectTextColor
     */
    public void setStyle(int tabBg, int unSelectTextColor, int selectTextColor, int cursorBg) {
        mainLayout.setBackgroundResource(tabBg);
        cursor.setBackgroundResource(cursorBg);
        bottomLine.setBackgroundColor(selectTextColor);
        this.unSelectTextColor = unSelectTextColor;
        this.selectTextColor = selectTextColor;
    }

    /**
     * 设置参数.
     *
     * @param tabSpacing    tab间距
     * @param cursorPadding 游标向左右延伸的距离, 默认为0, 也即和tab文本一样长
     * @param viewPager     设置ViewPager
     * @param tabTexts      tab内容数组
     * @param autoArrange   当tab长度不足屏幕宽度时, 是否自动进行摆列, 默认为true
     */
    public void setParams(int tabSpacing, int cursorPadding, String[] tabTexts,
                          ViewPager viewPager, Boolean autoArrange) {
        if (tabTexts == null || tabTexts.length < 1) {
            return;
        }
        this.tabSpacing = tabSpacing;
        this.cursorPadding = cursorPadding;
        this.tabTexts = tabTexts;
        this.mViewPager = viewPager;
        this.autoArrange = autoArrange;

        this.setVisibility(View.VISIBLE);
        initTabSpacing();
        initTabs();
        initCursor();
        onPageSelected(currTabPosition);// 初始化tab样式和动画
        if (mViewPager != null) {
            mViewPager.addOnPageChangeListener(this);
        }
    }

    /**
     * 确定tab间隙长度
     */
    private void initTabSpacing() {
        // TODO Auto-generated method stub
        tabLength = tabTexts.length;
        int tabTextsWidth = 0;// tab文本总长度
        TextView tv = new TextView(mContext);
        tabTextWidthArr = new int[tabLength];
        for (int i = 0; i < tabLength; i++) {
            tv.setTextSize(mTextSize);
            tabTextWidthArr[i] = (int) tv.getPaint().measureText(tabTexts[i]);
            tabTextsWidth += tabTextWidthArr[i];
        }
        int scrollViewWidth = tabTextsWidth + tabLength * tabSpacing;
        // 默认, tab(包括间隙)总长度小于屏幕宽度时, 重新计算间距, 让各个tab均匀填满scrollview
        if (autoArrange && scrollViewWidth < diviceWidth) {
            tabSpacing = (diviceWidth - tabTextsWidth) / tabLength;
        }
    }

    /**
     * 初始化Tab
     */
    private void initTabs() {
        tabLy.removeAllViews();
        tabArr = new RelativeLayout[tabLength];
        TextView tabTextView = null;
        for (int i = 0; i < tabLength; i++) {
            // 初始化tabArr
            LinearLayout.LayoutParams tabParam = new LinearLayout.LayoutParams(
                    tabTextWidthArr[i] + tabSpacing, LayoutParams.MATCH_PARENT);
            tabArr[i] = (RelativeLayout) View.inflate(mContext,
                    R.layout.viewpager_indicator_tab, null);
            tabArr[i].setLayoutParams(tabParam);
            tabArr[i].setTag(i);
            tabArr[i].setOnClickListener(this);
            // 初始化tabTextViewArr
            tabTextView = (TextView) tabArr[i].findViewById(R.id.tab_tv);
            tabTextView.setTextSize(mTextSize);
            tabTextView.setTextColor(mTextColor);
            tabTextView.setText(tabTexts[i]);
            // 添加子tab
            tabLy.addView(tabArr[i], i);
        }
    }

    @Override
    public void onClick(View v) {
        int i = (Integer) v.getTag();
        setTabClick(i);
    }

    /**
     * 设置当前Tab点击时事件
     *
     * @param position
     */
    private void setTabClick(int position) {
        // 如果点击的为当前的Tab，则滚回到顶部， 否则切换
        if (position == currTabPosition) {

        } else {
            mViewPager.setCurrentItem(position);
        }
    }

    /**
     * 初始化cursor长度
     */
    private void initCursor() {
        // TODO Auto-generated method stub
        baseCursorWidth = tabTextWidthArr[currTabPosition] + cursorPadding;
        RelativeLayout.LayoutParams cursorLp = (RelativeLayout.LayoutParams) cursor
                .getLayoutParams();
        cursorLp.width = baseCursorWidth;
        cursor.setLayoutParams(cursorLp);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(final int position) {
        // TODO Auto-generated method stub
        currTabPosition = position;
        setTabStyle(position);// 改变tab字体颜色
        handler.post(new Runnable() {

            @Override
            public void run() {
                setTabAnimation(position);
            }
        });

        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(currTabPosition);
        }

        if (onPageBackHandlerListener != null) {
            onPageBackHandlerListener.pageSelectHandler(currTabPosition);
        }
    }


    /**
     * 设置未选中tab字体的颜色
     */
    public void setUnSelectTextColor(int UnSelectTextColor) {
        this.unSelectTextColor = UnSelectTextColor;
    }

    /**
     * 设置选中tab字体的颜色
     */
    public void setSelectTextColor(int selectTextColor) {
        this.selectTextColor = selectTextColor;
    }

    /**
     * 设置tab字体的大小
     */
    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
    }


    /**
     * 设置选中项背景和字体颜色
     *
     * @param position
     */
    private void setTabStyle(int position) {
        // 还原Tab的背景和字体颜色
        for (int i = 0; i < tabLength; i++) {
            ((TextView) tabArr[i].getChildAt(0)).setTextColor(unSelectTextColor);
        }
        // 改变 选中文本的颜色, 同时红点消失
        ((TextView) tabArr[position].getChildAt(0)).setTextColor(selectTextColor);
    }

    /**
     * 设置tab的动画
     *
     * @param position
     */
    private void setTabAnimation(final int position) {
        // 选中的tab中心位置到scrollview最左边位置的距离
        int offset = 0;
        for (int i = 0; i < position; i++) {
            offset += tabTextWidthArr[i] + tabSpacing;
        }
        offset += (tabTextWidthArr[position] + tabSpacing) / 2;
        // tab滚动到居中位置
        int scrollOffset = offset - diviceWidth / 2 <= 0 ? 0 : offset
                - diviceWidth / 2;
        smoothScrollTo(scrollOffset, 0);

        // 移动动画
        offset -= (tabTextWidthArr[0] / 2 + cursorPadding / 2);

        Animation translateAnim = new TranslateAnimation(isPageFirstScrolled ? offset : fromTransX, offset, 0,
                0);
        fromTransX = offset;
        isPageFirstScrolled = false;

        // 缩放动画, 以初始化的cursor为缩放参考
        offset = tabTextWidthArr[currTabPosition] + cursorPadding;
        float toScaleX = (float) offset / (float) baseCursorWidth;
        Animation scaleAnim = new ScaleAnimation(fromScaleX, toScaleX, 1.0f,
                1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);// 中间为缩放中心
        fromScaleX = toScaleX;

        // 将两个动画添加进集合, 同时执行
        AnimationSet anim = new AnimationSet(true);
        anim.addAnimation(scaleAnim); // 注意: 先添加缩放动画, 否则会出问题, 原因不知
        anim.addAnimation(translateAnim);
        anim.setDuration(300);
        anim.setFillAfter(true);
        cursor.startAnimation(anim); // 开始动画
    }


    private OnPageChangeListener onPageChangeListener;

    public void setOnPageChangeListener(
            OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    //处理返回跟viewpager滑动冲突问题
    public interface OnPageBackHandlerListener {
        void pageSelectHandler(int position);
    }

    private OnPageBackHandlerListener onPageBackHandlerListener;


    public void setOnPageBackHandlerListener(OnPageBackHandlerListener onPageBackHandlerListener) {
        this.onPageBackHandlerListener = onPageBackHandlerListener;
    }
}
