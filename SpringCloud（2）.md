[toc]

# 1. SpringCloud Alibaba

**服务限流降级：**默认支持Servlet，Feign，Restemplate，Dubbo和RocketMQ限流降级功能的接入，可以在运行时通过控制台实时修改限流降级规则，还支持查看限流降级Metrics监控

**服务注册与发现：**适配Spring Cloud服务注册与发现标准，默认集成了Ribbon的支持

**分布式配置管理：**支持分布式系统中的外部化配置，配置更改时自动刷新

**消息驱动：**基于Spring Cloud Stream为微服务应用构建消息驱动能力

**阿里云对象存储：**阿里云提供的海量，安全，低成本，高可用的云存储服务

**分布式任务调度**



# 2. SpringCloud Alibaba Nacos服务注册和配置中心

Dynamic Naming and Configuration Service   ==**注册中心+配置中心**==

==Nacos = Eureka（Consul/Zookeeper）+ Config + Bus==

| 服务注册与发现框架 | CAP                 | 控制台管理 | 社区活跃度 |
| ------------------ | ------------------- | ---------- | ---------- |
| Eureka             | AP                  | 支持       | 低         |
| Zookeeper          | CP                  | 不支持     | 中         |
| Consul             | CP                  | 支持       | 高         |
| Nacos              | AP/CP（自适应调整） | 支持       | 高         |

## 1. 安装Nacos

+ 下载地址：https://github.com/alibaba/nacos/releases/tag/2.1.0 本次我下载的是**nacos-server-2.1.0.zip**
+ 解压之后打开bin目录的cmd，运行命令 `startup.cmd -m standalone`完成nacos的启动
+ 如果是旧版本的Nacos，使用命令  `startup.sh`完成启动
+ 打开http://localhost:8848/nacos，使用默认账户（账户名密码均为nacos）完成登录

![image-20220507155125864](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507155125864.png)



## 2. Nacos之服务注册中心

### a. Nacos之服务提供者注册

+ 新建工程cloudalibaba-provider-payment9001
+ POM

~~~xml
<dependencies>
        <!--引入SpringCloud Alibaba Nacos依赖-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>com.hz</groupId>
            <artifactId>cloud_api_commons</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    </dependencies>
~~~

+ yaml

~~~yaml
server:
  port: 9001

spring:
  application:
    name: nacos-provider
# Nacos的注册中心
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
# 暴露监控端点 用于更新配置刷新
management:
  endpoints:
    web:
      exposure:
        include: "*"
~~~

+ 主程序类

~~~java
@SpringBootApplication
@EnableDiscoveryClient
public class NacosProvider9001 {

    public static void main(String[] args) {
        SpringApplication.run(NacosProvider9001.class, args);
    }
}
~~~

+ Controller

+ ```java
  @RestController
  public class NacosPaymentController {
  
      @Value("${server.port}")
      private String serverPort;
  
      @GetMapping("/nacos/payment/{id}")
      public String getPayment(@PathVariable("id") Integer id){
          return "Nacos test enabled, serverPort: " + serverPort + "\t currently id: " + id;
      }
  }
  ```

+ 同理创建cloudalibaba-provider-payment9002的

### b. Nacos之服务消费者注册和负载

+ 新建cloudalibaba-consumer-order9090工程
+ POM
+ YAML

~~~yaml
server:
  port: 9090

spring:
  application:
    name: nacos-order-consumer
# Nacos的注册中心
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
# order将要去访问的微服务名
service-url:
  nacos-user-service: http://nacos-provider
~~~

+ 主程序类
+ controller

~~~java
@RestController
public class NacosOrderController {

    // public final static String URL = "";

    @Autowired
    private RestTemplate restTemplate;
    // 实现配置和代码的分离 直接读取配置文件去获取服务提供者的URL
    @Value(("${service-url.nacos-user-service}"))
    private String serverURL;

    @GetMapping("/consumer/nacos/payment/{id}")
    public String PaymentInfo(@PathVariable("id") Integer id){

        return restTemplate.getForObject(serverURL + "/nacos/payment/"+id, String.class);
    }
}
~~~

+ RestTemplate

~~~java
@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced // 开启负载均衡功能
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
~~~

+ 启动三个工程，查看Nacos服务注册中心

![image-20220507193118690](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507193118690.png)

**<span style='font-size:20px; color:blue'>为什么Nacos支持负载均衡？</span>**

**由于引入的nacos依赖内置了Netflix-Ribbon负载均衡**

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507162754140.png" alt="image-20220507162754140" style="zoom:150%;" />



## 3. Nacos与其他注册中心对比

**==Nacos支持AP和CP的切换==**

**C（一致性）是所有节点在同一时间看到的数据是一致的；A（可用性）是所有的请求都会收到响应**

![image-20220507171254190](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507171254190.png)

**<span style='font-size:20px; color:blue'>何时选择哪种模式？</span>**

+ **如果不需要存储服务级别的信息且服务实例是通过nacos-client注册，并能够保持心跳，可选择AP模式**。AP模式为了服务的可能性减弱了一致性，因此==AP模式下只支持注册临时实例==
+ **如果需要存储服务级别的信息或者存储配置信息，选择CP模式**。==CP模式下支持注册持久化实例==，注册实例之前必须先注册服务，如果服务不存在，返回错误



## 4. Naco之服务配置中心

###  a. Nacos-基础配置

Nacos与SpringCloud Config类似，在项目初始化，要保证先从配置中心进行配置拉取，拉取配置后，才能保证项目的启动

SpringBoot中配置文件的加载顺序：**bootstrap > application**

