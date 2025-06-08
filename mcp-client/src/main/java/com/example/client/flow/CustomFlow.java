package com.example.client.flow;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class CustomFlow implements Flow.Subscriber<Integer> {

    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(Integer item) {
        // 接受到一个数据, 处理
        System.out.println("接受到数据: " + item);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 处理完调用request再请求一个数据
        this.subscription.request(1);
        // 或者 已经达到了目标, 调用cancel告诉发布者不再接受数据了
        // this.subscription.cancel();
    }

    @Override
    public void onError(Throwable throwable) {
        // 出现了异常(例如处理数据的时候产生了异常)
        throwable.printStackTrace();
        // 我们可以告诉发布者, 后面不接受数据了
        this.subscription.cancel();
    }

    @Override
    public void onComplete() {
        // 全部数据处理完了(发布者关闭了)
        System.out.println("处理完了!");
    }
}
