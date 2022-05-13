package com.hz.loadBalancer;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// 模拟源码写轮询
@Component
public class MyLB implements MyLoadBalancer{

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public final int getAndIncrement(){
        int cur;
        int next;
        do{
            cur = this.atomicInteger.get();
            next = cur >= Integer.MAX_VALUE ? 0 : cur+1;

        }while (!this.atomicInteger.compareAndSet(cur, next));
        System.out.println("第：" + next + "次访问");
        return next;
    }

    @Override
    public ServiceInstance getInstance(List<ServiceInstance> serviceInstances) {
        int index = getAndIncrement() % serviceInstances.size();
        ServiceInstance instance = serviceInstances.get(index);
        if(instance == null){
            try {
                throw new Exception("get service failed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

}