+ 新建工程
+ POM：在上面的基础上增加nacos-config的依赖

~~~xml
        <!--Nacos Config-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>
~~~

+ yaml：创建bootstrap和application两个yaml文件，bootstrap用于读取外部配置文件信息

~~~yaml
server:
  port: 3377

spring:
  application:
    name: nacos-config-center

  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 # 指定Nacos服务注册中心地址
      config:
        server-addr: localhost:8848 # 指定Nacos配置中心地址
        file-extension: yaml        # 指定yaml格式的配置(可读取后缀名为yaml格式的文件)

# nacos中的匹配规则
# ${prefix}-${spring.profile.active}.${file-extension}
# ${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}
# nacos-config-center-dev.yaml
~~~

~~~yaml
spring:
  profiles:
    active: dev # 指定当前开发环境
~~~

+ controller

~~~java
@RefreshScope
@RestController // 开启Nacos的动态刷新功能(广播通知 -> 配置自动更新)
public class ConfigCenterController {

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/nacos/config/info")
    public String getConfigInfo(){
        return configInfo;
    }
}
~~~

+ nacos中DataId的匹配规则，查看官方文档

![image-20220507174413883](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507174413883.png)

**设置DataId的规则为**

`${prefix}-${spring.profile.active}.${file-extension}`

+ prefix默认为`spring.application.name`的值，也可以通过配置`spring.cloud.nacos.config.prefix`进行配置
+ `spring.profile.active`为当前环境对应的profile，如果不配置该参数，则**dataId**的格式变为 `${prefix}.${file-extension}`
+ `${file-extension}`为配置内容的数据格式，通过配置项 `spring.cloud.nacos.config.file-extension`来配置，支持yaml和properties两种类型

----

测试：工程启动前**保证nacos客户端-配置管理-配置列表中有对应的yaml（或properties）配置文件**

![image-20220507175743721](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507175743721.png)

启动工程，调用接口 http://localhost:3377/nacos/config/info 查看配置中心的配置信息

如果启动失败：**重启Nacos；不要修改group名字，使用默认的DEFAULT_GROUP**

只需要更新Nacos配置管理中的DataId的文件内容，即可同步更新到项目中，不再需要手动发送POST请求去通知，非常方便

### b.Nacos-分类配置

**<span style='font-size:20px; color:blue'>问题引入</span>**

+ 实际开发中，通常一个系统会准备dev，test以及prod环境，**如何保证Nacos在指定环境启动时可以正确读取到Nacos上相应环境的配置文件**
+ 一个大型分布式微服务系统会有很多的微服务子项目，每个微服务又存在相应的dev，test以及prod生产环境，**如何对微服务配置进行管理**

Nacos中的命名空间由==**Namespace+Group+Data Id**==三部分组成

类似于java中的包名和类名，**最外层的namespace用于区分部署环境，group和dataId逻辑上区分两个目标对象**

默认情况：Namspace=public，Group=DEFAULT_GROUP，默认cluster=DEFAULT

+ Nacos默认的命名空间是public，namespace用来实现隔离，对于dev，test以及prod可以创建三个namespace，不同的namespace之间是隔离的
+ Group默认是DEFAULT_GROUP，group可以把不同的微服务划分到同一个分组里
+ Service就是微服务，一个Service可以包含多个cluster，cluster是对指定微服务的一个虚拟划分。Nacos默认Cluster是DEFAULT，为了解决容灾，可以将Service微服务部署在不同的地理位置，那么每一个位置的微服务起一个集群名称
+ Instance就是为微服务中的实例

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220508134803062.png" alt="image-20220508134803062" style="zoom:50%;" />

**<span style='font-size:20px; color:blue'>三种方案加载配置</span>**

+ ==DataId方法==：指定 `spring.profiles.active`和配置文件中的dataId来使不同环境下读取不同的配置，只需要切换 `spring.profiles.active`属性即可进行多环境下配置文件的读取

![image-20220508135930577](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220508135930577.png)

~~~yaml
# Nacos注册配置 spring.profiles.active指定哪个环境就加载哪个配置
spring:
  profiles:
   # active: dev # 指定当前开发环境为开发
    active: test # 指定当前开发环境为测试
~~~

+ ==group方法：==通过group实现环境区分，新建两个不同的group，但是DataId是一样的，同时改变bootstrap和application两个yaml文件即可

![image-20220508140502170](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220508140502170.png)

![image-20220508140731801](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220508140731801.png)

![image-20220508140802250](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220508140802250.png)

+ ==Namespace方法：==在nacos中的命名空间中新建dev和test两个命名空间，回到服务管理-服务列表进行查看，按照域名配置填写，修改YAML

![image-20220508141048956](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220508141048956.png)

![image-20220508141512655](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220508141512655.png)

![image-20220508142000992](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220508142000992.png)

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220508142033029.png" alt="image-20220508142033029" style="zoom:200%;" />



## 5. Nacos集群和持久化配置

官方文档：https://nacos.io/zh-cn/docs/cluster-mode-quick-start.html

Nacos默认使用**嵌入式数据库实现数据的存储**，如果启动多个默认配置下的nacos节点，数据存储是**存在一致性问题**的。为了解决，nacos采用==集中式存储的方式支持集群化部署，只支持MySQL的存储==

**<span style='font-size:20px; color:blue'>Nacos支持三种部署模式</span>**

+ 单机模式
+ 集群模式
+ 多集群模式

Nacos默认自带的是嵌入式数据库**derby**

**<span style='font-size:20px; color:blue'>derby到MySQL的切换配置</span>**

