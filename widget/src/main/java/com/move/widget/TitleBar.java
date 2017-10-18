package com.move.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by cxj on 2017/6/23.
 * 项目中所有标题栏
 */
public class TitleBar extends RelativeLayout implements View.OnClickListener {

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        isFitsystem = a.getBoolean(R.styleable.TitleBar_titlebar_title_fitsystem, true);

        isHaveLine = a.getBoolean(R.styleable.TitleBar_titlebar_title_ishave_line, true);
        bottomLineColor = a.getColor(R.styleable.TitleBar_titlebar_title_bottom_line_color, BOTTOMLINECOLOR);


        titleBg = a.getDrawable(R.styleable.TitleBar_titlebar_title_bg);
        titleBgColor = a.getColor(R.styleable.TitleBar_titlebar_title_bg, Color.WHITE);
        titleHeight = a.getDimensionPixelSize(R.styleable.TitleBar_titlebar_title_height, ViewGroup.LayoutParams.WRAP_CONTENT);

        backDrwable = a.getDrawable(R.styleable.TitleBar_titlebar_title_back_img);
        imgBackPadding = a.getDimensionPixelSize(R.styleable.TitleBar_titlebar_title_back_img_padding, dpToPx(context, IMG_BACK_PADDING));
        imgBackMarginLeft = a.getDimensionPixelSize(R.styleable.TitleBar_titlebar_title_back_img_margin_left, dpToPx(context, IMG_BACK_MARGINLEFT));
        imgBackWidth = a.getDimensionPixelSize(R.styleable.TitleBar_titlebar_title_back_img_width, dpToPx(context, IMG_BACK_WIDTH));
        imgBackHeight = a.getDimensionPixelSize(R.styleable.TitleBar_titlebar_title_back_img_height, dpToPx(context, IMG_BACK_HEIGHT));

        title = a.getString(R.styleable.TitleBar_titlebar_title_text);
        titleTextSize = a.getDimensionPixelSize(R.styleable.TitleBar_titlebar_title_text_size, spToPx(context, TITLE_TEXTSIZE));
        titleColor = a.getColor(R.styleable.TitleBar_titlebar_title_text_color, TITLE_TEXTCOLOR);

        menuText = a.getString(R.styleable.TitleBar_titlebar_title_menu_text);
        menuColor = a.getColor(R.styleable.TitleBar_titlebar_title_menu_color, TEXT_MENU_TEXTCOLOR);
        menuTextSize = a.getDimensionPixelSize(R.styleable.TitleBar_titlebar_title_menu_textsize, spToPx(context, TEXT_MENU_TEXTSIZE));
        menuMarginRight = a.getDimensionPixelSize(R.styleable.TitleBar_titlebar_title_menu_margin_right, MENU_RIGHTMARGIN);

        menuDrwable = a.getDrawable(R.styleable.TitleBar_titlebar_title_menu_img);
        imgMenuPadding = a.getDimensionPixelSize(R.styleable.TitleBar_titlebar_title_menu_img_padding, dpToPx(context, IMG_MENU_PADDING));
        imgMenuWidth = a.getDimensionPixelSize(R.styleable.TitleBar_titlebar_title_menu_img_width, dpToPx(context, IMG_MENU_WIDTH));
        imgMenuHeight = a.getDimensionPixelSize(R.styleable.TitleBar_titlebar_title_menu_img_height, dpToPx(context, IMG_MENU_HEIGHT));


        a.recycle();

        onConfigRead();

