package com.lzy.org.rxjava2;

import android.util.Log;

import com.lzy.org.rxjava2.entity.Student;
import com.lzy.org.rxjava2.entity.Teacher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava2Test
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 *       https://github.com/linzhiyong
 *       https://www.jianshu.com/u/e664ba5d0800
 * @time 2018/7/16
 * @desc
 */
public class RxJava2Test {

    private static final String TAG = RxJava2Test.class.getName();

    public static void test() {
        Flowable_just1();
        Flowable_test1();
        Observable_test1();
        Observable_just1();
        Observable_fromArray1();
        Observable_fromCallable1();
        Observable_fromFuture1();
        Observable_fromIterable1();
        Observable_defer1();
        Observable_timer1();
        Observable_interval1();
        Observable_intervalRange1();
        Observable_range1();
        Observable_rangeLong1();
        Observable_map1();
        Ovservable_flatMap1();
        Ovservable_buffer1();
        Ovservable_groupBy1();
        Ovservable_scan1();
        Ovservable_window1();
        Ovservable_concat1();
        Ovservable_concatArray1();
        // ...
        Ovservable_reduce1();
        Ovservable_collect1();
        Ovservable_startWith1();
    }

    public static void Flowable_just1() {
        // support Java 8 lambdas (yet)
        Flowable.just("--1--Hello World!").subscribe(System.out::println);

        // 普通使用
        Flowable.just("--2--Hello World").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
    }

    public static void Flowable_test1() {
        Flowable<Integer> flowable = Flowable.range(0, 50)
                .map(v -> v * v)
//                .map(new Function<Integer, Integer>() {
//                    @Override
//                    public Integer apply(Integer integer) throws Exception {
//                        return integer * integer;
//                    }
//                })
                .filter(v -> v % 3 == 0);
//                .filter(new Predicate<Integer>() {
//                    @Override
//                    public boolean test(Integer o) throws Exception {
//                        return o % 3 == 0;
//                    }
//                });
        flowable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "--Flowable_test1--" + integer);
            }
        });
    }

    /**
     * 创建一个被观察者
     */
    public static void Observable_test1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.i(TAG, "--Observable_test1--2--" + "\n");
                emitter.onNext(1);
                Log.i(TAG, "--Observable_test1--2--" + "\n");
                emitter.onNext(2);
                Log.i(TAG, "--Observable_test1--3--" + "\n");
                emitter.onNext(3);
                emitter.onComplete();
                Log.i(TAG, "--Observable_test1--4--" + "\n" );
                emitter.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {

            private int i = 0;

            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "Observer accept" + integer + "\n" );
                if (integer == 3) {
                    disposable.dispose();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError : value : " + e.getMessage() + "\n" );
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete" + "\n" );
            }
        });
    }

    /**
     * 创建一个被观察者，并发送事件，发送的事件不可以超过10个
     */
    public static void Observable_just1() {
        Observable.just(1, 2, 3)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "--Observable_just1--start--");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i(TAG, "--Observable_just1--onNext--" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "--Observable_just1--onError--");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "--Observable_just1--onComplete--");
                    }
                });
    }

    /**
     * 这个方法和 just() 类似，fromArray可以传入一个数组
     */
    public static void Observable_fromArray1() {
        Integer[] array = {1, 2, 3, 4, 5, 6};
        Observable.fromArray(array)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "--Observable_fromArray1--start--");
                    }

                    @Override
                    public void onNext(Integer i) {
                        Log.i(TAG, "--Observable_fromArray1--onNext--" + i);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "--Observable_fromArray1--onError--");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "--Observable_fromArray1--onComplete--");
                    }
                });

    }

    /**
     * 这里的 Callable 是 java.util.concurrent 中的 Callable，Callable 和 Runnable 的用法基本一致，只是它会返回一个结果值，这个结果值就是发给观察者的
     */
    public static void Observable_fromCallable1() {
        Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "linzhiyong";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "--Observable_fromCallable1--receive message--" + s);
            }
        });
    }

    /**
     * 参数中的 Future 是 java.util.concurrent 中的 Future，Future 的作用是增加了 cancel() 等方法操作 Callable，
     * 它可以通过 get() 方法来获取 Callable 返回的值。
     */
    public static void Observable_fromFuture1() {
        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "create by linzhiyong";
            }
        });
        Observable.fromFuture(futureTask)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.i(TAG, "--Observable_fromFuture1--Disposable--");
                        futureTask.run();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "--Observable_fromFuture1--accept--" + s);
                        String ss = futureTask.get();
                    }
                });
    }

    /**
     * 直接发送一个 List 集合数据给观察者
     */
    public static void Observable_fromIterable1() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        Observable.fromIterable(list)
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        Log.i(TAG, "--Observable_fromFuture1--accept--" + integer);
//                    }
//                });
        .subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "--Observable_fromIterable1--onSubscribe--");
            }

            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "--Observable_fromIterable1--onNext--" + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "--Observable_fromIterable1--onError--");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "--Observable_fromIterable1--onComplete--");
            }
        });
    }

    /**
     * 直到被观察者被订阅后才会创建被观察者。
     * 因为 defer() 只有观察者订阅的时候才会创建新的被观察者，所以每订阅一次就会通知一次观察者
     */
    static String content = "nothing";
    public static void Observable_defer1() {
        Observable observable = Observable.defer(new Callable<ObservableSource<?>>() {
            @Override
            public ObservableSource<?> call() throws Exception {
                return Observable.just(content);
            }
        });

        Observer observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "--Observable_defer1--onSubscribe--");
            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "--Observable_defer1--onNext--" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "--Observable_defer1--onError--");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "--Observable_defer1--onComplete--");
            }
        };

        observable.subscribe(observer);

        content = "create by linzhiyong";
        observable.subscribe(observer);
    }

    /**
     * 当到指定时间后就会发送一个 0L 的值给观察者。
     */
    public static void Observable_timer1() {
        Observable.timer(2L, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "--Observable_timer1--onSubscribe--");
                    }

                    @Override
                    public void onNext(Long l) {
                        Log.i(TAG, "--Observable_timer1--onNext--" + l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "--Observable_timer1--onError--");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "--Observable_defer1--onComplete--");
                    }
                });

