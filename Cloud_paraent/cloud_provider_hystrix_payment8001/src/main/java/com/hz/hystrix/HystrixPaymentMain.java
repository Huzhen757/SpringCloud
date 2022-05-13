package com.hz.hystrix;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient    // 注册到Eureka Server
@EnableCircuitBreaker  // 开启Hystrix服务熔断的支持
public class HystrixPaymentMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(HystrixPaymentMain.class, args);

    }
// 在主启动类中指定监控路径的配置 仅仅是为了服务监控而配置 由于SpringCloud升级后的ServletRegistrationBean的默认路径不是
// "/hystrix.stream", 需要在所需要监控的服务中配置下面的Servlet
    @Bean
    public ServletRegistrationBean getServlet(){
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean<HystrixMetricsStreamServlet> registrationBean = new ServletRegistrationBean<>(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
}