        init(context);

    }

    /**
     * after read the config
     */
    protected void onConfigRead() {
    }


    private int TITLE_TEXTSIZE = 18;

    private int MENU_RIGHTMARGIN = 10;  // dp单位
    private int IMG_MENU_PADDING = 8;   // dp单位
    private int IMG_MENU_WIDTH = 40;    // dp单位
    private int IMG_MENU_HEIGHT = 40;   // dp单位
    private int TEXT_MENU_TEXTSIZE = 16;
    private int TEXT_MENU_TEXTCOLOR = Color.BLACK;
    private int TEXT_MENU_TEXTCOLOR_DISABLED = Color.parseColor("#999999");

    private int IMG_BACK_MARGINLEFT = 4;  // dp单位
    private int IMG_BACK_PADDING = 4;     // dp单位
    private int IMG_BACK_WIDTH = 40;      // dp单位
    private int IMG_BACK_HEIGHT = 40;     // dp单位

    private int BOTTOMLINECOLOR = Color.parseColor("#CCCCCC");
    private int TITLE_TEXTCOLOR = Color.BLACK;

    // 自定义属性的值

    private int textTypedValue = TypedValue.COMPLEX_UNIT_PX;

    protected String title = "";
    protected int titleColor;
    protected int titleTextSize;

    protected int imgBackMarginLeft;   // px单位
    protected int imgBackPadding;   // px单位
    protected int imgBackWidth;     // px单位
    protected int imgBackHeight;    // px单位

    protected String menuText = "";
    protected int menuColor;
    protected int menuTextSize;
    protected int menuMarginRight;  // px单位


    protected Drawable menuDrwable = null;
    protected int imgMenuPadding;   // px单位
    protected int imgMenuWidth;     // px单位
    protected int imgMenuHeight;    // px单位


    protected boolean isFitsystem;

    protected boolean isHaveLine;
    protected int bottomLineColor;


    protected Drawable titleBg = null;
    protected int titleBgColor;
    protected int titleHeight;
    protected Drawable backDrwable = null;


    protected ImageView iv_back;
    protected TextView tv_menu;
    protected ImageView iv_menu;
    protected TextView tv_title;

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {

        if (titleBg == null) {
            setBackgroundColor(titleBgColor);
        } else {
            if (Build.VERSION.SDK_INT >= 16) {
                setBackground(titleBg);
            } else {
                setBackgroundDrawable(titleBg);
            }
        }

        RelativeLayout rl = new RelativeLayout(context);
        rl.setId(R.id.titlebar_titlebar_container);

        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, titleHeight
        );

        // 如果需要适应系统m
        if (isFitsystem) {
            lp.topMargin = getStatusHeight(context);
        }

        rl.setLayoutParams(lp);

        addBackImage(context, rl);

        addTextMenu(context, rl);

        addImageMenu(context, rl);

        addTitle(context, rl);

        addView(rl);

        addBottomLine(context, rl);

        iv_back.setOnClickListener(this);
        tv_menu.setOnClickListener(this);
        iv_menu.setOnClickListener(this);

    }

    private void addTitle(Context context, RelativeLayout rl) {
        LayoutParams lp;// 标题
        tv_title = new TextView(context);
        tv_title.setText(title);
        lp = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.addRule(CENTER_IN_PARENT);
        tv_title.setLayoutParams(lp);
        tv_title.setTextColor(titleColor);
        tv_title.setTextSize(textTypedValue, titleTextSize);
        tv_title.setMaxLines(1);
        tv_title.setEllipsize(TextUtils.TruncateAt.END);
        tv_title.setGravity(Gravity.CENTER);

        rl.addView(tv_title);
    }

    private void addImageMenu(Context context, RelativeLayout rl) {
        LayoutParams lp;
        iv_menu = new ImageView(context);
        iv_menu.setId(R.id.titlebar_titlebar_imgmenu);
        int p = imgMenuPadding;
        iv_menu.setPadding(p, p, p, p);
        lp = new LayoutParams(imgMenuWidth, imgMenuHeight);
        lp.addRule(ALIGN_PARENT_END);
        lp.addRule(CENTER_VERTICAL);
        iv_menu.setLayoutParams(lp);
        if (menuDrwable != null) {
            iv_menu.setImageDrawable(menuDrwable);
        }
        rl.addView(iv_menu);
    }

    private void addTextMenu(Context context, RelativeLayout rl) {
        LayoutParams lp;// 菜单
        tv_menu = new TextView(context);
        tv_menu.setId(R.id.titlebar_titlebar_textmenu);
        lp = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lp.addRule(ALIGN_PARENT_END);
        lp.addRule(CENTER_VERTICAL);
        lp.rightMargin = menuMarginRight;
        tv_menu.setLayoutParams(lp);
        tv_menu.setTextColor(menuColor);
        tv_menu.setTextSize(textTypedValue, menuTextSize);
        if (TextUtils.isEmpty(menuText)) {
            tv_menu.setVisibility(INVISIBLE);
        } else {
            tv_menu.setText(menuText);
        }

        rl.addView(tv_menu);
    }

    private void addBackImage(Context context, RelativeLayout rl) {
        LayoutParams lp;// 返回按钮
        iv_back = new ImageView(context);
        iv_back.setId(R.id.titlebar_titlebar_goback);
        int p = imgBackPadding;
        iv_back.setPadding(p, p, p, p);
        lp = new LayoutParams(imgBackWidth, imgBackHeight);
        lp.addRule(CENTER_VERTICAL);
        lp.leftMargin = imgBackMarginLeft;
        lp.addRule(ALIGN_PARENT_START);
        iv_back.setLayoutParams(lp);
        if (backDrwable == null) {
            iv_back.setEnabled(false);
        } else {
            iv_back.setEnabled(true);
            iv_back.setImageDrawable(backDrwable);
        }
        rl.addView(iv_back);
    }

    private void addBottomLine(Context context, RelativeLayout rl) {
        LayoutParams lp;// 如果有线条,就添加线条
        if (isHaveLine) {
            View line = new View(context);
            lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            lp.addRule(BELOW, rl.getId());
            line.setLayoutParams(lp);
            line.setBackgroundColor(bottomLineColor);
            addView(line);
        }
    }

    private OnClickListener l;

    public void setOnMenuClickListener(OnClickListener onClickListener) {
        l = onClickListener;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == tv_menu.getId() || id == iv_menu.getId()) {
            if (l != null) {
                l.onClick(v);
            }
        } else if (id == iv_back.getId()) {
            if (getContext() instanceof Activity) {
                Activity act = (Activity) getContext();
                act.finish();
            }
        }
    }

    public void setMenuText(@NonNull String text) {
        menuText = text;
        tv_menu.setText(menuText);
    }

    public String getMenuText() {
        return menuText;
    }

    public void setTitle(String text) {
        title = text;
        tv_title.setText(title);
    }

    public void setTextMenuEable(boolean b) {
        tv_menu.setEnabled(b);
        if (b) {
            tv_menu.setTextColor(TEXT_MENU_TEXTCOLOR);
        } else {
            tv_menu.setTextColor(TEXT_MENU_TEXTCOLOR_DISABLED);
        }
    }

    public void setImgMenuVisiable(boolean b){
        iv_menu.setVisibility(b?VISIBLE:INVISIBLE);
    }

    public void setTextMenuVisiable(boolean b){
        tv_menu.setVisibility(b?VISIBLE:INVISIBLE);
    }

    public void setMenuColor(int color) {
        tv_menu.setTextColor(color);
    }


    /**
     * 获得状态栏的高度
     *
     * @param context 上下文
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dpToPx(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public static int spToPx(Context context, float spVal) {
        return (int)TypedValue.applyDimension(2, spVal, context.getResources().getDisplayMetrics());
    }

}
