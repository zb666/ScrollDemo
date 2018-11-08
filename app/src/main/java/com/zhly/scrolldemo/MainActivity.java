package com.zhly.scrolldemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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

    private boolean isFinish = false;

    private boolean isStart = true;
    private boolean isIDELFinish = false;

    private List<String> mList = new ArrayList<>();

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
        llFloatDemo.startFloatAnim();
        llFloatDemo.setClickable(false);

    }
}
