package com.zhly.scrolldemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author 刘洋巴金
 * @date 2017-4-27
 * <p>
 * 定义我们自己的布局
 */
public class LoveLayout extends RelativeLayout {

    private Context context;
    private LayoutParams params;
    private Drawable[] icons = new Drawable[4];
    private Interpolator[] interpolators = new Interpolator[4];
    private int mWidth;
    private int mHeight;

    public LoveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {

        // 图片资源
        icons[0] = getResources().getDrawable(R.drawable.green);
        icons[1] = getResources().getDrawable(R.drawable.purple);
        icons[2] = getResources().getDrawable(R.drawable.red);
        icons[3] = getResources().getDrawable(R.drawable.yellow);

        // 插值器
        interpolators[0] = new AccelerateDecelerateInterpolator(); // 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
        interpolators[1] = new AccelerateInterpolator();  // 在动画开始的地方速率改变比较慢，然后开始加速
        interpolators[2] = new DecelerateInterpolator(); // 在动画开始的地方快然后慢
        interpolators[3] = new LinearInterpolator();  // 以常量速率改变

        int width = icons[0].getIntrinsicWidth() / 2;
        int height = icons[0].getIntrinsicWidth() / 2;
        params = new LayoutParams(width, height);
        params.addRule(CENTER_HORIZONTAL, TRUE);
        params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void addLoveView() {
        for (int i = 0; i < 3; i++) {
            final ImageView iv = new ImageView(context);
            iv.setLayoutParams(params);
            iv.setImageDrawable(icons[new Random().nextInt(4)]);
            addView(iv);
            // 开启动画，并且用完销毁
            AnimatorSet set = getAnimatorSet(iv);
            set.start();
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    removeView(iv);
                }
            });
        }
    }

    /**
     * 获取动画集合
     *
     * @param iv
     */
    private AnimatorSet getAnimatorSet(ImageView iv) {

        // 1.alpha动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(iv, "alpha", 0.3f, 1f);

        // 2.缩放动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(iv, "scaleX", 0.3f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(iv, "scaleY", 0.3f, 1f);

        // 动画集合
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, scaleX, scaleY);
        set.setDuration(500);
        set.setStartDelay(200);
        // 贝塞尔曲线动画
        ValueAnimator bzier = getBzierAnimator(iv);

        AnimatorSet set2 = new AnimatorSet();
        set2.playSequentially(set, bzier);
        set2.setTarget(iv);
        return set2;
    }

    /**
     * 贝塞尔动画
     */
    private ValueAnimator getBzierAnimator(final ImageView iv) {
        // TODO Auto-generated method stub
        PointF[] PointFs = getPointFs(iv); // 4个点的坐标
        BasEvaluator evaluator = new BasEvaluator(PointFs[1], PointFs[2]);
        ValueAnimator valueAnim = ValueAnimator.ofObject(evaluator, PointFs[0], PointFs[3]);
        valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF p = (PointF) animation.getAnimatedValue();
                iv.setX(p.x);
                iv.setY(p.y);
                iv.setAlpha(1 - animation.getAnimatedFraction()); // 透明度
            }
        });
        valueAnim.setTarget(iv);
        valueAnim.setRepeatCount(ValueAnimator.INFINITE);
        valueAnim.setRepeatMode(ValueAnimator.RESTART);
        valueAnim.setDuration(3000);
        valueAnim.setInterpolator(interpolators[new Random().nextInt(4)]);
        return valueAnim;
    }

    private PointF[] getPointFs(ImageView iv) {
        // TODO Auto-generated method stub
        PointF[] PointFs = new PointF[4];
        PointFs[0] = new PointF(); // p0
        PointFs[0].x = (mWidth - params.width) / 2;
        PointFs[0].y = mHeight - params.height;

        PointFs[1] = new PointF(); // p1
        PointFs[1].x = new Random().nextInt(mWidth);
        PointFs[1].y = new Random().nextInt(mHeight / 2) + mHeight / 2 + params.height;

        PointFs[2] = new PointF(); // p2
        PointFs[2].x = new Random().nextInt(mWidth);
        PointFs[2].y = new Random().nextInt(mHeight / 2);

        PointFs[3] = new PointF(); // p3
        PointFs[3].x = new Random().nextInt(mWidth);
        PointFs[3].y = 0;
        return PointFs;
    }
}