+ nacos/conf目录下执行nacos-mysql.sql脚本
+ 修改conf目录下的application.properties，增加支持mysql的数据源配置

~~~properties
spring.datasource.platform=mysql
db.num=1
db.url.0=jdbc:mysql:///cloud?serverTimezone=UTC&useSSL=false
db.user=root
db.password=123456
~~~

重新启动Nacos，登录nacos注册中心，之前的配置全部被清空

**<span style='font-size:20px; color:blue'>Nacos之Linux安装</span>**

+ https://github.com/alibaba/nacos/releases/tag/1.1.4官网下载nacos-server-1.1.4.tar.gz
+ `tar -zxvf nacos-server-1.1.4.tar.gz`
+ ==1个Nginx+3个Nacos注册中心+1个MySQL==

**安装Nginx**

+ 安装Nginx相关依赖： 

  `yum -y install gcc zlib zlib-devel pcre-devel openssl openssl-devel`

+ 安装pcre依赖：`tar -xvf pcre-8.40.tar.gz`

进入到安装目录中，执行 `./configure` 进行编译

执行 `make && make install`进行安装

使用 `pcre-config --version`查看版本号

+ Nginx安装

解压安装包 `tar -xvf nginx-1.20.2.tar.gz `

在解压的目录下执行`./configure`   

安装 `make && make install`.

打开/usr/local目录下，查看多了一个Nginx目录，并且该目录下存在一个sbin文件（启动脚本）

![image-20220508213655086](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220508213655086.png)

在sbin文件夹下，`./nginx`启动Nginx

![image-20220508213956709](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220508213956709.png)

打开Nginx安装目录下的nginx.conf文件

![image-20220508214312868](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220508214312868.png)

直接访问该地址即可打开Nginx的欢迎页

![image-20220508214341245](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220508214341245.png)

`./nginx -s stop` 关闭Nginx； `./nginx -s quit` 退出Nginx

+ Nginx集群搭建：==两台Nginx服务器+keepalived+虚拟ip==

1. 两个虚拟机上都安装并且启动nginx
2. 安装keepalived

`yum -y install keepalived` 

`rpm -q -a keepalived`

在/etc目录下生成keepalived文件夹，其中存在一个**keepalived.conf**，修改两个服务器上的配置

~~~conf
global_defs {	#全局定义
 notification_email {
 acassen@firewall.loc
 failover@firewall.loc
 sysadmin@firewall.loc
 }
 notification_email_from Alexandre.Cassen@firewall.loc
 smtp_server 192.168.45.100
 smtp_connect_timeout 30
 router_id LVS_DEVEL	#唯一不重复
}

###############################脚本配置
vrrp_script chk_http_port {
 script "/usr/local/nginx/nginx_check.sh"	#检测脚本的路径及名称
 interval 2 #（检测脚本执行的间隔，单位s）
 weight -20	#权重。设置当前服务器的权重，此处的配置说明：当前服务器如果宕机了，那么该服务器的权重降低20
}

#虚拟IP配置
vrrp_instance VI_1 {
 state BACKUP 	  #主服务器写MASTER、备份服务器写BACKUP
 interface ens33  #网卡
 virtual_router_id 51 # 主、备机的 virtual_router_id 必须相同
 priority 90 	      #主、备机取不同的优先级，主机值较大，备份机值较小
 advert_int 1	#时间间隔。每隔多少秒发送一次心跳检测服务器是否还活着，默认1秒发送一次心跳
 authentication {
 auth_type PASS
 auth_pass 1111
 }
 virtual_ipaddress {
 192.168.45.80     #  VRRP H 虚拟IP地址,网段要和linux的网段一致，可以绑定多个虚拟ip
 } }
~~~

3. 在/usr/local/nginx路径下创建nginx_check.sh（用于检测nginx是否宕机）

~~~txt
#!/bin/bash
A=`ps -C nginx –no-header |wc -l`
if [ $A -eq 0 ];then
 /usr/local/nginx/sbin/nginx
 sleep 2
 if [ `ps -C nginx --no-header |wc -l` -eq 0 ];then
 killall keepalived
 fi
fi
~~~

4. 启动nginx和keepalived

`systemctl start keepalived.service`

`ps -ef | grep keepalived`

5. 测试：使用虚拟ip地址直接访问，有欢迎页则配置成功；停止主服务器，再使用虚拟ip访问，也能进行访问（keepalived切换到备份服务器）

**<span style='font-size:20px; color:blue'>Nacos集群搭建</span>**

+ 切换默认数据库为MySQL（与windows下配置相同）
+ 集群配置cluster.conf：复制conf文件夹下的默认配置文件 `cp cluster.conf.example cluster.conf`，在 cluster.conf文件中配置nacos集群

![image-20220509142330697](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509142330697.png)



+ 编辑nacos/bin中的启动脚本startup.sh，保证Nacos可以接受不同的启动端口

集群启动，==传递不同的端口号启动不同的nacos实例==，如startup.sh -p 8848表示启动端口号为8848的nacos服务器实例

![image-20220509143408099](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509143408099.png)

![image-20220509143847933](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509143847933.png)

+ `./startup.sh -p 8848` 启动

![image-20220509145503487](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509145503487.png)

+ 配置Nginx监听Nacos集群：/usr/local/nginx/conf/nginx.conf

![image-20220510131546289](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220510131546289.png)

----

总结

+ Linux上MySQL的数据库配置
+ application.properties配置
+ nacos的集群配置cluster.conf
+ 编辑nacos的启动脚本startup.sh，使得nacos启动可以接受不同的端口号参数
+ Nginx的配置，由Nginx实现负载均衡器
+ 实现1个Nginx+3个Nacos+1个MySQL

