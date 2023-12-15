package com.example.callback;

public interface VoidCallBack<T> {
    void onSucceed(T t);
    void onFail();
}