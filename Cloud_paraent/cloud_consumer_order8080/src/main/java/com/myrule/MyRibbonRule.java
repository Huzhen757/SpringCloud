package com.myrule;

import com.netflix.loadbalancer.BestAvailableRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRibbonRule {

    @Bean
    public IRule myRandomRule(){
        return new RandomRule();
    }

//    @Bean
//    public IRule myBestAvailableRule(){
//        return new BestAvailableRule();
//    }

}