# 3. SpringCloud Alibaba Sentinel服务熔断和限流

## 1. 简介

==分布式系统的流量防卫兵==：服务降级，服务熔断，服务限流，服务雪崩

Hystrix的**缺点**

+ 需要手工搭建监控平台Dashboard
+ 没有一套web界面可以进行更加细粒度化的配置服务熔断，服务降级，服务限流

Sentinel分为两部分

+ **核心库（java客户端）不依赖任何框架/库，能够运行所有java运行环境**，同时对Dubbo/Nacos/SpringCloud等框架很好的支持
+ **控制台（Dashboard）基于SpringBoot开发，打包后可直接运行**，不需要额外的Tomcat服务器（默认端口为8080）

Sentinel安装

+ 官网下载jar包sentinel-dashboard-1.7.2.jar
+ `java -jar sentinel-dashboard-1.7.2.jar`
+ localhost:8080直接访问控制台页面（默认账户名和密码均为sentinel）

![image-20220509164300130](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509164300130.png)

## 2. Sentinel初始化监控

## 3. 流控规则

### a. 基本介绍

![image-20220509170905522](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509170905522.png)

+ 资源名：唯一名称，默认请求路径
+ 针对来源：Sentinel可以针对调用者进行限流，填写微服务名，默认default（不区分来源）
+ 阈值类型/单级阈值：
  + QPS：每秒钟的请求数量，当调用该API的QPS达到阈值时，进行限流
  + 线程数：当调用该API的线程数达到阈值时，进行限流
+ 是否集群：勾选代表开启集群模式
+ 流控模式：
  + 直连：API达到限流条件时，直接限流
  + 关联：当关联的资源达到阈值时，限流自己
  + 链路：只记录指定链路上的流量（指定资源从吐口资源进来的流量，如果达到阈值则进行限流），属于API级别的限流
+ 流控效果：
  + 快速失败：直接失败，抛出异常
  + Warm up：根据codeFactor（冷加载因子，默认3），从阈值开始，经过预热时长，才达到设置的QPS阈值
  + 排队等待：匀速排队，让请求以匀速的速度通过，阈值类型必须设置为QPS，否则无效

### b. 流控模式

![image-20220509171856659](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509171856659.png)

+ 直连+快速失败（默认规则）：如果1s内查询超过2次则直接报错==Blocked by Sentinel (flow limiting)==

存在问题：仅仅是报默认错误，对纠错不友好，需要类似fallback的机制（服务降级）

![image-20220509185254927](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509185254927.png)

+ 关联：当关联资源/test2的QPS超过2时，限流自己/test3的Rest访问地址，需要设置好/test3所关联的资源名为/test2

### c. 流控效果

+ 快速失败（默认的流控处理）：对应的源码位置**com.alibaba.csp.sentinel.slots.block.flow.controller.DefaultController**
+ Warm up：**如果设置单机阈值为10，也就是QPS从10/3=3开始，经过指定的预热时长3s升高至QPS阈值**，对应的源码位置com.alibaba.csp.sentinel.slots.block.flow.controller.**WarmUpController**

![image-20220509185846732](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509185846732.png)

+ 排队等待：匀速排队，阈值类型必须是QPS，否则无效。让/test1每秒请求2次，超过的话排队等待，不会直接抛出异常，等待超时时间为20000ms。对应的源码为com.alibaba.csp.sentinel.slots.block.flow.controller.**RateLimiterController**

![image-20220509190528508](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509190528508.png)

## 4. 降级规则

![image-20220509191758396](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509191758396.png)

+ 资源名：唯一名称，默认请求路径
+ ==降级策略==：
  + RT：平均响应时间，秒级。==RT超出阈值并且在时间窗口内通过的请求超过阈值，触发降级==，RT最大设置为4900，窗口期过后关闭断路器
  + 异常比例：==QPS>=5并且异常比例（秒级别）超过阈值，触发降级==。时间窗口结束后关闭
  + 异常数：==当资源1分钟内的异常数超过阈值，触发降级==。此时时间窗口是分钟级别的，所以设置窗口大小需要>=60s
+ 时间窗口：该时间段内才会开启熔断（==熔断时间==）

Sentinel熔断降级在调用链路中某个资源出现不稳定状态时（调用超时或异常比例升高），**对这个资源的调用进行限制，让请求快速失败，避免影响到其他的资源而导致级联错误。**当资源被降级后，**在时间窗口内，对该资源的调用都自动熔断（默认抛出DegradeException）**。与Hystrix不同的是，Sentinel==不存在熔断器半开状态==

![image-20220509193716555](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509193716555.png)

如果一秒内进来10个线程（大于5个）调用test4方法，希望500ms内处理完本次任务。超过这个阈值，在未来的2s内，断路器打开，微服务不可用



## 5. 热点规则

何为热点？热点即经常访问的数据。很多时候我们希望统计某个热点数据中访问频次最高的 Top K 数据，并对其访问进行限制。比如：

- 商品 ID 为参数，统计一段时间内最常购买的商品 ID 并进行限制
- 用户 ID 为参数，针对一段时间内频繁访问的用户 ID 进行限制

热点参数限流会统计传入参数中的热点参数，并根据配置的限流阈值与模式，对包含热点参数的资源调用进行限流。热点参数限流可以看做是一种特殊的流量控制，仅对包含热点参数的资源调用生效。

之前的case中，服务降级出现问题后，都是使用Sentinel默认的提示**Blocked by Sentinel (flow limiting)**，能够自定义降级或熔断方法？类似于Hystrix中的==HystrixCommand注解==

