package com.zhly.scrolldemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.zhly.scrolldemo.adapter.AnimAdapter;
import com.zhly.scrolldemo.adapter.SplashAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhly on 2018/11/8.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.splash_rv)
    RecyclerView splashRv;
    @BindView(R.id.ll_float_demo)
    LoveLayout llFloatDemo;
    @BindView(R.id.tv_demo)
    TextView tvDemo;
    @BindView(R.id.tv_zan)
    TextView tvZan;

    private boolean isFinish = false;

    private boolean isStart = true;

    private boolean isAnimFinish = false;

    private List<String> mList = new ArrayList<>();
    private Animation mStartAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        for (int i = 0; i < 200; i++) {
            mList.add("当前位置" + i);
        }
        SplashAdapter splashAdapter = new SplashAdapter(R.layout.item_layout);
        splashRv.setLayoutManager(new LinearLayoutManager(this));
        splashRv.setAdapter(splashAdapter);
        splashAdapter.setNewData(mList);

        tvDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationSet = initAnimationSet();
                v.startAnimation(animationSet);
            }
        });
        splashRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, final int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        //没有完成并且是第一次
                        if (isFinish || isStart) {
                            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_start_x);
                            animation.setAnimationListener(new AnimAdapter() {
                                @Override
                                protected void onStart() {
                                    isStart = false;
                                    isFinish = false;
                                }

                                @Override
                                protected void onEnd() {
                                    isFinish = true;
                                }
                            });
                            llFloatDemo.startAnimation(animation);
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //类似加锁的机制 每段时间只允许一次 动画的播放
                        doAnim();
                        break;
                }
            }
        });

        tvZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation startAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.anim_scale);
                tvZan.startAnimation(startAnim);
            }
        });
    }

    private void doAnim() {
        if (isFinish) {
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_end_x);
            animation.setAnimationListener(new AnimAdapter() {
                @Override
                protected void onStart() {
                }

                @Override
                protected void onEnd() {
                }
            });
            llFloatDemo.startAnimation(animation);
        }
    }

    @OnClick(R.id.ll_float_demo)
    public void onViewClicked() {
        llFloatDemo.addLoveView();
        if (mStartAnim != null) {
            mStartAnim.reset();
        }
    }

    private AnimationSet initAnimationSet() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1f, 2.3f, 1f, 2.3f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(1000);
        sa.setRepeatCount(Animation.INFINITE);// 设置循环
        AlphaAnimation aa = new AlphaAnimation(1, 0.1f);
        aa.setDuration(1000);
        aa.setRepeatCount(Animation.INFINITE);//设置循环
        as.addAnimation(sa);
        as.addAnimation(aa);
        return as;
    }


}
