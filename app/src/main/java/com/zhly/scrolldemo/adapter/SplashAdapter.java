package com.zhly.scrolldemo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhly.scrolldemo.R;

/**
 * Created by zhly on 2018/11/7.
 */

public class SplashAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public SplashAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_test,item);
    }
}