使用**==@SentinelResource==**

~~~java
  @GetMapping("/sentinel/hotkey")
  @SentinelResource(value = "hotkey", blockHandler = "deal_testHotkey")
  public String testHotKey(@RequestParam(value = "username",required = false)String name,
                             @RequestParam(value = "age",required = false)String age){

        return "****Sentinel 热点规则";
    }

    public String deal_testHotkey(String p1, String p2, BlockException exception){

        return "****Sentinel my fallback method deal_testHotkey exec...";
    }
~~~

+ SentinelResource注解中的**value值**相当于是一个id，用于配置热点规则指定哪一个热点，只需要唯一即可

+ **==blockHandler==** 指定哪一个自定义的fallback方法

+ **配置/sentinel/hotkey的热点规则**

  资源名对应SentinelResource注解中的value值，参数索引为0对应检测的参数为username，如果1s内发送带有username的请求超过1个，则执行==blockHandler==指定的fallback方法**deal_testHotkey**

![image-20220509201132730](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509201132730.png)

![image-20220509201448212](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509201448212.png)

+ 如果期望username参数是**某个特殊值时，限流值和平时不一样**。比如当传递的username等于dasima，然而此时的阈值可以达到200，参数类型对应方法中的参数类型

![image-20220509210242009](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509210242009.png)

+ ==SentinelResource注解处理的是Sentinel控制台配置的违规情况，只负责配置出错，会走blockHandler方法==。**而对于程序的RuntimeException，即使没有违反Sentinel控制台配置的规则，也不会去执行blockHandler**

## 6. 系统规则

Sentinel 系统自适应限流从整体维度对应用入口流量进行控制，结合**应用的 Load、CPU 使用率、总体平均 RT、入口 QPS 和并发线程数**等几个维度的监控指标，通过自适应的流控策略，让系统的入口流量和系统的负载达到一个平衡，让系统尽可能跑在最大吞吐量的同时保证系统整体的稳定性。

![image-20220509211501516](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220509211501516.png)

## 7. SentinelResource配置

+ 按照资源名称限流，存在的问题：**一旦微服务关闭，那么刷新Sentinel控制台，限流规则直接消失（临时）**

+ 按照URL地址限流：通过访问的URL进行限流，违反了限流规则，执行Sentinel默认的限流规则

存在的问题（与**HystrixCommand**类似）

1. 系统默认的，没有体现出相应的业务要求
2. 自定义的处理方法（降级）和业务代码耦合度比较高，不直观
3. 每个业务方法都有一个对应的降级方法，代码比较冗余
4. 全局统一的处理方法没有体现

解决

1. 创建CustomerBlockHandler类用于自定义的限流处理逻辑
2. 自定义限流处理类
3. ==SentinelResource==注解中使用==blockHandlerClass==指定哪一个类为自定义处理逻辑类，==blockHandler==指定该类中的哪一个方法
4. 启动微服务调用测试，Sentinel控制台配置



----

**<span style='font-size:20px; color:blue'>SentinelResource注解的其他属性</span>**

+ blockHandler指定的方法不能是private
+ **value：**指定当前的资源名，唯一即可
+ **fallback：**==用于抛出异常时提供fallback的处理逻辑（**处理业务逻辑的运行时异常**）。==指定的fallback函数必须与原函数的返回值类型保持一致；函数的参数列表与原函数一致，或者可以额外增加一个Throwable类型的参数用于接受对应的异常；fallback函数与原函数必须在同一个类中，可以使用**fallbackClass**来指定对应的class对象，但是对应的fallback方法必须是static的，否则无法解析
+ **blockHandler/blockHandlerClass：**==用于处理BlockException的方法（**处理配置规则中出现的异常**）==，其他要求与fallback相同
+ **defaultFallback**
+ **exceptionsToIgnore：**可以指定某一个异常，当程序中发生该异常时不会调用fallback，也就不会发生服务降级 

## 8. Sentinel+Ribbon+Openfeign+fallback配置

### a. fallback & blockHandler

+ 新建cloudalibaba-sentinel-lb-payment9004，cloudalibaba-sentinel-lb-payment9003以及cloudalibaba-sentinel-lb-order8402三个工程

+ 两个支付服务的yaml配置

~~~yaml
server:
  port: 9003 # for cloudalibaba-sentinel-lb-payment9003; 9004 for cloudalibaba-sentinel-lb-payment9004

spring:
  application:
    name: cloudalibaba-sentinel-lb-provider

  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848

management:
  endpoints:
    web:
      exposure:
        inclued: "*"
~~~

+ 三个工程的POM一致

~~~xml
 <dependencies>
        <!--Nacos服务注册依赖-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <!--sentinel-datasource-nacos持久化-->
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
        </dependency>
        <!--Sentinel依赖-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>com.hz</groupId>
            <artifactId>cloud_api_commons</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
~~~

+ controller

~~~java
@RestController
@Slf4j
public class SentinelLBController9003 {

    @Value("${server.port}")
    public String serverPort;

    public static HashMap<Long, Payment> map = new HashMap<>();
	// 模拟数据库中的数据
    static{
        map.put(2001L, new Payment(78L, UUID.randomUUID().toString()));
        map.put(2002L, new Payment(79L, UUID.randomUUID().toString()));
        map.put(2003L, new Payment(80L, UUID.randomUUID().toString()));
        map.put(2004L, new Payment(81L, UUID.randomUUID().toString()));
    }

