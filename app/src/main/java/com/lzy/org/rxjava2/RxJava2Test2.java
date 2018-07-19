package com.lzy.org.rxjava2;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 类
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 * https://github.com/linzhiyong
 * https://www.jianshu.com/u/e664ba5d0800
 * @time 2018/7/19
 * @desc
 */
public class RxJava2Test2 {

    private static final String TAG = RxJava2Test2.class.getName();

    public void test() {
        Observable_create1();
    }

    private void Observable_create1() {
        // 使用create创建被观察者，定义事件
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                // 使用Emitter事件发射器发射事件
                emitter.onNext("这是事件1");
                emitter.onNext("这是事件2");
//                emitter.onError(new Exception("这里事件发生了异常。"));
                emitter.onNext("这是事件3");
                emitter.onNext("这是事件4");
                emitter.onComplete();

                Log.i(TAG, "--被观察者--threadId--" + Thread.currentThread().getId());
            }
        })
                .subscribeOn(Schedulers.single())
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.single())
                .observeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread());

        // 定义观察者，接收事件
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                // 订阅成功回调该方法，返回控制对象
                // d.dispose();
                Log.i(TAG, "--onSubscribe--");
                Log.i(TAG, "--订阅成功--threadId--" + Thread.currentThread().getId());
            }

            @Override
            public void onNext(String s) {
                // 这里接收被观察者发出的事件
                Log.i(TAG, "--onNext--" + s);
                Log.i(TAG, "--观察者--threadId--" + Thread.currentThread().getId());
            }

            @Override
            public void onError(Throwable e) {
                // 错误事件
                Log.i(TAG, "--onError--" + e.getMessage());
            }

            @Override
            public void onComplete() {
                // 完成事件
                Log.i(TAG, "--onComplete--");
            }
        };

        // 观察者订阅被观察者
        observable.subscribe(observer);
    }

}
