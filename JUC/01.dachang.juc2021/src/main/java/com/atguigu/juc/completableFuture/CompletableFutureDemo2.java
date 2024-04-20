package com.atguigu.juc.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CompletableFutureDemo2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 533;
        });
        // 注释掉暂停线程， get 还没有算完只能返回 complete 方法设置的 444 ；暂停 2 秒钟线程，异步线程能够计算完成返回 get
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 当调用 CompletableFuture.get() 被阻塞的时候 ,complete 方法就是结束阻塞并 get() 获取设置的 complete 里面的值 .
        System.out.println(completableFuture.getNow(999)+"\t"+completableFuture.complete(444) + " \t " + completableFuture.get());
    }
}