    @GetMapping(value = "/sentinel/payment/{id}")
    public CommonResult<Payment> getById(@PathVariable("id") Long id){
        log.info("this is Sentinel+LB+OpenFeign+fallback payment 9003");
        Payment payment = map.get(id);
        return new CommonResult<>(200, "sentinel+lb, select from mysql, serverPort: " + serverPort, payment);
    }
}
~~~

+ 订单服务的yaml中增加Sentinel的监控

~~~yaml
server:
  port: 8402

spring:
  application:
    name: cloudalibaba-sentinel-lb-order

  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
# 配置Sentinel Dashboard地址
    sentinel:
      transport:
        dashboard: localhost:8080 # 80端口监视8402
        port: 8719

# order8402将要访问的微服务名称(URL)
service-url:
  nacos-user-service: http://cloudalibaba-sentinel-lb-provider
~~~

+ controller

~~~java
@RestController
@Slf4j
public class CircleBreakController {

    public final String URL = "http://cloudalibaba-sentinel-lb-provider";
    @Value("${service-url.nacos-user-service}")
    private String SERVICE_URL;

    @Autowired
    private RestTemplate restTemplate;
    // ########### 测试fallback  -> 负责运行时异常的处理
    @GetMapping("/sentinel/consumer/payment/fallback/{id}")
   // @SentinelResource(value = "fallback") 未指定对应的fallback方法 运行时异常直接在前台输出
    @SentinelResource(value = "fallback", fallback = "handlerFallback")
    public CommonResult<Payment> fallback(@PathVariable("id") Long id){

        CommonResult res = restTemplate.getForObject(URL + "/sentinel/payment/" + id, CommonResult.class, id);
        if(res.getData() == null){
            throw new NullPointerException("输入id有误, 查询不到数据...");
        }else if(id < 2001 && id > 2004){
            throw new IllegalArgumentException("输入id非法, 非法输入参数异常");
        }
        return res;
    }
    // 自定义fallback方法: 返回值类型与原函数一致 参数列表一致(可以额外增加一个Throwable类型的参数 用来接受异常)
    public CommonResult handlerFallback(@PathVariable("id") Long id, Throwable e){
        Payment payment = new Payment(id, "no data");
        return new CommonResult(500, "fallback异常处理: " + e.getMessage(), payment);
    }

    // ########### 测试blockHandler -> 负责违规配置的处理
    @GetMapping("/sentinel/consumer/payment/blockhandler/{id}")
    @SentinelResource(value = "blockHandler", blockHandler = "myBlockHandler",
    exceptionsToIgnore = {IllegalArgumentException.class}) // 出现指定的异常直接忽略, 没有服务降级
    public CommonResult<Payment> testBlockHandler(@PathVariable("id") Long id){

        CommonResult res = restTemplate.getForObject(URL + "/sentinel/payment/" + id, CommonResult.class, id);
        if(res.getData() == null){
            throw new NullPointerException("输入id有误, 查询不到数据...");
        }else if(id < 2001 && id > 2004){
            throw new IllegalArgumentException("输入id非法, 非法输入参数异常");
        }
        return res;
    }

    public CommonResult myBlockHandler(@PathVariable("id") Long id, BlockException exception){
        Payment payment = new Payment(id, "no data");
        return new CommonResult(500, "block handler异常处理: "+exception.getMessage(), payment);
    }

    // ############ fallback和blockHandler都配置
    @GetMapping("/sentinel/consumer/payment/all/{id}")
    @SentinelResource(value = "fallbackAndBlockHandler", blockHandler = "myBlockHandler", fallback = "handlerFallback")
    public CommonResult<Payment> testBlockHandlerAndFallback(@PathVariable("id") Long id){

        CommonResult res = restTemplate.getForObject(URL + "/sentinel/payment/" + id, CommonResult.class, id);
        if(res.getData() == null){
            throw new NullPointerException("输入id有误, 查询不到数据...");
        }else if(id < 2001 && id > 2004){
            throw new IllegalArgumentException("输入id非法, 非法输入参数异常");
        }
        return res;
    }

}
~~~

+ 测试fallback只负责运行时异常，blockHandler只负责配置违规
  1. 只配置fallback
  2. 只配置blockHandler
  3. fallback和blockHandler都配置

==如果请求发生程序运行时异常，会去调用fallback处理逻辑；如果发生配置违规，会去调用blockHandler处理逻辑；如果两种情况同时发生，还是会调用blockHandler去处理==



### b. Sentinel服务熔断OpenFeign

+ 在cloudalibaba-sentinel-lb-order8402工程中增加OpenFeign的依赖
+ 定义Service层接口，==FeignClient注解==中value属性指明调用哪个微服务（与所调用的微服务中的controller方法保持一致），fallback属性指定该接口的实现类，用于对程序中发生错误进行服务熔断

~~~java
// 指明调用哪一个微服务
@FeignClient(value = "cloudalibaba-sentinel-lb-provider", fallback = SentinelPaymentServiceImpl.class)
public interface SentinelPaymentService {
    // 与对应的微服务controller方法保持一致 方法体去掉即可
    @GetMapping(value = "/sentinel/payment/{id}")
    public CommonResult<Payment> getById(@PathVariable("id") Long id);

}
~~~

+ service接口的实现类，order8402调用9003和9004, 如果9003宕机, 继续访问则会执行@FeignClient注解中fallback指明的类中的方法。而该类是OpenFeign接口的实现类, 所以不会在前台输出错误页面而是执行实现类中的fallback方法

~~~java
@Component
public class SentinelPaymentServiceImpl implements SentinelPaymentService {
    @Override
    public CommonResult<Payment> getById(Long id) {
        return new CommonResult<>(500, "服务降级, OpenFeign中的fallback方法", new Payment(id, "openFeign for fallback"));
    }
}
~~~

