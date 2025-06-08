package com.example.client.flow;

import java.util.concurrent.SubmissionPublisher;

public class FlowDemo {

    public static void main(String[] args) {

        // 1. 定义发布者, 发布的数据类型是 Integer
        // 直接使用jdk自带的SubmissionPublisher, 它实现了 Publisher 接口
        try(SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>()){
            // 2. 定义订阅者
            CustomFlow subscriber = new CustomFlow();
            // 3. 发布者和订阅者 建立订阅关系
            publisher.subscribe(subscriber);
            // 4. 生产数据, 并发布
            // 这里忽略数据生产过程
            for (int i = 0; i < 10; i++) {
                System.out.println("生成数据:" + i);
                // submit是个block方法
                publisher.submit(i);
            }

            // 5. 结束后 关闭发布者
            // 正式环境 应该放 finally 或者使用 try-resouce 确保关闭
            publisher.close();

            // 主线程延迟停止, 否则数据没有消费就退出
            Thread.currentThread().join(1000);
            // debug的时候, 下面这行需要有断点
            // 否则主线程结束无法debug
            System.out.println();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
