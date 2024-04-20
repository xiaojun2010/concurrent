package com.atguigu.juc.completableFuture;


import java.util.concurrent.*;

/**
 * @auther zzyy
 * @create 2021-03-02 11:56
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        testThenCombine();

    }

    static void testThenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"\t"+"--completableFuture1--come in ----");
            return 10;
        });
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"\t"+"--completableFuture2--come in ----");
            return 20;
        });
        CompletableFuture<Integer> completableFutureResult = completableFuture1.thenCombine(completableFuture2,(x,y)->{
            System.out.println(Thread.currentThread().getName()+"\t"+"--thenCombine--come in ----");
            return x+y;
        });
        System.out.println(completableFutureResult.get());
    }

    static void testApplyToEither()  {
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"\t"+"----come in ----");

            try {TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e) {e.printStackTrace();}
            return 10;
        });

        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+"\t"+"----come in ----");
            try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {e.printStackTrace();}
            return 20;
        });

        CompletableFuture<Integer> completableFutureResult = completableFuture1.applyToEither(completableFuture2,f->{
            System.out.println(Thread.currentThread().getName()+"\t"+"----come in ----");
            return f+1;
        });
        try {
            System.out.println(Thread.currentThread().getName()+"\t" + completableFutureResult.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    static void m7() {
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenRun(()->{}).join());

        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenAccept(resultA->{}).join());

        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenApply(resultA->resultA + " resultB").join());
    }
    static void m6() {

        CompletableFuture.supplyAsync(() -> {
            return 1;
        }).thenApply(f -> {
            return f + 2;

        }).thenApply(f -> {
            return f + 3;

        }).thenApply(f -> {
            return f + 4;

        }).thenAccept(f -> {
            System.out.println(f);

        });


}

    static void m5() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
                    System.out.println(Thread.currentThread().getName() + "---- return 1 -----");
                    return 1;
                }).handle((f, e) -> {
                    System.out.println(Thread.currentThread().getName() + "------ handle f + 2 -----");
                    int a = 10 / 0;
                    return f + 2;

                }).handle((f, e) -> {
                    System.out.println(Thread.currentThread().getName() + "------ handle f + 3 -----");
                    return f + 3;
                }).whenComplete((v, e) -> {
                    System.out.println(Thread.currentThread().getName() + "-----whenComplete:  v =" + v + " e= " + e);
                    if (e == null) {
                        System.out.println(Thread.currentThread().getName() + "-----whenComplete:   " + v);
                    }

                }).whenCompleteAsync((v, e) -> {
                    System.out.println(Thread.currentThread().getName() + "-----whenCompleteAsync:  v =" + v + " e= " + e);
                })

                .exceptionally(e -> {
                    System.out.println("------ exceptionally -----");
                    e.printStackTrace();
                    return null;
                });

//        try {
//            int res = future.get(500,TimeUnit.MILLISECONDS);
//            System.out.println("res = "+res);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }

        System.out.println("-------------main over");

//        //主线程不要立刻结束，否则 CompletableFuture 默认使用额线程池会离开关闭；暂停3秒钟线程
//        try {
//            TimeUnit.SECONDS.sleep(3);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        threadPoolExecutor.shutdown();

    }

    static void m4() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            System.out.println("---- return 1 -----");
            return 1;
        }).thenApply(f -> {

            System.out.println("------ f + 2 -----");
            //int a = 10/0;
            return f + 2;
        }).thenApply(f -> {
            System.out.println("------ f + 3 -----");
            return f + 3;
        }).whenComplete((v, e) -> {
            System.out.println("-----whenComplete:  v =" + v + " e= " + e);
            if (e == null) {
                System.out.println("-----whenComplete:   " + v);
            }

        }).exceptionally(e -> {
            System.out.println("------ exceptionally -----");
            e.printStackTrace();
            return null;
        });

//        try {
//            int res = future.get(500,TimeUnit.MILLISECONDS);
//            System.out.println("res = "+res);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }

        System.out.println("-------------main over");

//        //主线程不要立刻结束，否则 CompletableFuture 默认使用额线程池会离开关闭；暂停3秒钟线程
//        try {
//            TimeUnit.SECONDS.sleep(3);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        threadPoolExecutor.shutdown();

    }

    static void m3() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }).thenApply(f -> {
            return f + 2;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("-----result:  " + v);
            }

        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });

        System.out.println("-------------main over");

        //主线程不要立刻结束，否则 CompletableFuture 默认使用额线程池会离开关闭；暂停3秒钟线程
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPoolExecutor.shutdown();

    }

    static void m2() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture<Integer> future4 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---- come in ----");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 12;
        }, threadPoolExecutor);
        System.out.println(future4.get());

        System.out.println("------main over---");

        threadPoolExecutor.shutdown();

    }

    static void m1() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---- come in ----");
        });
        System.out.println(future1.get());

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t" + "---- come in  ----");
                },
                threadPoolExecutor
        );
        System.out.println(future2.get());


        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---- come in ----");
            return 11;
        });
        System.out.println(future3.get());


        CompletableFuture<Integer> future4 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---- come in ----");
            return 12;
        }, threadPoolExecutor);
        System.out.println(future4.get());


        threadPoolExecutor.shutdown();

    }
}
