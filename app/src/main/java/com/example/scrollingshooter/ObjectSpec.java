package com.example.scrollingshooter;

import android.graphics.PointF;

abstract class ObjectSpec {

    private String mTag;
    private String mBitmapName;
    private float mSpeed;
    private PointF mSizeScale;
    private String[] mComponents;

    public ObjectSpec(String tag, String bitmapName,
                      float speed, PointF relativeScale, String[] components) {
        this.mTag = tag;
        this.mBitmapName = bitmapName;
        this.mSpeed = speed;
        this.mSizeScale = relativeScale;
        this.mComponents = components;
    }

    String getTag(){
        return mTag;
    }

    String getBitmapName(){
        return mBitmapName;
    }

    float getSpeed(){
        return mSpeed;
    }

    PointF getScale(){
        return mSizeScale;
    }

    String[] getComponents(){
        return mComponents;
    }
}
