package com.zhly.scrolldemo;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * 自定义估值器
 */
public class BasEvaluator implements TypeEvaluator<PointF> {

    private PointF p1;
    private PointF p2;

    public BasEvaluator(PointF p1, PointF p2) {
        super();
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public PointF evaluate(float t, PointF p0, PointF p3) {
        // TODO Auto-generated method stub
        PointF pointf = new PointF();

        // 贝塞尔曲线  p0*(1-t)^3 + 3p1*t*(1-t)^2 + 3p2*t^2*(1-t) + p3^3
        pointf.x = p0.x * (1 - t) * (1 - t) * (1 - t)
                + 3 * p1.x * t * (1 - t) * (1 - t)
                + 3 * p2.x * t * t * (1 - t)
                + p3.x * t * t * t;
        pointf.y = p0.y * (1 - t) * (1 - t) * (1 - t)
                + 3 * p1.y * t * (1 - t) * (1 - t)
                + 3 * p2.y * t * t * (1 - t)
                + p3.y * t * t * t;
//		pointf.x = (float) (p0.x * Math.pow(1-t, 3) + 3 * p1.x * t * Math.pow(1-t, 2) + 3 * p2.x * Math.pow(t, 2) * (1-t) + Math.pow(p3.x, 3)); 
//		pointf.y = (float) (p0.y * Math.pow(1-t, 3) + 3 * p1.y * t * Math.pow(1-t, 2) + 3 * p2.y * Math.pow(t, 2) * (1-t) + Math.pow(p3.y, 3)); 
        return pointf;
    }
}