//        Observable.timer(2L, TimeUnit.SECONDS, new Scheduler() {
//            @Override
//            public Worker createWorker() {
//                return new Worker() {
//                    @Override
//                    public Disposable schedule(Runnable run, long delay, TimeUnit unit) {
//                        return null;
//                    }
//
//                    @Override
//                    public void dispose() {
//
//                    }
//
//                    @Override
//                    public boolean isDisposed() {
//                        return false;
//                    }
//                };
//            }
//        });
    }

    /**
     * 每隔一段时间就会发送一个事件，这个事件是从0开始，不断增1的数字。
     */
    public static void Observable_interval1() {
        Observable.interval(2L, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "--Observable_interval1--onSubscribe--");
                    }

                    @Override
                    public void onNext(Long l) {
                        Log.i(TAG, "--Observable_interval1--onNext--" + l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "--Observable_interval1--onError--");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "--Observable_interval1--onComplete--");
                    }
                });
    }

    /**
     * 可以指定发送事件的开始值和数量，其他与 interval() 的功能一样。
     */
    public static void Observable_intervalRange1() {
        Observable.intervalRange(2L, 10, 0, 2L, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "--Observable_intervalRange1--onSubscribe--");
                    }

                    @Override
                    public void onNext(Long l) {
                        Log.i(TAG, "--Observable_intervalRange1--onNext--" + l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "--Observable_intervalRange1--onError--");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "--Observable_intervalRange1--onComplete--");
                    }
                });
    }

    /**
     * 同时发送一定范围的事件序列。
     */
    public static void Observable_range1() {
        Observable.range(1, 5)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "--Observable_range1--onSubscribe--");
                    }

                    @Override
                    public void onNext(Integer l) {
                        Log.i(TAG, "--Observable_range1--onNext--" + l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "--Observable_range1--onError--");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "--Observable_range1--onComplete--");
                    }
                });

    }

    /**
     * 同时发送一定范围的事件序列，作用与 range() 一样，只是数据类型为 Long。
     */
    public static void Observable_rangeLong1() {
        Observable.rangeLong(1L, 100L)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "--Observable_rangeLong1--onSubscribe--");
                    }

                    @Override
                    public void onNext(Long l) {
                        Log.i(TAG, "--Observable_rangeLong1--onNext--" + l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "--Observable_rangeLong1--onError--");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "--Observable_rangeLong1--onComplete--");
                    }
                });
    }

    /**
     * 遍历被观察者发送的数据，可以进行数据操作、数据类型转换
     */
    public static void Observable_map1() {
        Integer[] array = {1, 2, 3};
        Observable.fromArray(array)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer * 2;
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "--Observable_rangeLong1--onSubscribe--");
                    }

                    @Override
                    public void onNext(Integer l) {
                        Log.i(TAG, "--Observable_rangeLong1--onNext--" + l);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "--Observable_rangeLong1--onError--");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "--Observable_rangeLong1--onComplete--");
                    }
                });
    }

    /**
     * flatMap() 其实与 map() 类似，但是 flatMap() 返回的是一个 Observerable
     */
    public static void Ovservable_flatMap1() {
        // 举例
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher("语文", "王老师"));
        teachers.add(new Teacher("数学", "李老师"));
        teachers.add(new Teacher("英语", "刘老师"));

        for (int i = 0; i < teachers.size(); i++) {
            teachers.get(i).getStudentList().add(new Student(16, teachers.get(i).getName() + "的学生" + 1));
            teachers.get(i).getStudentList().add(new Student(16 , teachers.get(i).getName() + "的学生" + 2));
            teachers.get(i).getStudentList().add(new Student(16 , teachers.get(i).getName() + "的学生" + 3));
        }

        // 普通map遍历
        Observable.fromIterable(teachers)
                .map(new Function<Teacher, List<Student>>() {
                    @Override
                    public List<Student> apply(Teacher teacher) throws Exception {
                        return teacher.getStudentList();
                    }
                })
                .subscribe(new Consumer<List<Student>>() {
                    @Override
                    public void accept(List<Student> students) throws Exception {
                        for (Student student : students) {
                            Log.i(TAG, "--Observable_flatMap--student.getName()--" + student.getName());
                        }
                    }
                });

        // flatMap遍历
        Observable.fromIterable(teachers)
                .flatMap(new Function<Teacher, ObservableSource<Student>>() {
                    @Override
                    public ObservableSource<Student> apply(Teacher teacher) throws Exception {
                        if (teacher.getName().contains("李")) {
                            return Observable.fromIterable(teacher.getStudentList()).delay(1, TimeUnit.SECONDS);
                        }
                        return Observable.fromIterable(teacher.getStudentList());
                    }
                })
                .subscribe(new Consumer<Student>() {
                    @Override
                    public void accept(Student student) throws Exception {
                        Log.i(TAG, "--Observable_flatMap--student.getName()--" + student.getName());
                    }
                });


        // concatMap遍历
        Observable.fromIterable(teachers)
                .concatMap(new Function<Teacher, ObservableSource<Student>>() {
                    @Override
                    public ObservableSource<Student> apply(Teacher teacher) throws Exception {
                        if (teacher.getName().contains("李")) {
                            return Observable.fromIterable(teacher.getStudentList()).delay(1, TimeUnit.SECONDS);
                        }
                        return Observable.fromIterable(teacher.getStudentList());
                    }
                })
                .subscribe(new Consumer<Student>() {
                    @Override
                    public void accept(Student student) throws Exception {
                        Log.i(TAG, "--Observable_concatMap--student.getName()--" + student.getName());
                    }
                });
    }

    /**
     * 从需要发送的事件当中获取一定数量的事件，并将这些事件放到缓冲区当中一并发出
     */
    public static void Ovservable_buffer1() {
        Observable.just(1, 2, 3, 4, 5)
                .buffer(2, 1)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Log.i(TAG, "--Ovservable_buffer1--数量--" + integers.size());
                    }
                });
    }

    /**
     * 将发送的数据进行分组，每个分组都会返回一个被观察者。
     */
    public static void Ovservable_groupBy1() {
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .groupBy(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer % 3;
                    }
                })
                .subscribe(new Observer<GroupedObservable<Integer, Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(GroupedObservable<Integer, Integer> observable) {
                        observable.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.i(TAG, "--Ovservable_groupBy1--key--" + observable.getKey() + ", value--" + integer);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 将数据以一定的逻辑聚合起来。
     */
    public static void Ovservable_scan1() {
        Observable.just(1, 2, 3, 4, 5)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        Log.i(TAG, "--Ovservable_scan1--integer--" + integer + "----integer2--" + integer2);
                        return integer * integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "--Ovservable_scan1--" + integer);
                    }
                });
    }

    /**
     * 发送指定数量的事件时，就将这些事件分为一组。window 中的 count 的参数就是代表指定的数量，
     * 例如将 count 指定为2，那么每发2个数据就会将这2个数据分成一组。
     */
    public static void Ovservable_window1() {
        Observable.just(1, 2, 3, 4, 5)
                .window(2)
                .subscribe(new Observer<Observable<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "--Ovservable_window1--onSubscribe--");
                    }

                    @Override
                    public void onNext(Observable<Integer> integerObservable) {
                        integerObservable.subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.i(TAG, "--Ovservable_window1--sub--onSubscribe--");
                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.i(TAG, "--Ovservable_window1--sub--onNext--" + integer);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                Log.i(TAG, "--Ovservable_window1--sub--onComplete--");
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "--Ovservable_window1--onComplete--");
                    }
                });
    }

    /**
     * 可以将多个观察者组合在一起，然后按照之前发送顺序发送事件。需要注意的是，concat() 最多只可以发送4个事件。
     */
    public static void Ovservable_concat1() {
        Observable.concat(
                Observable.just(1, 2),
                Observable.just(3, 4)
        ).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "--Ovservable_concat1--" + integer);
            }
        });
    }

    /**
     * 与 concat() 作用一样，不过 concatArray() 接收数组参数。
     */
    public static void Ovservable_concatArray1() {
        Observable.concatArray(
                Observable.just(1, 2),
                Observable.just(3, 4)
        ).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "--Ovservable_concatArray1--" + integer);
            }
        });
    }

    /**
     * 这个方法月 concat() 作用基本一样，知识 concat() 是串行发送事件，而 merge() 并行发送事件。
     */
    public static void Ovservable_merge1() {

    }

    /**
     * concatArrayDelayError() & mergeArrayDelayError()
     * 在 concatArray() 和 mergeArray() 两个方法当中，如果其中有一个被观察者发送了一个 Error 事件，那么就会停止发送事件，
     * 如果你想 onError() 事件延迟到所有被观察者都发送完事件后再执行的话，就可以使用  concatArrayDelayError() 和 mergeArrayDelayError()
     */
    public static void Ovservable_concatArrayDelayError1() {

    }

    /**
     * 会将多个被观察者合并，根据各个被观察者发送事件的顺序一个个结合起来，最终发送的事件数量会与源 Observable 中最少事件的数量一样。
     */
    public static void Ovservable_zip1() {

    }

    /**
     * combineLatest() & combineLatestDelayError()
     * combineLatest() 的作用与 zip() 类似，但是 combineLatest() 发送事件的序列是与发送的时间线有关的，
     * 当 combineLatest() 中所有的 Observable 都发送了事件，只要其中有一个 Observable 发送事件，这个事件就会和其他 Observable 最近发送的事件结合起来发送
     */
    public static void combineLatest1() {

    }

    /**
     * 与 scan() 操作符的作用也是将发送数据以一定逻辑聚合起来，
     * 这两个的区别在于 scan() 每处理一次数据就会将事件发送给观察者，而 reduce() 会将所有数据聚合在一起才会发送事件给观察者。
     */
    public static void Ovservable_reduce1() {
        Observable.just(1, 2, 5, 6, 10)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        Log.i(TAG, "--Ovservable_reduce1--integer--" + integer);
                        Log.i(TAG, "--Ovservable_reduce1--integer2--" + integer2);
                        return integer + integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "--Ovservable_reduce1--result--" + integer);
                    }
                });
    }

    /**
     * 将数据收集到数据结构当中。
     */
    public static void Ovservable_collect1() {
        Observable.just(1, 2, 3, 4)
                .collect(new Callable<ArrayList<Integer>>() {
                    @Override
                    public ArrayList<Integer> call() throws Exception {
                        Log.i(TAG, "--Ovservable_collect1-AA-Thread id--" + Thread.currentThread().getId());
                        return new ArrayList<>();
                    }
                }, new BiConsumer<ArrayList<Integer>, Integer>() {
                    @Override
                    public void accept(ArrayList<Integer> integers, Integer integer) throws Exception {
                        Log.i(TAG, "--Ovservable_collect1-BB-Thread id--" + Thread.currentThread().getId());
                        integers.add(integer);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.single())
                .subscribe(new Consumer<ArrayList<Integer>>() {
                    @Override
                    public void accept(ArrayList<Integer> integers) throws Exception {
                        Log.i(TAG, "--Ovservable_collect1-CC-Thread id--" + Thread.currentThread().getId());
                        Log.i(TAG, "--Ovservable_collect1--integers--" + integers);
                    }
                });
    }

    /**
     * startWith() & startWithArray()
     * 在发送事件之前追加事件，startWith() 追加一个事件，startWithArray() 可以追加多个事件。追加的事件会先发出。
     */
    public static void Ovservable_startWith1() {
        Observable.just(1, 2, 3)
                .startWithArray(6, 7, 8)
                .startWith(9)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "--Ovservable_startWith1--integer--" + integer);
                    }
                });
    }

    /**
     * 返回被观察者发送事件的数量。
     */
    public static void Ovservable_count1() {
        Observable.just(1, 2, 3)
                .count()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i(TAG, "--Ovservable_count1--integer--" + aLong);
                    }
                });
    }

    /**
     * 去重操作符，去掉重复的事件。
     */
    public static void Ovservable_distinct1() {
        Observable.just(1, 2, 3, 3, 2)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "--Ovservable_distinct1--integer--" + integer);
                    }
                });
    }

    /**
     * 延时发送事件。
     */
    public static void Ovservable_delay1() {
        Observable.just(1, 2, 3, 3, 2)
                .delay(1, TimeUnit.SECONDS)
                .distinctUntilChanged()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "--Ovservable_delay1--integer--" + integer);
                    }
                });
    }

    /**
     * 其他事件
     */
    public static void Ovservable_doOn_After_completeNext1() {
        Observable.just(1, 2, 3, 3, 2)
                .ofType(Integer.class)
                .skip(1)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "--Ovservable_doOn_After_completeNext1-doOnNext-integer--" + integer);
                    }
                })
                .doOnEach(new Consumer<Notification<Integer>>() {
                    @Override
                    public void accept(Notification<Integer> integerNotification) throws Exception {
                        Log.i(TAG, "--Ovservable_doOn_After_completeNext1-doOnEach-");
                    }
                })
                .doAfterNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "--Ovservable_doOn_After_completeNext1-doAfterNext-integer--" + integer);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "--Ovservable_doOn_After_completeNext1-doOnError--" + throwable.getMessage());
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i(TAG, "--Ovservable_doOn_After_completeNext1-doComplete--");
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i(TAG, "--Ovservable_doOn_After_completeNext1-doOnDispose--");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i(TAG, "--Ovservable_doOn_After_completeNext1-doOnTerminate--");
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i(TAG, "--Ovservable_doOn_After_completeNext1-doAfterTerminate--");
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i(TAG, "--Ovservable_doOn_After_completeNext1-doFinally--");
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "--Ovservable_doOn_After_completeNext1--integer--" + integer);
                    }
                });
    }

    public static void Observable_debounce1() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                Thread.sleep(500);
                emitter.onNext(2);
                Thread.sleep(1200);
                emitter.onNext(3);
            }
        })
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "--Observable_debounce1--integer--" + integer);
                    }
                });

    }

    /**
     * 取指定数量的事件
     */
    public static void Observable_take1() {
        Observable.just(1, 2, 3, 4)
                .take(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "--Observable_debounce1--integer--" + integer);
                    }
                });
    }

    /**
     * firstElement() && lastElement()
     */
    public static void Observable_firstElement1() {
        Observable.just(1, 2, 3, 4)
                .firstElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "--Observable_firstElement1--integer--" + integer);
                    }
                });
    }

    /**
     * elementAt() & elementAtOrError()
     */
    public static void Observable_elementAt1() {
        Observable.just(1, 2, 3, 4)
                .elementAt(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "--Observable_elementAt1--integer--" + integer);
                    }
                });
    }

}
