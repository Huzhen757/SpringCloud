# SpringCloud
SpringCloud &amp; SpringCloud Alibaba学习
SpringCloud Hoxton.SR1版本 
Spring Cloud Alibaba 2.1.0.RELEASE

## 1. SpringCloud
详情见SpringCloud（1）
### 1.1. 服务注册发现
Eureka介绍以及使用
Zookeeper安装以及使用
Consul安装以及使用

### 1.2. 服务调用
负载均衡调用Ribbon
服务接口调用OpenFeign

### 1.3. 服务降级/熔断/限流
Hystrix介绍以及服务降级和服务熔断的使用

### 1.4. 服务网关
Gateway配置动态路由

### 1.5. 服务配置
SpringCloud Config介绍，与github的整合部署，Config动态刷新问题

### 1.6. 服务总线
SpringCloud Bus介绍，动态刷新（解决Config中的手动刷新问题），精确通知

### 1.7. 消息驱动
SpringCloud Stream介绍，常用注解和API，结合RabbitMQ，解决重复消费和消息持久化问题

### 1.8. 服务链路跟踪
Sleuth之zipkin安装，Sleuth链路监控的具体使用

## 2. Spring Cloud Alibaba 
详情见SpringCloud（2）

### 2.1. Nacos服务注册配置中心
**Nacos=Eureka（Zookeeper/consul）+ Config + Bus**
Nacos安装
Nacos实现服务注册
Nacos实现服务配置：基础配置，分类配置
Nacos集群和持久化配置（Linux）

### 2.2. Sentinel服务降级/熔断/限流
Sentinel基本介绍
Sentinel流控规则：阈值类型，流控模式，流控效果
Sentinel降级规则
Sentinel系统规则
SentinelResource注解的使用（类似于HystrixCommand注解）
Sentinel+Ribbon+Openfeign+fallback的联合配置以及使用

### 2.3.分布式事务
Seata的基本概念和安装
订单6001，库存6003，账户6002三个微服务，完成**下订单->减库存->扣余额->修改订单状态**的业务逻辑来说明分布式事务
存在的问题以及解决方法：引入**GlobalTransactional注解**