+ 配置RestTemplate

~~~java
@Configuration
public class ApplicationContextConfig {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
~~~

+ controller中定义方法进行测试

~~~java
    @Resource
    private SentinelPaymentService service;

    @GetMapping(value = "/openfeign/consumer/payment/{id}")
    public CommonResult<Payment> getById(@PathVariable("id") Long id){
        return service.getById(id);
    }
~~~

![image-20220512113834124](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220512113834124.png)

### c. 三种不同的服务降级/熔断框架比较

|                | Sentinel                     | Hystrix               | resilience4j     |
| -------------- | ---------------------------- | --------------------- | ---------------- |
| 隔离策略       | 信号量隔离（并发线程数限流） | 线程池隔离/信号量隔离 | 信号量隔离       |
| 熔断降级策略   | 基于RT，异常比例，异常数     | 基于异常比率          | 基于异常比率，RT |
| 实时统计实现   | 滑动窗口                     | 滑动窗口              | Ring Bit Buffer  |
| 动态规则配置   | 支持多种数据源               | 支持多种数据源        | 有限支持         |
| 扩展性         | 多个扩展点                   | 插件的形式            | 接口的形式       |
| 基于注解的支持 | SentinelResource             | HystrixCommand        | 支持             |
| 限流           | 基于QPS，基于调用关系的限流  | 有限的支持            | Rate Limiter     |

### d. Sentinel持久化规则

问题引入：==一旦重启应用，刷新Sentinel控制台，之前配置的限流规则直接消失（临时），生产环境需要将配置规则进行持久化==

解决：**将限流配置规则持计划到Nacos进行保存，只要刷新8402某个Rest地址，Sentinel控制台的流控规则就能看到，只要Nacos中的配置不删除，针对8402上的流控规则持续有效**

+ 在配置文件中增加Nacos的数据源配置

~~~yaml
spring:
  cloud:
    nacos:
     # 配置Nacos数据源
      datasource:
        ds1:
          nacos:
            server-addr: localhost:8848
            dataId: ${spring.application.name}
            groupId: DEFAULT_GROUP
            data-type: json
            rule-type: flow # 流控规则
~~~

+ Nacos注册中心增加配置，dataId（不带后缀名）和DefaultGroup对应yaml中配置的参数信息

~~~json
[
    {
        "resource": "/sentinel/byurl",
        "limitApp": "default",
        "grade": 1,
        "count": 1,
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    }
]
~~~

![image-20220512145053230](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220512145053230.png)

resource：资源名

limitApp：来源应用

grade：**阈值类型，0表示线程数，1表示QPS**

count：单机阈值

strategy：**流控模式，0表示直接，1表示关联，2表示链路**

controlBehavior：**流控效果，0表示快速失败，1表示WarmUp，2表示排队等待**

clusterMode：是否开启集群



# 4. 分布式事务

## 1. 问题由来

对于用户购买商品的业务逻辑，整个业务逻辑由3个微服务来提供支持：

+ 库存服务：对给定的商品扣除库存数量
+ 订单服务：根据采购需求创建订单
+ 账户服务：从用户账户中扣除金额

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220512150958018.png" alt="image-20220512150958018" style="zoom:80%;" />

单体应用被拆分成微服务应用。原来的三个模块被拆分成**三个独立性的应用，分别使用三个独立的数据源**，业务操作需要调用三个服务来完成。此时==每个服务内部的数据一致性由本地事务来保证，但是全局的数据一致性问题不能保证==

## 2. Seata

### a. 基本概念

分布式事务过程的ID：**Transaction ID XID，全局唯一的事务ID**

==Transaction Coordinator（TC）==：事务协调器，维护全局事务的运行状态，驱动全局事务提交或回滚

==Transaction Manager（TM）==：事务管理器，控制全局事务的边界，负责开始全局事务、提交或回滚全局事务

==Resource Manager（RM）==：资源管理器，控制分支事务，负责分支注册，报告分支事务的状态，并驱动分支事务提交或回滚

<img src="C:\Users\Huzhen\Pictures\Seata分布式事务.png" alt="Seata分布式事务" style="zoom: 50%;" />

**一个分布式事务的处理过程**

+ TM向TC申请开启一个全局事务，全局事务创建成功并生产一个全局的唯一XID
+ XID在微服务调用链路的上下文中传播
+ RM向TC注册分支事务，将其纳入XID对应全局事务的管理
+ TM向TC发起针对XID的全局提交或回滚协议
+ TC调度XID所管辖的全部分支事务完成提交或回滚请求

### b. 安装

+ 官方下载地址 http://seata.io/zh-cn/blog/download.html

+ 解压后，修改conf目录下的**file.conf**配置文件

先备份原始的file.conf配置文件，修改==**自定义事务组名称+事务日志存储模式为DB+数据库连接信息**==

service模块 (自定义事务组名)

store模块（修改mode为db，修改对应的数据库连接信息，MySQL8.0以后driverClassName=com.mysql.cj.jdbc.Driver）

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220512155618201.png" alt="image-20220512155618201" style="zoom:67%;" />

+ 建立数据库seata，在conf文件夹或者官网的README中找到sql语句

~~~sql
-- -------------------------------- The script used when storeMode is 'db' --------------------------------
-- the table to store GlobalSession data
CREATE TABLE IF NOT EXISTS `global_table`
(
    `xid`                       VARCHAR(128) NOT NULL,
    `transaction_id`            BIGINT,
    `status`                    TINYINT      NOT NULL,
    `application_id`            VARCHAR(32),
    `transaction_service_group` VARCHAR(32),
    `transaction_name`          VARCHAR(128),
    `timeout`                   INT,
    `begin_time`                BIGINT,
    `application_data`          VARCHAR(2000),
    `gmt_create`                DATETIME,
    `gmt_modified`              DATETIME,
    PRIMARY KEY (`xid`),
    KEY `idx_gmt_modified_status` (`gmt_modified`, `status`),
    KEY `idx_transaction_id` (`transaction_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
-- the table to store BranchSession data
CREATE TABLE IF NOT EXISTS `branch_table`
(
    `branch_id`         BIGINT       NOT NULL,
    `xid`               VARCHAR(128) NOT NULL,
    `transaction_id`    BIGINT,
    `resource_group_id` VARCHAR(32),
    `resource_id`       VARCHAR(256),
    `branch_type`       VARCHAR(8),
    `status`            TINYINT,
    `client_id`         VARCHAR(64),
    `application_data`  VARCHAR(2000),
    `gmt_create`        DATETIME,
    `gmt_modified`      DATETIME,
    PRIMARY KEY (`branch_id`),
    KEY `idx_xid` (`xid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
-- the table to store lock data
CREATE TABLE IF NOT EXISTS `lock_table`
(
    `row_key`        VARCHAR(128) NOT NULL,
    `xid`            VARCHAR(96),
    `transaction_id` BIGINT,
    `branch_id`      BIGINT       NOT NULL,
    `resource_id`    VARCHAR(256),
    `table_name`     VARCHAR(32),
    `pk`             VARCHAR(36),
    `gmt_create`     DATETIME,
    `gmt_modified`   DATETIME,
    PRIMARY KEY (`row_key`),
    KEY `idx_branch_id` (`branch_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
~~~

+ 修改registry.conf

![image-20220512160415615](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220512160415615.png)

+ 先启动Nacos，Sentinel，再启动seata（bin目录下的seata-server.bat），测试是否成功注册到Nacos中

![image-20220512161954975](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220512161954975.png)



## 3. Seata使用

### a. 准备工作

创建三个微服务：**订单，库存，账户**

当用户下单时，会在订单服务中创建一个订单，然后通过RPC调用库存服务来扣减下单商品的库存；再通过RPC调用账户来扣减账户里面的余额；最后在订单服务中修改订单状态为已完成

该操作**跨越三个数据库，存在两次远程调用**，显然有分布式问题

~~~sql
-- 创建订单，库存，账户三个数据库
CREATE DATABASE prod_order;
CREATE DATABASE prod_storage;
CREATE DATABASE account;

-- 订单表
USE prod_order;
CREATE TABLE t_order(
 id BIGINT(12) NOT NULL AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT(12) DEFAULT NULL COMMENT '用户id',
 product_id BIGINT(12) DEFAULT NULL COMMENT '商品id',
 COUNT INT(11) DEFAULT NULL COMMENT '数量',
 money DECIMAL(11,0) DEFAULT NULL COMMENT '金额',
 STATUS INT(1) DEFAULT NULL COMMENT '订单状态:0创建中,1已完成'
 )ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
 
SELECT * FROM t_order;
-- 回滚日志表（三个表各一份）
CREATE TABLE undo_log(
 id BIGINT(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
 branch_id BIGINT(20) NOT NULL,
 xid VARCHAR(100) NOT NULL,
 context VARCHAR(128) NOT NULL,
 rollback_info LONGBLOB NOT NULL,
 log_status INT(11) NOT NULL,
 log_created DATETIME NOT NULL,
 log_modified DATETIME NOT NULL,
 ext VARCHAR(100) DEFAULT NULL,
UNIQUE KEY ux_undo_log(xid, branch_id)
 )ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- storage库存表
USE prod_storage;
CREATE TABLE t_storage(
 id BIGINT(12) NOT NULL AUTO_INCREMENT PRIMARY KEY,
 product_id BIGINT(12) DEFAULT NULL COMMENT "商品id",
 total INT(11) DEFAULT NULL COMMENT '总库存',
 used INT(11) DEFAULT NULL COMMENT '已用库存',
 residue INT(11) DEFAULT NULL COMMENT '剩余库存')ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

SELECT * FROM t_storage;

-- 账户表
USE account;
CREATE TABLE t_account(
 id BIGINT(12) NOT NULL AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT(12) DEFAULT NULL COMMENT '用户id',
 total DECIMAL(20,0) DEFAULT NULL COMMENT '总额度',
 used DECIMAL(20,0) DEFAULT NULL COMMENT '已用额度',
 residue DECIMAL(20,0) DEFAULT NULL COMMENT '剩余额度'
)ENGINE=INNODB DEFAULT CHARSET=utf8 AUTO_INCREMENT=2;

SELECT * FROM t_account;

~~~

整体的表结构

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220512170726149.png" alt="image-20220512170726149" style="zoom: 67%;" />

### b. 业务逻辑（下订单->减库存->扣余额->修改订单状态）

+ 新建订单Order，Storage以及Account三个工程



![image-20220513154650958](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220513154650958.png)

![image-20220513154710584](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220513154710584.png)

### c. 问题

+ **Account工程中的AccountServiceImpl添加超时（线程sleep20s）**

再次发送下订单请求，数据库中的order表的status为0，storage表中的库存被减以及account表中的金额被减

==当库存和账户金额扣减后，订单状态并没有设置为1，而且由于openFeign的重试机制，账户余额有可能被多扣减==

+ 解决：使用**==GlobalTransactional注解==**

















































