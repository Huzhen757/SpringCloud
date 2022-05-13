[toc]



# 1. 微服务架构

微服务架构是一种结构模式，提倡将单一应用程序划分成一组小的服务，服务之间相互协调，互相配合，为用户提供最终价值。每个服务运行在独立的进程中，服务与服务之间采用轻量级的通信机制互相协作（**通常是基于HTTP协议的RestFul风格**）。每个服务都环绕着具体业务进行构建，并且能够独立的部署到生产环境，类生成环境等。另外，应该尽量避免统一的，集中式的服务管理机制，对具体的一个服务而言，应根据业务上下文，选择合适的语言，工具对其进行构建

==SpringCloud==分布式微服务架构的一站式解决方案，是多种微服务架构落地技术的集合体，称之为微服务全家桶

主流的技术栈：

+ 服务注册发现：Eureka
+ 服务负载与调用：RIbbon
+ 服务负载调用：Netflix
+ 服务熔断降级：Hystrix
+ 服务网关：Zuul
+ 服务分布式配置：Spring Cloud Config
+ 服务开发：Spring boot



# 2. Spring Cloud和boot版本选型

## 2.1. 版本选择

Spring Cloud F版本是基于Spring boot 2.0.x构建的，不再使用Spring boot 1.5.x

Spring Cloud D版本和E版本是基于Spring boot1.5.x的，不支持Spring boot 2.0.x

更详细的版本对应查看地址：https://start.spring.io/actuator/info

![image-20220415145358772](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220415145358772.png)

版本选择：

+ **Spring Cloud Hoxton.SR1**   官网https://cloud.spring.io/spring-cloud-static/Hoxton.SR1/reference/htmlsingle/
+ **Spring boot 2.2.2.RELEASE**
+ **Spring Cloud Alibaba 2.1.0.RELEASE**
+ java8
+ Maven 3.5及以上
+ MySQL 5.7及以上

## 2.2. Cloud组件停更

**被动修复Bugs，不再接受合并请求，不再发布新版本**

+ 服务注册发现：Eureka的替换有==Zookeeper； Consul；Nacos==
+ 服务调用：Ribbon的替换有==LoadBalancer==，Feign的替换为==OpenFeign==
+ 服务熔断和降级：Hystrix的替换为==resilience4j； Sentinel（Spring Cloud Alibaba）==
+ 服务网关：Zuul替换为==GateWay==
+ 服务配置：Config替换为==Nacos==
+ 服务总线：Bus替换为==Nacos==





# 3. 搭建父工程

新建maven工程，选择maven-archetype-site作为achetype，在父工程的pom文件中：

+ 声明统一管理的jar包版本
+ 使用==dependencyManagement==标签，子模块继承之后，可以锁定版本，并且子module不再需要在pom文件中添加groupId和Version
+ dependencyManagement元素标签提供一种管理依赖版本号的方式，**通常会在一个组织或者项目的最顶层的父Pom文件中看到该元素。**可以让所有子项目中引用一个依赖而不用显示的列出版本号，Maven会沿着父子层次向上寻找，直至找到一个拥有dependencyManagement元素的项目，然后就会使用这个dependencyManagement标签中指定的版本号，聚合统一管理非常方便。**dependencyManagement标签里只是申明依赖，并不实现引入，是因此子项目需要显式的声明需要使用的依赖**
+ Maven如何跳过单元测试：点击maven的==Skip Test Model==

~~~xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.hz</groupId>
  <artifactId>Cloud_paraent</artifactId>
  <version>1.0-SNAPSHOT</version>
  <!--父工程的pom-->
  <packaging>pom</packaging>

  <name>Maven</name>
  <!-- FIXME change it to the project's website -->
  <url>http://maven.apache.org/</url>
  <inceptionYear>2001</inceptionYear>

  <distributionManagement>
    <site>
      <id>website</id>
      <url>scp://webhost.company.com/www/website</url>
    </site>
  </distributionManagement>

  <properties>
      <!--统一管理jar包版本-->
      <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
      <maven.compiler.source>1.8</maven.compiler.source>
      <maven.compiler.target>1.8</maven.compiler.target>
      <junit.version>4.12</junit.version>
      <lombok.version>1.18.10</lombok.version>
      <log4j.version>1.2.17</log4j.version>
      <mysql.version>5.1.47</mysql.version>
      <druid.version>1.1.16</druid.version>
      <mybatis.spring.boot.version>2.1.1</mybatis.spring.boot.version>
  </properties>

  <!--子模块继承之后，提供作用：锁定版本+子module不用写groupId和version-->
  <!--spring boot 2.2.2-->
  <dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-dependencies</artifactId>
      <version>2.2.2.RELEASE</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
    <!--spring cloud Hoxton.SR1-->
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-dependencies</artifactId>
      <version>Hoxton.SR1</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>

    <!--spring cloud 阿里巴巴-->
    <dependency>
      <groupId>com.alibaba.cloud</groupId>
      <artifactId>spring-cloud-alibaba-dependencies</artifactId>
      <version>2.1.0.RELEASE</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
    <!--mysql-->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>${mysql.version}</version>
      <scope>runtime</scope>
    </dependency>
    <!-- druid-->
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>${druid.version}</version>
    </dependency>

    <!--mybatis-->
    <dependency>
      <groupId>org.mybatis.spring.boot</groupId>
      <artifactId>mybatis-spring-boot-starter</artifactId>
      <version>${mybatis.spring.boot.version}</version>
    </dependency>
    <!--junit-->
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>${junit.version}</version>
    </dependency>
    <!--log4j-->
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>${log4j.version}</version>
    </dependency>
  </dependencies>
  </dependencyManagement>
  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
          <fork>true</fork>
          <addResources>true</addResources>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
~~~



# 4. 支付模块搭建(微服务订单模块，微服务消费者订单模块)

## 4.1. IDEA新建Project工作空间

+ 建Module
+ 修改POM
+ 写YAML
+ 主启动
+ 实现业务类
+ 测试
  + 通过页面请求访问：http：//localhost:8001/payment/get/{id}
  + 使用Postman
  + 开启Run DashBoard -> 替换成Services（2020以后）



## 4.2. 开启热部署Devtools

+ 添加devtools jar包到子工程中的pom文件

~~~xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
~~~

+ 在父工程的pom文件中添加插件spring-boot-maven-plugin

~~~xml
  <build>
    <plugins>
      <plugin>
        <!--热部署-->
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
          <fork>true</fork>
          <addResources>true</addResources>
        </configuration>
      </plugin>
    </plugins>
  </build>
~~~

+ 开启自动编译选项

![image-20220417190629619](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220417190629619.png)

+ 更新：ctrl + alt + shift + /  -> 打开Registry -> 开启下面两个选项

![image-20220417190944166](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220417190944166.png)

+ 重启IDEA



## 4.3. RestTemplate

+ *是什么？*

RestTemplate提供了多种便捷访问远程HTTP服务的方法，是一种简单便捷的访问**RestFul服务模板类**，是Spring提供的用于访问Rest服务的==客户端模板工具集==，是一种接口调用方式的封装

+ *如何使用？*

使用RestTemplate访问Restful接口非常简单，其中的参数（==url，requestMap，ResponseBean.class==）这三个参数分别表示**REST请求地址，请求参数，HTTP响应被转换成的对象类型**



## 4.4. 工程重构

+ 每个Module中是否存在重复的部分（实体类，方法等）
+ 新建工程cloud-api-commons
+ 新建工程POM的文件中添加共有的第三方工具包：lombok，devtools，hutool
+ entities：将两个微服务订单中的实体类拷贝到相同包路径下的cloud-api-commons工程中
+ cloud-api-commons工程使用maven命令clean，install
+ 改造两个订单微服务：
  + 删除各自的原先存在的实体类文件夹
  + 两个微服务订单POM中分别粘贴cloud-api-commons工程：

~~~xml
        <dependency>
            <groupId>com.hz</groupId>
            <artifactId>cloud_api_commons</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
~~~



# 5. 服务注册发现Eureka

## 5.1. 什么是服务治理

SpringCloud封装了Netflix公司的Eureka模块**实现服务治理**

在传统的**RPC远程调用框架**中，管理每个服务与服务之间依赖关系比较复杂，所以需要使用服务治理，**管理服务与服务之间的依赖关系**，可以实现服务调用，负载均衡，容错等，实现服务发现与注册

## 5.2. 什么是服务注册与发现

![image-20220418151905900](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220418151905900.png)

Eureka采用了**CS**的设计架构，**Eureka Server作为服务注册功能的服务器，是服务注册中心**。而系统中的其他微服务，**使用Eureka的客户端连接到Eureka Server并维持心跳连接**。这样系统维护人员可以通过Eureka Server来监控系统中每个微服务是否正常运行

在服务注册与发现中，有一个注册中心。当服务器启动时，会把当前自己服务器的信息如服务地址通讯地址等注册到注册中心上，另一方（消费者或服务提供者），以别名的方式去注册中心上获取到实际的服务通讯地址，然后再实现**本地RPC调用RPC远程调用框架核心设计思想：注册中心**，因为使用注册中心管理每个微服务与每个微服务之间的一个依赖关系（服务治理）。在任何RPC远程框架中，都会有一个注册中心（存放服务地址相关信息即解接口地址）

Eureka包含两个组件：

<span style='font-size:20px; color:red'>Eureka Server 提供服务注册</span>

**每个微服务节点通过配置启动后，会在Eureka Server中进行注册**，其中的服务注册列表将会存储所有可用服务节点的信息，服务节点的信息可以在界面中直接看到

<span style='font-size:20px; color:red'>Eureka Client 通过注册中心访问</span>

是一个**java客户端，用于简化Eureka Server的交互**，客户端同时也是一个内置的，**使用轮询负载算法**的负载均衡器。在应用启动后，将会在Eureka Server发送心跳（默认周期30s）。如果Eureka Server在多个心跳周期内没有接收到某个节点的心跳，Eureka Server将会从服务注册表中把这个服务节点移除（默认90s）

## 5.3. 服务器端安装

新建maven工程，由于是服务器端只需要配置一个主程序类即可，并且在配置文件中指明端口号和Eureka的地址

导入Eureka的依赖：注意不需要指定版本号，由于在父工程中的spring-cloud中已经进行了依赖绑定

在主程序类上加上注解**@EnableEurekaServer**

~~~xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
~~~

~~~java
// 表示该端口7001就是服务注册中心
@SpringBootApplication
@EnableEurekaServer
public class EurekaMain7001 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaMain7001.class, args);
    }
}
~~~

~~~yaml
server:
  port: 7001

eureka:
  instance:
    hostname: localhost # Eureka服务器端的实例名
  client:
    register-with-eureka: false # 表示不向注册中心注册自己
    fetch-registry: false       # 本地端口就是服务注册中心，维护服务实例，不需要去检索服务
    service-url:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
      # 设置于Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址

  server:
    enable-self-preservation: false
~~~

## 5.4. 服务注册

将cloud-provider-payment8001注册到Eureka Server成为服务提供者provider

+ 在cloud-provider-payment8001工程中导入Eureka Client依赖

~~~xml
        <dependency>
            <!--Eureka Client-->
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
~~~

+ 在主程序类中加注解**@EnableEurekaClient**

将cloud-consumer-order80注册到Eureka Server成为服务消费者consumer

是否从EurekaServer抓取已有的注册信息，默认为true。单节点无所谓，集群必须设置为true，才能配合Ribbon使用负载均衡

+ 在微服务工程的配置文件中注册到Eureka Server

~~~yaml
server:
  port: 8001

eureka:
  client:
    register-with-eureka: true # 表示注册中心注册
    fetchRegistry: true       # 是否从EurekaServer抓取已有的注册信息默认为true 单节点无所谓 集群必须设置为true才能配合Ribbon使用负载均衡
    service-url:
      defaultZone: http://localhost:7001/eureka # Eureka Server
~~~

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220419140917682.png" alt="image-20220419140917682" style="zoom:150%;" />

注意两点：

+ 在配置文件中配置service-url时传递的是一个Map键值对，**defaultZone需要缩进，并且空格之后跟上Eureka Server的地址**
+ **开启Eureka Server之后，再开启添加到Server的两个client微服务**，才能显示出Application有两个微服务注册到Eureka Server 



## 5.5. Eureka集群

### a. 原理

<span style='font-size:20px; color:red'>服务注册</span>：将服务信息注册到注册中心

<span style='font-size:20px; color:red'>服务发现</span>：从注册中心上获取服务信息

实质上存取key为服务名，获取value值为调用地址

具体过程：

+ 启动Eureka注册中心
+ 启动服务器提供者payment支付服务
+ 支付服务启动后会自把自身信息（如服务地址以别名的方式注册到Eureka）
+ 消费者Order服务需要在调用接口时，使用服务别名去注册中心获取实际的RPC远程调用地址
+ 消费者获取调用地址后，底层实际是利用HttpClient技术实现远程调用
+ 消费者获取服务地址后会缓存在本地的JVM内存中，默认间隔30s更新一次服务调用地址

***微服务RPC远程服务调用的核心？***

高可用性，如果注册中心只有一个，一旦出故障或者宕机，会导致整个服务环境不可用。==搭建Eureka注册中心集群，实现负载均衡+故障容错==



### b. 搭建集群（Eureka Server）

+ 创建Eureka Server7002服务和Eureka Server7003服务
+ 修改映射配置

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220419143214957.png" alt="image-20220419143214957" style="zoom:150%;" />

![image-20220419143324896](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220419143324896.png)

在hosts文件中添加三个Eureka Server信息

+ 修改单机版的yaml文件：每个Server的hostname不能是localhost，为了区分需要指明当前是哪一个Server注册中心，对于eureka7001的Server需要注册7002和7003的信息，在defaultZone中使用逗号分隔。同理，eureka7002和eureka7003的也需要注册

~~~yaml
server:
  port: 7001

# 对于Eureka集群 互相注册 相互守望 每个Server都需要注册其他Server的信息
eureka:
  instance:
    hostname: eureka7001.com # Eureka服务器端的实例名
  client:
    register-with-eureka: false # 表示不向注册中心注册自己
    fetch-registry: false       # 本地端口就是服务注册中心，维护服务实例，不需要去检索服务
    service-url:
      defaultZone: http://eureka7002.com:7002/eureka, http://eureka7003.com:7003/eureka
      # 设置于Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址(单机版)
      # defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
~~~

同时开启三个Server注册中心：eureka7001.com:7001中绑定7002和7003的信息，其余两个同理

![image-20220419144817130](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220419144817130.png)

![image-20220419144827558](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220419144827558.png)

![image-20220419144840356](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220419144840356.png)

+ 将支付服务8001注册到Eureka集群中，只需要修改该工程的yaml文件中的service-url

~~~yaml
eureka:
  client:
    register-with-eureka: true # 表示注册中心注册
    fetchRegistry: true       # 是否从EurekaServer抓取已有的注册信息默认为true 单节点无所谓 集群必须设置为true才能配合Ribbon使用负载均衡
    service-url:              # 将8001微服务注册到Eureka集群中的每一个节点中
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka, http://eureka7003.com:7003/eureka
      # defaultZone: http://localhost:7001/eureka # Eureka Server(单机版的Client注册)
~~~

测试结果：在**http://eureka7001.com:7001**中可以看到payment-service已经注册成功，同理其他两个server也能注册成功

![image-20220419145725530](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220419145725530.png)

### c. 支付微服务的集群搭建（Service Provider）

+ 新建一个Module为cloud-provider-payment8002
+ 将8001服务工程中的**dao，service以及dao层的映射文件daomapper全部进行拷贝**
+ 在Controller中加上一个serverPort成员属性，并且在查询返回的结果CommonResult的msg中追加serverPort属性，表示当前服务来自哪一个

~~~java
    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/payment/get/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
        Payment res = service.getPaymentById(id);
        log.info("根据id查询结果：" + res);
        if(res != null){
            return new CommonResult(200, "查询成功,serverPort: " + serverPort, res);
        }else{
            return new CommonResult(555, "记录id不存在", null);
        }
    }
~~~

+ 修改配置文件

~~~yaml
server:
  port: 8002
eureka:
  client:
    register-with-eureka: true # 表示注册中心注册
    fetchRegistry: true       # 是否从EurekaServer抓取已有的注册信息默认为true 单节点无所谓 集群必须设置为true才能配合Ribbon使用负载均衡
    service-url:              # 将8001微服务注册到Eureka集群中的每一个节点中
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka, http://eureka7003.com:7003/eureka
      # defaultZone: http://localhost:7001/eureka # Eureka Server(单机版的Client注册)
~~~

+ 修改订单微服务的Controller中的url属性，URL为注册到server中的provider别名

~~~java
// 对于单机版的服务订单地址可以写死
// public static final String PAYMENT_URL = "http://localhost:8001";
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";
~~~

+ ==启动EurekaServer，7001和7002服务==
+ ==再启动Service Provider，8001和8002服务==
+ 此时如果直接使用order中的url是不能访问到数据的，由于只能找到是哪一组别名为CLOUD-PAYMENT-SERVICE来提供服务，但是具体是哪一台服务器却不能找到。因此需要在restTemplat配置类中使用==注解@LoadBalanced开启负载均衡功能==

~~~java
    @Bean()
    @LoadBalanced // 开启restTemplate的负载均衡功能(否则order服务不能访问Service Provider)
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
~~~

+ 测试结果：默认使用**轮询**来调用具体的服务器来提供服务

![image-20220419164529727](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220419164529727.png)

![image-20220419164543532](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220419164543532.png)

**Ribbon和Eureka整合后，order服务可以直接调用服务而不用再关心具体的地址和端口号**

---

+ **主机名：服务名修改**

在两个支付微服务的配置文件中增加，8002同理修改

~~~yaml
eureka:
  instance:
    instance-id: payment8001
~~~

修改之前的主机名和服务名

![image-20220419165449697](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220419165449697.png)

修改之后的主机名和服务名（**如果存在4个service服务，只需要将Eureka Server重新启动就好了**）

![image-20220419170459673](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220419170459673.png)

+ **访问信息有IP信息提示**

~~~yaml
  # 主机名服务名修改  访问路径可以显示IP地址(默认是主机名:服务名)
  instance:
    instance-id: payment8001
    prefer-ip-address: true
~~~



## 5.6. 服务发现Discovery

<span style='font-size:20px; color:orange'>对于注册进Eureka Server中的微服务，可以通过服务发现来获取该服务的信息</span>

在两个微服务的主程序类上添加主机**@@EnableDiscoveryClient**

在Controller中增加一个方法，日志打印出服务和服务实例

~~~java
     // 服务发现来获取注册到server的服务信息
    @Autowired
    private DiscoveryClient discoveryClient;

@GetMapping("/payment/discovery")
    public Object getDiscovery(){
        // 获取所有的微服务(payment,order,...)
        List<String> services = discoveryClient.getServices();
        for(String service : services){
            log.info("******service: " + service);
        }
        // 获取某一个微服务的所有实例(payment8001,payment8002,...)
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD_PAYMENT_SERVICE");
        for(ServiceInstance instance : instances){
            log.info("******instance:\t" + instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return this.discoveryClient.toString();
    }
~~~

测试服务发现信息

![image-20220422141535814](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220422141535814.png)



## 5.7. Eureka的自我保护

概念：保护模式主要用于一组客户端和Eureka Server之间存在网络分区场景下的保护，一旦进入保护模式，==Eureka Server将会尝试保护其服务注册表中的信息，不再删除服务注册表中的数据，也就是不会注销任何微服务==，也就是启动Eureka Server之后，在Eureka首页上看到的 一串红字

**某个时刻某一个微服务不可用了，Eureka不会立刻清理，依旧会对该微服务的信息进行保存**

属于CAP理论中的**AP分支**（可用性和分区容忍性）：**当发生网络分区时，最大努力保证可用性**

*<span style='font-size:20px; color:blue'>为什么会产生Eureka自我保护机制？</span>*

为了防止EurekaClient可以正常运行，但是与EurekaServer网络不通情况下，Eureka Server==不会立刻==将EurekaClient服务删除

*<span style='font-size:20px; color:blue'>什么是自我保护模式？</span>*

默认情况下，如果EurekaServer在一定时间内没有接受到某个微服务实例的心跳，EurekaServer将会立刻注销该实例（默认90s）。但是**当网络分区故障发生（延时，卡顿，拥挤）时，微服务与EurekaServer之前无法正常通信**，以上行为可能变得非常危险。**由于微服务本身是健康的，此时不应该注销掉这个微服务，Eureka通过‘自我保护机制’来解决这个问题，当EurekaServer节点在短时间内丢失过多客户端时（可能发生网络分区故障），那么这个节点就会进入自我保护模式**

默认情况下，Eureka Client会定时向Eureka Server端发送心跳包，如果Eureka在Server端一定时间内（90s）没有收到Client端的心跳包，EurekaServer将会立刻注销该实例。当EurekaServer节点在短时间内丢失过多客户端时（可能发生网络分区故障），那么这个节点就会进入自我保护模式，不会删除该服务

---

*<span style='font-size:20px; color:blue'>怎么禁止自我保护机制？</span>*

+ 设置Server端口的配置文件

~~~yaml
eureka:
  server:
    enable-self-preservation: false      # 关闭自我保护机制
    eviction-interval-timer-in-ms: 20000 # 20s没有发送心跳包，直接注销微服务
~~~

+ 设置Client端的配置文件

~~~yaml
eureka:
  instance:
   # 设置client发送心跳包的时间间隔(默认30s)
    lease-renewal-interval-in-seconds: 15
    # EurekaServer收到最后一次心跳包之后的等待时间，默认90s
    lease-expiration-duration-in-seconds: 60
~~~





# 6. 服务注册发现Zookeeper

Zookeeper是一个分布式协调工具，可以实现注册中心功能

+ 关闭Linux防火墙 `systemctl stop firewalld`以启动Zookeeper服务器

+ 启动Zookeeper 服务器   `./zkServer.sh start` 

+ 启动Zookeeper客户端    `./zkCli.sh`

----

创建一个Zookeeper客户端微服务的Module

+ 新建cloud-provider-payment8004
+ ==修改其中的POM文件==，之前是使用Eureka作为客户端来注册发现
+ ==修改yaml==
+ 主程序类
+ Controller
+ 启动主程序类并且注册到Zookeeper

**注意：**

1. 由于spring-cloud-starter-zookeeper-discovery自带一个Zookeeper3.5.3-beta版本，需要排除掉这个版本，并且添加一个与Linux环境上所安装的Zookeeper版本一致的依赖，这里安装的是Zookeeper3.5.7
2. 由于在pom文件中导入了lombok依赖，而Zookeeper中自带一个slf4j-log4j12的依赖，需要将这个依赖排除掉，否则一启动就会报错

==SLF4J: Class path contains multiple SLF4J bindings.==

~~~xml
 <dependencies>
        <dependency>
            <groupId>com.hz</groupId>
            <artifactId>cloud_api_commons</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <!--SpringBoot整合Zookeeper客户端-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
            <!--排除自带的zookeeper3.5.3-beta版本-->
            <exclusions>
                <exclusion>
                    <groupId>org.apache.zookeeper</groupId>
                    <artifactId>zookeeper</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!--添加Zookeeper3.5.7版本，保证和Linux上安装的版本一致 排除掉其中自带的Slf4j依赖-->
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.5.7</version>
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-log4j12</artifactId>
                </exclusion>
            </exclusions>
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
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    </dependencies>
~~~

~~~yaml
server:
  port: 8004
# 注册到Zookeeper服务器的支付微服务端口号

spring:
  application:
    name: cloud-provider-payment

  datasource:
    type: com.alibaba.druid.pool.DruidDataSource                # 当前数据源操作类型
    driver-class-name: org.gjt.mm.mysql.Driver                  # mysql驱动包
    url: jdbc:mysql:///cloud?serverTimezone=UTC&useSSL=false
    username: root
    password: 123456

  cloud:
    zookeeper:
      connect-string: 192.168.45.128:2181                      # Zookeeper的IP:端口号
~~~

~~~java
@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String servePort;

    @GetMapping("/payment/zk")
    public String paymentZk(){
        return "Spring cloud with zookeeper: " + servePort + "\t" + UUID.randomUUID().toString();

    }
}
~~~

启动主程序类，没有报错，查看在Zookeeper注册中心是否注册成功该服务cloud-provider-payment，注册成功之后会自动生成一个service流水号

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220423213926926.png" alt="image-20220423213926926" style="zoom:150%;" />

该微服务即为一个**znode节点**存入到Zookeeper，保存的是json字符串

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220423221625360.png" alt="image-20220423221625360" style="zoom:200%;" />

*<span style='font-size:20px; color:blue'>服务节点是临时节点还是持久节点？</span>* 存储的设计临时节点，==实现数据的一致性和分区容错性CP==，但是高可用性降低



# 7. 服务注册发现Consul

## 1. 简介

Consul是一套开源的分布式服务发现和配置管理系统，基于Go实现的

提供了微服务系统的服务治理，配置中心，控制总线等功能。基于raft协议，支持健康检查，同时支持HTTP和DNS协议支持跨数据中心的广域网集群，提供图形界面跨平台，支持Linux，Mac，以及Windows

+ Service Discovery服务发现：提供HTTP和DNS两种发现方式
+ Health Checking
+ KV Store
+ Secure Service Communication
+ Multi Datacenter
+ 可视化界面：下载地址为https://www.consul.io/downloads

## 2. 安装

==consul --version== 查看版本

==consul agent -dev== 启动consul

输入地址：==localhost:8500==查看是否启动成功

## 3. 创建Consul支付服务8006

+ 新建Module支付服务provider8006
+ POM：与Zookeeper支付微服务类似，只需要删除掉Zookeeper相关的依赖，并且导入consul-discovery的依赖即可

~~~xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        </dependency>
~~~

+ YAML

~~~yaml
server:
  port: 8006
# 注册到Consul服务器的支付微服务端口号

spring:
  application:
    name: cloud-provider-payment-consul8006

# Consul server注册地址
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: ${spring.application.name}
~~~

+ 主启动类：加上EnableDiscoveryClient注解
+ 业务类Controller

~~~java
@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/payment/consul")
    public String paymentConsul(){
        return "spring cloud consul: " + serverPort + "\t" + UUID.randomUUID().toString();
   }
}
~~~

+ 验证测试

localhost:8500查看是否已经注册成功，http://8006/payment/consul查看controller能否正常执行

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220427223325399.png" alt="image-20220427223325399" style="zoom:150%;" />

## 4. 创建Consul订单服务80

## 5. 三种注册中心的区别

| 组件名    | 语言 | CAP                | 服务健康检查 | 对外暴露接口          | Spring Cloud集成 |
| --------- | ---- | ------------------ | ------------ | --------------------- | ---------------- |
| Eureka    | Java | AP（自我保护机制） | 可配支持     | HTTP                  | 已集成           |
| Zookeeper | Java | CP                 | 支持         | 客户端（没有web界面） | 已集成           |
| Consul    | Go   | CP                 | 支持         | HTTP/DNS              | 已集成           |

最多只能满足CAP其中两个，CAP核心思想：**关注粒度是数据而不是整体系统设置，一个分布式系统不可能同时很好的满足一致性，可用性和分区容错性三个需求**

因此，根据CAP理论将NoSQL数据库分成了满足**AP，CP，以及AC**三大类：

==AC==----单点集群，满足一致性，可用性的系统，通常在扩展性上较为欠缺

==CP==----满足一致性和分区容忍性的系统，性能不是很高

==AP==----满足可用性和分区容忍性的系统，对一致性要求较低



# 8. 负载均衡服务调用Ribbon

## 1. 简介

Ribbon是基于Netfix Ribbon实现的一套==**客户端负载均衡工具**==

主要功能是：提供==**客户端的软件负载均衡算法和服务调用**==，Ribbon客户端组件提供一系列配置项如连接超时，重试等。也就是在配置文件中列出**Load Balance**后面的所有机器，Ribbon会自动基于某种规则（如**简单轮询，随机连接**等）去连接机器，**可以使用Ribbon实现自定义的负载均衡算法**

Ribbon目前进入维护模式

## 2. Load Balance是什么

负载均衡：**将用户的请求平摊到多个服务器上，从而达到系统的HA（高可用）**

常见的负载均衡软件有**Nginx**，LVS，F5等

**Ribbon本地负载均衡客户端与Nginx服务端负载均衡的区别**

1. Nginx是服务器负载均衡（==集中式的LB==），客户端所有的请求都会交给Nginx，然后由Nginx实现转发请求，负载均衡由服务器端实现
2. Ribbon是客户端负载均衡（==进程内的LB==），在调用微服务接口时，会在注册中心上获取注册信息服务列表之后缓存到JVM本地，从而在本地实现RPC远程服务调用技术

**进程内的LB**：将LB逻辑集成到消费方，消费方从服务注册中心获取有哪些地址可用，然后自己再从这些地址中选择出一个合适的服务器。Ribbon属于进程内LB，只是一个类库，集成于消费方进程，消费方通过它来获取到服务提供方的地址

**集中式的LB**：在服务的消费方和提供方之间使用独立的LB设施，如Nginx，由该设施负责将访问请求通过某种策略发送至服务的提供方

## 3. 使用

Ribbon其实就是一个软负载均衡的客户端组件，可以和其他所需请求的客户端结合使用，和Eureka结合仅仅是其中的一个实例

工作流程：

1. 选择EurekaServer，优先选择在同一个区域内负载较少的Server
2. 根据用户指定的策略，从Server获取到的服务注册列表中选择一个地址

----

***<span style='font-size:25px; color:blue'>RestTemplate的使用</span>***

+ getForObject/getForEntitiy

getForObject：**返回对象为响应体中的数据转换为JSON格式的对象**

getForEntitiy：**返回对象为ResponseEntity对象**，包含了响应中的一些重要信息，如响应头，响应状态码，响应体等

+ postForObject/postForEntity
+ GET
+ POST

## 4. Ribbon的负载规则

查看Ribbon下的IRule接口以及实现类，主要的负载规则有

![image-20220428154301245](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220428154301245.png)

1. RoundRobinRule：轮询
2. RandomRule：随机
3. RetryRule：先按照轮询策略获取服务，如果获取失败则在指定时间内进行重试，在获取可用的服务
4. WeightedResponseTimeRule：轮询的扩展，响应速度越快的实例选择权重越大，越容易被选择
5. ==BestAvailableRule==：先过滤掉由于多次访问故障而处于断路状态的服务，然后选择并发量最小的服务
6. AvailabilityFilteringRule：过滤掉故障实例，再选择并发量最小的实例
7. ZoneAvoidanceRule：默认规则，复合判断server所在的区域性能和server的可用性去选择服务器

----

**Ribbon负载规则的替换**

+ 在订单微服务（客户端）自定义类MyRibbonRule，并且不能在SpringBootApplication注解中的ComponentScan所扫描的范围内
+ 在主程序类上增加注解==@RibbonClient(name = "CLOUD-PAYMENT-SERVICE", configuration = MyRibbonRule.class)==，表示从CLOUD-PAYMENT-SERVICE中获取服务，使用自定义的规则类，不再使用默认规则

~~~java

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
~~~

## 5. Ribbon默认负载轮询算法

**<span style='font-size:20px; color:blue'>rest接口第几次请求参数 % 服务器集群总数量（提供某个服务的实例数） = 实际调用服务器位置下标</span>**

每次服务重启后rest接口会从1开始计数，从1开始递增

获取该服务的所有实例数：`List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");`

instances[0] = 127.0.0.1:8001，instances[1] = 127.0.0.1:8002

8001+8002组合成集群，集群总数为2，按照轮询算法：

当请求次数为1，1%2=1，对应下标位置为1，获取服务器地址为127.0.0.1:8002

当请求次数为2，2%2=0，对应下标位置为0，获取服务器地址为127.0.0.1:8001  以此类推......

~~~java
 public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            log.warn("no load balancer");
            return null;
        } else {
            Server server = null;
            int count = 0;

            while(true) {
                if (server == null && count++ < 10) {
                    List<Server> reachableServers = lb.getReachableServers();
                    List<Server> allServers = lb.getAllServers();
                    int upCount = reachableServers.size();
                    int serverCount = allServers.size();
                    if (upCount != 0 && serverCount != 0) {
                        // 通过自旋得到当前需要哪个微服务的位置下标
                        int nextServerIndex = this.incrementAndGetModulo(serverCount);
                        server = (Server)allServers.get(nextServerIndex);
                        if (server == null) {
                            Thread.yield();
                        } else {
                            if (server.isAlive() && server.isReadyToServe()) {
                                return server;
                            }

                            server = null;
                        }
                        continue;
                    }

                    log.warn("No up servers available from load balancer: " + lb);
                    return null;
                }

                if (count >= 10) {
                    log.warn("No available alive servers after 10 tries from load balancer: " + lb);
                }

                return server;
            }
        }
    }
~~~

----

***<span style='font-size:20px; color:blue'>自定义轮询算法并使用该规则</span>***

1. 在订单模块中自定义MyLoadBalancer接口

~~~java
import org.springframework.cloud.client.ServiceInstance;
import java.util.List;

public interface MyLoadBalancer {

    ServiceInstance getInstance(List<ServiceInstance> serviceInstances);

}
~~~

2. 自定义该接口的实现类

~~~java
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
~~~

3. 在CLOUD-PAYMENT-SERVICE微服务的两个实例中的controller中增加一个方法，用于返回当前使用哪一个实例（端口号）

~~~java
    @GetMapping("/payment/lb")
    public String getPaymentLB(){
        return "当前提供的微服务实例端口：" + serverPort;
    }
~~~

4. 在订单模块中的controller方法中增加方法，使用自定义的轮询获取当前所提供的服务实例是哪一个

~~~java
    public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";
    @Resource
    private RestTemplate restTemplate;

    @Autowired
    private MyLoadBalancer loaderHandler;

    @Autowired
    private DiscoveryClient discoveryClient;

@GetMapping("/consumer/payment/lb")
    public String getPaymentMyLB(){
        // 获取该微服务下的所有实例
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if(instances == null || instances.size() <= 0){
            return null;
        }
        // 根据微服务实例列表通过自定义的轮询得到当前需要访问的实例是哪一个
        ServiceInstance service = loaderHandler.getInstance(instances);
        URI uri = service.getUri();
        return restTemplate.getForObject(uri + "/payment/lb", String.class);
			
    }
~~~

5. 测试，根据http://localhost/consumer/payment/lb去访问当前提供的服务实例是哪一个，并且是否是否符合轮询的要求，8001和8002每次切换



# 9. 服务接口调用OpenFeign

## 1. 简介

Fegin是一个==声明式WebService客户端==，使用方法是==定义一个接口然后再此基础上添加注解==。Spring CLoud对其进行了封装，使其支持Spring MVC标准注解和HttpMessageConverters。另外，Fegin也可以和Eureka，Ribbon组合使用达到负载均衡

***<span style='font-size:20px; color:blue'>Fegin的作用</span>***

前面使用**Ribbon+restTemplate**结合时，利用**RestTemplate对Http请求的封装，形成了一套模板化的调用方式**，但是在实际开中，由于对服务依赖的调用可能不止一处，==往往一个接口会被多处调用，所以通常都会针对个每个微服务自行封装一些客户端类来包装这些依赖服务的调用==。所以，Fegin在此基础上作进一步封装，由他来帮助我们实现定义和实现依赖服务接口的定义。在Fegin的实现下，==只需要创建一个接口并使用注解的方式来配置他（以前是在Dao接口上使用**Mapper**注解，现在是一个微服务接口上面标注一个**Fegin**注解即可）==，即可完成对服务提供方的接口绑定，简化了使用Spring Cloud Ribbon时，自动封装服务调用客户端的开发量。

创建一个微服务接口，并且添加与OpenFeign相关的注解，即可实现接口之间的调用

***<span style='font-size:20px; color:blue'>Feign集成了Ribbon</span>***

利用Ribbon维护了PAYMENT服务列表信息，并且通过轮询实现了客户端的负载均衡，与Ribbon不同的是，==通过Fegin只需要要定义服务绑定接口并以声明式的方法==，即可实现服务调用



## 2. Feign和OpenFeign的区别

| Feign                                                        | OpenFeign                                                    |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| Fegin是Spring Cloud组件中的一个轻量级RESTful的HTTP服务客户端，Fegin内置了Ribbon，用来做客户端负载均衡，去调用服务注册中心的服务。Fegin的使用方式：使用Fegin的注解定义接口，调用这个接口就可以调用服务注册中心的服务 | OpenFegin是Spring Cloud在Fegin的基础上支持了Spring MVC的注解，如RequestMapping注解。OpenFegin的==FeginClient==注解可以解析SpringMVC的RequestMapping注解下的接口，并通过动态代理的方式得到实现类，实现类用于负载均衡并调用其他服务 |
| `<dependency>     <groupId>org.springframework.cloud</groupId>     <artifactId>spring-cloud-starter-feign</artifactId> </dependency>` | `<dependency>   <groupId>org.springframework.cloud</groupId>     <artifactId>spring-cloud-starter-openfeign</artifactId> </dependency>` |

## 3. 使用

1. 创建cloud-order-openFeign80工程

2. 主程序类

   ~~~java
   @SpringBootApplication
   @EnableFeignClients // 表示当前类是一个Feign客户端
   public class OrderMainFeign80 {
   
       public static void main(String[] args) {
           ConfigurableApplicationContext run = SpringApplication.run(OrderMainFeign80.class, args);
   
       }
   }
   ~~~

3. 配置文件

   ~~~yaml
   server:
     port: 80
   # 注册到Consul服务器的订单微服务端口号
   
   spring:
     application:
       name: cloud-order-openFeign80
   
     datasource:
       type: com.alibaba.druid.pool.DruidDataSource                # 当前数据源操作类型
       driver-class-name: org.gjt.mm.mysql.Driver                  # mysql驱动包
       url: jdbc:mysql:///cloud?serverTimezone=UTC&useSSL=false
       username: root
       password: 123456
   
   eureka:
     client:
       register-with-eureka: false
       service-url:
         defaultZone: http://eureka7001.com:7001/eureka, http://eureka7002.com:7002/eureka, http://eureka7003.com:7003/eureka

4. FeignService层：使用注解==FeignClient并且指明获取哪个微服务地址==，抽象方法与PAYMENT微服务中的service接口保持一致，注意这里的返回值类型与调用service层方法的返回值泛型保持一致

   ~~~java
   @Component
   @FeignClient(value = "CLOUD-PAYMENT-SERVICE") // 获取哪个微服务的地址
   public interface PaymentFeignService {
       // 注意这里返回值类型必须与需要调用的某个微服务的该方法泛型保持一致
       @GetMapping("/payment/get/{id}") // 对应该方法的uri
       CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);
   
       @GetMapping("/payment/selectall")
       CommonResult getAllPayment();
   
       @PostMapping("/payment/save")
       CommonResult save(@RequestBody Payment payment);
   }
   ~~~

5. controller层

   ~~~java
   @RestController
   @Slf4j
   public class orderFeignController {
   
       @Resource
       private PaymentFeignService service;
   
       @GetMapping("/consumer/payment/get/{id}")
       public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id){
           return service.getPaymentById(id);
       }
   
       @GetMapping("/consumer/payment/getAll")
       public CommonResult<Payment> getAllPayment(){
           return service.getAllPayment();
   
       }
   
       // 使用RequestBody注解表示使用json数据进行传输
       @PostMapping("/consumer/payment/save")
       public CommonResult save(@RequestBody Payment payment){
           CommonResult res = service.save(payment);
           log.info("插入记录数：" + res);
           return res;
       }
   }
   ~~~

6. 测试

调用controller层的getById方法，输出结果如下，并且其中已经内置了轮询算法

![image-20220503124134022](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220503124134022.png)



## 4. OpenFeign的超时控制

默认Feign客户端只等待1s，但是服务器端处理需要超过1s，导致Feign客户端直接放弃等待，直接返回报错信息。为了避免这种情况，需要设置Feign客户端的超时控制

![image-20220503125848552](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220503125848552.png)

在cloud-order-openFeign80工程的配置文件中开启配置

~~~yaml
# 设置Feign客户端超时时间 OpenFeign默认支持Ribbon
ribbon:
  ReadTimeout: 5000      # 建立连接所用的时间，适用于网络状况正常的情况下，两端连接所用时间
  ConnectTimeoue: 5000  # 建立连接后从服务器读取到可用资源所用的时间
~~~

## 5. 日志增强

Feign提供了日志打印功能，可以通过配置来调整日志级别，从而了解Feign中Http请求的细节，也就是==对Feign接口的调用情况进行监控和输出==

日志级别：

+ **NONE：**默认的，不显示任何日志
+ **BASIC：**记录请求方法，URL，响应状态码以及执行时间
+ **HEADERS：**除了BASIC中定义的信息，还有请求和响应的头信息
+ **FULL：**除了HEADERS中定义的头信息之外，还有请求和响应的正文以及元数据

----

在cloud-order-openFeign80工程定义配置类

~~~java
@Configuration
public class LoggerConfig {

    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
~~~

在在cloud-order-openFeign80工程的配置文件中指明对哪个接口进行日志监控

~~~yaml
# Feign日志以什么级别去监控接口
logging:
  level:
    com.hz.openfeign.service.PaymentFeignService: debug
~~~



# 10. 服务降级Hystrix

## 1. 简介

分布式系统面临的问题：**复杂分布式体系结构中的应用程序中有数十个依赖关系，每个依赖关系在某些时候将不可避免的失败**

Hystrix是一个用于处理分布式系统的**延迟**和**容错**的开源库，在分布式系统里，许多依赖不可避免的会调用失败，比如超时，异常等。Hystrix能够保证在一个依赖出现问题的情况下，==不会导致整体服务失败，避免级联故障，以提高分布式系统的弹性==

通过断路器的故障监控，==向调用方返回一个符合预期的，可处理的备选响应（FallBack），而不是长时间的等待或者抛出调用方无法处理的异常==，这样就保证了服务调用方的线程不会被长时间，不必要的占用，从而避免了故障在分布式系统中的蔓延，甚至雪崩

## 2. Hystrix中几个重要概念

+ 服务降级fallback：服务器忙，请稍后再试，不让客户端等待并立刻返回一个友好提示，出现以下情况会出现服务降级
  + **程序运行异常**
  + **超时**
  + **服务器宕机**
  + **服务熔断触发服务降级**
  + **线程池/信号量打满也会导致服务降级**

+ 服务熔断break：达到最大服务访问后，直接拒绝访问，然后调用服务降级方法并返回友好提示
+ 服务限流：秒杀高并发操作，请求进行排队，有序进行

## 3. 使用以及JMeter高并发测试

### a.hystrix-payment8001微服务

### b. JMeter高并发测试

开启JMeter，2w个并发请求都去访问paymentInfo_timeout服务，瞬间压垮8001端口

此时Tomcat的默认工作线程都会去处理该服务，没有多余的线程来分解压力和处理paymentInfo_success服务，导致该服务也会在等待响应中

![image-20220503151718904](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220503151718904.png)

![image-20220503151737491](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220503151737491.png)

结论：**JMeter测试仅仅针对于服务提供者8001本身，假如此时外部的消费者80端口也来进行访问，那么消费者只能等待，最终80端口服务无法得到满足**

### c. 创建消费者80微服务

使用2w个线程去访问8001，消费者80微服务再去访问正常的hystrix-payment微服务8001地址，要么页面在转圈圈等待，要么消费者端报超时错误

![image-20220503154655231](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220503154655231.png)



## 4. 解决方案

解决的要求：

+ 超时导致服务器处理变慢（页面一直在转圈）  -->   **不应该返回ErrorPage，可以选择不再等待的友好界面**
+ 出错（宕机或者程序运行出错）                          -->  **出错的处理逻辑**

解决思路：

+ ==服务8001超时==了，调用者80不能一直卡死等待，必须有服务降级
+ ==服务8001宕机==了，调用者80不能一直卡死等待，必须有服务降级
+ ==服务8001正常运行，调用者80自身出现故障或者有自我要求==（自己的等待时间小于服务提供者），自己处理服务降级

----

### a. 服务降级

+ 8001服务自身的问题：设置自身调用超时时间的峰值，峰值内可以正常运行，超过了需要有对应的方法进行处理，作为服务降级fallback

+ 业务类启用注解==**HystrixCommand**==，报异常后如何处理

  一旦调用服务方法失败并抛出了错误信息后，会自动调用@HystrixCommand标注好的fallbackMethod调用类中的指定方法

  ~~~java
   @HystrixCommand(fallbackMethod = "paymentInfo_TimeoutHandler",
              commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "3000")}) // 设置运行的最大等待时间为3s，超过3s进行服务降级，运行paymentInfo_TimeoutHandler方法
      public String paymentInfo_timeout(Integer id){
         // int i = 10 / 0;  故意运行出错
          try {
              TimeUnit.SECONDS.sleep(5);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
          return "Thread pool: " + Thread.currentThread().getName() + " paymentInfo_id: " + id + " time out";
      }
  
      public String paymentInfo_TimeoutHandler(Integer id){
          return "Thread pool: " + Thread.currentThread().getName()+"\t 系统繁忙或者运行报错, id: " + id+" ErrorHandler";
      }
  ~~~

  

+ 主启动类激活，添加注解==**EnableCircuitBreaker**==，测试8001微服务自身超时异常情况的fallback

![image-20220503170803606](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220503170803606.png)

+ 其次，对于80订单微服务，进行客户端的服务降级保护

  + 主配置类中开启==**EnableHystrix**==注解

  ~~~java
  @SpringBootApplication
  @EnableFeignClients
  @EnableHystrix
  public class HystrixOrderMain {
  
      public static void main(String[] args) {
          ConfigurableApplicationContext run = SpringApplication.run(HystrixOrderMain.class, args);
  
      }
  }
  ~~~

  + yaml

  ~~~yaml
  feign:
    hystrix:
      enabled: true
  ~~~

  + controller

  ~~~java
      // 设置客户端运行时间的上限，超过2s或者程序运行错误执行服务降级paymentInfoTimeoutHandler
      @GetMapping("/consumer/payment/hystrix/timeout/{id}")
      @HystrixCommand(fallbackMethod = "paymentInfoTimeoutHandler",
              commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "2000")})
      public String paymentInfo_timeout(@PathVariable("id") Integer id){
          // int i = 10 / 0;
          String res = service.paymentInfo_timeout(id);
          return res;
  
      }
  
      public String paymentInfoTimeoutHandler(Integer id){
          return "openfeign-hystrix-order80,client server run time over limit or runTime Exception o(-<>-)o";
      }

注意：对于每个方法配置一个服务降级方法，代码量太大，除了个别重要核心业务有专属的服务降级，其他的可通过注解**==DefaultProperties(defaultFallback = "GlobalFallback")==**统一跳转到全局服务降级方法。将通用的和独享的分开，避免代码冗余，减少代码量，仅仅是使用注解HystrixCommand，没有配置任何参数，出现异常则执行默认的全局服务降级方法，指明了则执行对应的服务降级方法

~~~java

import com.hz.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@Slf4j
@DefaultProperties(defaultFallback = "GlobalFallbackMethod")
public class OrderHystrixController {

    @Resource
    private PaymentHystrixService service;

    @GetMapping("/consumer/payment/hystrix/success/{id}")
    @HystrixCommand // 没有特别指明就是用统一的服务降级DefaultProperties
    public String paymentInfo_success(@PathVariable("id") Integer id){
        int i=10/0;
        String res = service.paymentInfo_success(id);
        return res;
    }
    // 设置客户端运行时间的上限，超过2s或者程序运行错误执行服务降级paymentInfoTimeoutHandler
    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    @HystrixCommand(fallbackMethod = "paymentInfoTimeoutHandler",
            commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "2000")})
    public String paymentInfo_timeout(@PathVariable("id") Integer id){
        // int i = 10 / 0;
        String res = service.paymentInfo_timeout(id);
        return res;

    }

    public String paymentInfoTimeoutHandler(Integer id){
        return "openfeign-hystrix-order80,client server run time over limit or runTime Exception o(-<>-)o";
    }

    // 全局服务降级GlobalFallbackMethod
    public String GlobalFallbackMethod(){
        return "GlobalFallback Method running...";
    } 

}
~~~

*<span style='font-size:20px; color:blue'>服务降级，客户端调用服务端时服务端宕机或关闭</span>*

本次服务降级处理是在客**户端80实现的**，与服务端8001没有关系，只需要为**Feign客户端接口添加一个服务降级处理的实现类**即可实现解耦

注意该方法只能适用于客户端连接服务器端时服务器端宕机，对于**程序本身的错误是不能进行使用的**

~~~java
// Hystrix之通配服务降级FeignFallback, 当服务器宕机或者关闭时调用改接口的实现类中的fallback方法进行服务降级
@Component
@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT", fallback = OrderFallbackService.class)
public interface OrderHystrixService {
~~~

~~~java
@Component
public class OrderFallbackService implements OrderHystrixService {
    @Override
    public String paymentInfo_success(Integer id) {
        return "OrderFallbackService paymentInfo_success exec...Server probably is died";
    }

    @Override
    public String paymentInfo_timeout(Integer id) {
        return "OrderFallbackService paymentInfo_timeout exec...Server probably is died";
    }
}
~~~



### b. 服务熔断

熔断机制是应对雪崩效应的一种微服务链路保护机制，当某个微服务出错不可用或者响应时间太长，会进行服务的降级，进而熔断该节点微服务的调用，快速返回错误的响应信息。==当检测到该节点微服务调用响应正常后，恢复调用链路==

熔断机制通过Hystrix实现，当失败的调用到一定阈值，默认是5s内20次调用失败，会启动熔断机制，使用的注解是**==HystrixCommand==**

服务的降级 ->  进而熔断 ->  恢复调用链路

---

+ 修改hystrix-payment8001微服务
+ 在PaymentHystrixService中增加方法，并且配置参数
+ 在PaymentHystrixController中调用service中的方法
+ 测试：输入正确的id以及输入错误的id，连续不断的输入错误的id并且测试，再使用正确的id，发现刚开始即使输入正确的id也会报错，慢慢的恢复正常

----

**总结：**

==熔断类型==分为三种

+ **熔断打开open**：请求不再进行调用当前服务，内部设置时钟一般为MTTR（平均故障处理时间），当打开时长达到所设时钟则进入半熔断状态
+ **熔断关闭closed**：不会对服务进行熔断，正常调用
+ **熔断半开half-open**：部分请求根据规则调用当前服务，如果请求成功且符合规则则认为当前服务恢复正常，关闭熔断

三个重要参数：==快照时间窗，请求总数阈值，错误百分比阈值==

+ **sleepWindowInMilliseconds：**断路器确定是否打开统计一些请求和错误数据，而统计的时间范围就是时间快照，默认为最近的10s
+ **requestVolumeThreshold：**在快照时间窗口内，必须满足请求总数阈值才有资格熔断，默认为20，也就是说在10s内如果Hystrix命令调用次数不足20次，即使所有请求都超时或者其他原因失败，断路器都不会打开
+ **errorThresholdPercentage：**当请求总数在快照时间窗口内超过了阈值（默认50%），如发生30次调用，有15次发生了超时异常，那么断路器就会打开

==断路器开启或关闭的条件==

+ 当满足一定的阈值（默认10s内超过20个请求）
+ 当失败率达到一定阈值（默认10s内超过50%的请求访问失败）
+ 满足以上两个阈值，断路器开启
+ 开启时，所有请求都不会进行转发
+ 一段时间以后（默认5s），此时断路器处于半开状态，允许其中的部分请求进行转发，如果成功，断路器会关闭，否则继续处于开启状态

所有Hystrix的参数配置

![image-20220504144006827](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220504144006827.png)

![image-20220504144056471](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220504144056471.png)

![image-20220504144137888](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220504144137888.png)

![image-20220504144209090](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220504144209090.png)

![image-20220504144225601](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220504144225601.png)

### c. 服务限流使用Sentinel

### d. Hystrix图形化Dashboard

+ 新建cloud-consumer-hystrix-dashboard9001

+ POM：==spring-cloud-starter-netflix-hystrix-dashboard==

+ YAML：server.port=9001

+ 主程序类+新注解==EnableHystrixDashboard==
+ 所有Provider微服务提供者（8001,8002,8003）都需要监控依赖配置spring-boot-starter-actuator
+ 启动cloud-consumer-hystrix-dashboard9001测试监控微服务8001
+ 如果想要监控某个微服务，需要在该微服务的主程序类中加上关于Servlet的配置

~~~java
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
~~~

开启Eureka7001，Hystrix-payment8001以及cloud-consumer-hystrix-dashboard9001三个微服务，输入链接http://localhost:9001/hystrix监控页

![image-20220504152135760](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220504152135760.png)

输入所需要监控的微服务地址http://localhost:8001/hystrix.stream，延迟默认2000ms，Title使用T3。访问8001的服务，查看监控

![image-20220504152346183](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220504152346183.png)



# 11. 服务网关Gateway

## 1. 简介

SpringCloud Gataway使用的是**Webflux**中的**reactor-netty**响应式编程组件，底层使用了Netty通讯框架，提供一种简单有效的统一的API路由管理方式。提供了网关基本的功能：安全，监控指标，限流，反向代理，熔断等

SpringCloud Gateway与Zuul的区别

+ Zuul是一个基于阻塞IO的API Gateway，每次IO操作都是从工作线程中选择一个执行，请求线程被阻塞到工作线程完成。Nginx使用C++实现，Zuul使用Java实现，所以zuul性能较差
+ SpringCloud Gateway建立在Spring5，Project Reactor以及Spring Boot2之上，使用的是非阻塞API
+ SpringCloud Gateway还支持WebSocket，并且与Spring有紧密的集成，非阻塞+函数式编程

三个重要概念

+ **Route路由：**构建网关的基本模块，由ID，目标URL，一系列的断言和过滤器组成。如果断言为true则匹配该路由
+ **Predicate断言：**参考的是JDK8中的函数式编程的java.util.function.Predicate，用于匹配Http请求中的所有内容，==如果请求与断言相匹配则进行路由==
+ **Filter过滤：**Spring框架中GatewayFilter的实例，使用过滤器，可以在请求被路由前或者之后对请求进行修改

web请求，通过一些匹配条件，定位到真正的服务节点，并在这个转发过程的前后，进行一些精细化控制。Predicate就是匹配条件，而filter即使一个过滤器

核心逻辑：**<span style='font-size:20px; color:red'>路由转发+执行过滤器链</span>**

## 2. 使用

+ 新建工程cloud-gateway-gateway9527
+ POM：**注意不要添加web，以及web-actuator监控的依赖**，否则运行会报错

~~~xml
 <!--gateway 依赖-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
~~~

+ 主程序类

~~~java
@SpringBootApplication
@EnableEurekaClient
public class GatewayMain9527 {

    public static void main(String[] args) {

        SpringApplication.run(GatewayMain9527.class, args);
    }
}
~~~

+ YAML

~~~yaml
server:
  port: 9527

spring:
  application:
    name: cloud-gateway
  # Gateway配置网关路由的yaml方式
  cloud:
    gateway:
      routes:
        - id: payment-route1         # 路由id 没有固定规则 但需要唯一
          uri: http://localhost:8001 # 对应哪个微服务的路由地址
          predicates:
            - Path=/payment/get/**   # 断言，路径相匹配的进行路由

        - id: payment-route2
          uri: http://localhost:8001
          predicates:
            - Path=/payment/lb/**

eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
  instance:
    hostname: cloud-gateway-service
~~~

+ 测试：开启Eureka7001，cloud-provider-payment8001以及cloud-gateway-gateway9527三个微服务，使用端口9527去访问8001中的get以及lb方法

![image-20220504194217061](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220504194217061.png)

![image-20220504194231566](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220504194231566.png)

**如何做路由映射？**

对于cloud-provider-payment8001微服务中的controller的访问地址，/payment/get/{id}以及/payment/lb。不想直接暴露8001端口，可以在8001之上套上一层9527网关进行访问

**Gateway网关路由的两种配置方式**

+ ==在配置文件yaml中配置==
+ ==代码中注入RouteLocator的Bean对象==：在gateway9527工程中增加配置类，使用链接localhost:9527/guonei和localhost:9527/guoji即可访问百度国内以及国际新闻的页面

~~~java
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customerRouteLocator1(RouteLocatorBuilder builder){
        RouteLocatorBuilder.Builder routes = builder.routes();
        routes.route("custom_route_payment",
                r -> r.path("/guonei").uri("http://news.baidu.com/guonei")).build();
        // 访问localhost:9527/guonei将会转发到uri指定的地址
        return routes.build();
    }

    @Bean
    public RouteLocator customerRouteLocator2(RouteLocatorBuilder builder){
        RouteLocatorBuilder.Builder routes = builder.routes();
        routes.route("custom_route_payment",
                r -> r.path("/guoji").uri("http://news.baidu.com/guoji")).build();

        return routes.build();
    }
}

~~~

## 3. 配置动态路由

**<span style='font-size:20px; color:blue'>通过微服务名实现动态路由</span>**

默认情况下Gateway会根据注册中心注册的服务列表，使用注册中心上微服务名为路径创建动态路由进行转发，从而实现动态路由

只需要在yaml配置文件中将之前配置的uri指定固定路径，更改为EurekaServer中的服务名即可

~~~yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true # 开启从注册中心动态创建路由的功能 利用微服务名进行路由转发
    routes:
	  uri: lb://cloud-payment-service
~~~

## 4. Predicate

SpringCloud Gateway包括许多内置的**Route Predicate工厂**，所有这些**Predicate都与Http请求的不同属性匹配**。多个Route Predicate工厂可以进行组合

SpringCloud Gateway创建Route对象时，**使用RoutePredicateFactory创建Predicate对象，Predicate对象可以赋给Route**，所有这些谓词都匹配Http请求的不同属性，**多种谓词工厂可以进行组合，并且通过逻辑and**，所有的Predicate都满足才能转发

![image-20220504220754175](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220504220754175.png)

**<span style='font-size:20px; color:blue'>Route Predicate类型</span>**

+ After：此谓词匹配在指定日期时间之后发生的请求
+ Before：此谓词匹配在指定日期时间之前发生的请求
+ Between：此谓词匹配在指定日期时间之间发生的请求
+ Cookie：需要两个参数，一个是==Cookie name，一个是正则表达式==。路由规则会通过获取对应的Cookie name值和正则表达式去匹配，如果匹配上就会执行
+ Header：与Cookie类似，如Header=X-Request-Id, \d+ ，请求头必须包含X-Request-Id属性并且值为整数的正则表达式
+ Host：一组匹配的域名列表，这个模板是一个ant分隔的模板，用.号作为分隔符，通过参数中的主机地址作为匹配规则
+ Mathod：请求方式GET，POST，DELETE，PUT
+ Path：根据路径进行匹配
+ Query：带有查询条件的，如Query=age，\d+，参数中必须包含age属性并且值为整数才能匹配

## 5. Filter

路由过滤器可用于修改进入的Http请求和返回的Http响应，**路由过滤器只能指定路由进行使用**

SpringCloud Gateway内置了多种路由过滤器，都由==GatewayFilter的工厂类==产生

**<span style='font-size:20px; color:blue'>Filter类型</span>**

主要分为GatewayFilter和GlobalFilter（总共32种GatewayFilter）

**<span style='font-size:20px; color:blue'>自定义全局GlobalFilter</span>**

在Gateway9527工程中添加filter的配置类，实现==GlobalFilter, Ordered==两个接口，并且重写其中的filter方法

~~~java
@Component
@Slf4j
public class MyLogGatewayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("*********MySelf Global Filter: " + new Date());
        // 通过Gateway9527端口的地址访问必须带有uname的key和对应的value
        String uname = exchange.getRequest().getQueryParams().getFirst("uname");
        if(uname == null){ // 非法用户不放行
            log.info("********username can not be null");
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange); // 合法用户直接放行
    }

    // 返回的数值越小 过滤器链中该filter执行的优先级越高
    @Override
    public int getOrder() {
        return 0;
    }
}
~~~

# 12. 服务配置SpringCloud Config

## 1. 简介

微服务需要将单体应用中的业务拆分成一个个子服务，每个服务的粒度相对较小，因此系统中会出现大量的服务。**由于每个服务都需要各自的配置信息才能运行，所以一套集中式的，动态的配置管理系统设施是必不可少的**

SpringCloud提供了==ConfigServer==来解决这个问题，为微服务架构中的微服务提供集中化的外部配置支持，配置服务器为==每个不同微服务应用==的所有环境提供了一个==中心化的外部配置==

SpringCloud Config分为==服务端==和==客户端==两部分

+ 服务端也称为分布式配置中心，是一个独立的微服务应用，用来连接配置服务器并为客户端提供获取配置信息，加密解密等访问接口

+ 客户端采用指定的配置中心来管理应用资源，以及业务相关的配置内容，在启动的时候从配置中心获取和加载配置信息来配置服务器，默认采用git来存储配置信息

SpringCloud Config的作用

+ 集中管理配置文件
+ 不同环境不同配置，动态化的配置更新，分环境部署比如dev/test/prod/beta/release
+ 运行期间动态调整配置，不再需要在每个服务部署的机器上编写配置文件，服务会向配置中心统一获取配置自己的信息
+ 当配置发生变化，服务不再需要重启即可感知到配置的变化并应用最新的配置
+ 将配置信息以REST接口的形式暴露，post和curl等访问刷新即可

## 2. 与Github整合部署

+ 在Github上新建一个Repository
+ 获取Repository的git地址：git@github.com:Huzhen757/SpringCloud-Config.git
+ 本地硬盘目录上新建git仓库并clone：`git clone git@github.com:Huzhen757/SpringCloud-Config.git`，在本地仓库中生成一个文件夹，与新建的Repository同名
+ 新建cloud-config-center3344工程，作为cloud的配置中心模块cloudConfig Center
+ POM

~~~xml
  <!--Config Server-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
~~~

+ YAML

~~~yaml
server:
  port: 3344

spring:
  application:
    name: cloud-config-center

  cloud:
    config:
      server:
        git:
          uri: https://github.com/Huzhen757/SpringCloud-Config.git # github上的git仓库名(中心化的外部配置)
          search-paths:
            - SpringCloud-Config # 搜索目录
          username: xxxx
          password: xxxx
          force-pull: true
      label: main # 读取哪个分支 main master等

eureka:
  client:
    service-url:
      defaultZone: http://localhost:7001/eureka

~~~

+ 主启动类

~~~java
// Config服务器端
@SpringBootApplication
@EnableConfigServer
public class ConfigCenterMain {

    public static void main(String[] args) {
        SpringApplication.run(ConfigCenterMain.class, args);
    }

}
~~~



+ windows下修改host文件，增加映射：`127.0.0.1 config3344.com`

![image-20220505154546151](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220505154546151.png)

+ 测试通过Config微服务是否可以从Github上获取配置内容：启动微服务3344，访问http://config3344.com:3344/main/config-dev.yaml即可获取到github上对应的配置文件信息

![image-20220505173057559](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220505173057559.png)

+ **本地文件上传到github上**

  在需要上传的文件中打开Git Bash窗口，输入命令

  + `git init`
  + `git add .`
  + `git commit -m "info"` (info随便写)
  + `git remote add origin  repository的地址`(对应github上你的某一个repository的SSH地址)
  + `git push -u origin main`（可用git branch查询当前上传的是哪一个分支）

----

**<span style='font-size:20px; color:blue'>配置读取规则</span>**

+ **/{label}/{application}-{profile}.yml**

main分支（或master分支）: 

http://config3344.com:3344/main/config-dev.yml  

http://config3344.com:3344/main/config-test.yml 

 http://config3344.com:3344/main/config-prod.yml 

dev分支  

http://config3344.com:3344/dev/config-dev.yml  

http://config3344.com:3344/dev/config-test.yml 

 http://config3344.com:3344/dev/config-prod.yml 

+ **/{application}-{profile}.yml**：不带label的，会去访问默认分支（default branch）

http://config3344.com:3344/config-dev.yml  

http://config3344.com:3344/config-test.yml 

 http://config3344.com:3344/config-prod.yml 

+ **/{application}/{profile}/label**

http://config3344.com:3344/config/dev/master  

http://config3344.com:3344/config/test/master

 http://config3344.com:3344/config/prod/master

----

**<span style='font-size:20px; color:blue'>Config客户端配置与测试</span>**

+ 新建cloud-config-client3355工程
+ POM

~~~xml
 <!--Config Client-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
~~~

+ 主程序类

~~~java
@SpringBootApplication
@EnableEurekaClient
public class ConfigClientMain {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientMain.class, args);
    }
}
~~~

+ yaml

~~~yaml
# Boostrap Context 负责从外部资源加载配置属性并且解析配置
server:
  port: 3355

spring:
  application:
    name: config-client
  # Config Client配置
  cloud:
    config:
      # label下的{name}-{profile}合并成config-dev 与uri组合最后访问路径http://config3344.com:3344/main/config-dev.yaml
      label: main  # 分支名
      name: config # 配置文件名
      profile: dev # 读取的后缀名
      uri: http://localhost:3344

eureka:
  client:
    service-url:
      defaultZone: http://localhost:7001/eureka
    register-with-eureka: true
    fetch-registry: true
# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: "*"
~~~

+ controller

~~~java
RestController
@RefreshScope // 开启刷新功能
public class ConfigClientController {
    // 测试是否能从客户端3355获取到 3344在github上得到的配置信息config-dev.yaml
    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config/info")
    public String getConfigInfo(){
        return configInfo;
    }
}
~~~

**关于配置文件的说明**

application.yaml是用户级的资源配置项，bootstrap.yaml是系统级的，优先级更高

SpringCloud会创建一个==“Bootstrap Context”==，作为Spring应用的==Application Context==的父上下文。初始化时，Bootstrap Context负责**从外部资源加载配置属性并且解析配置**，两个上下文**共享一个从外部获取的Environment**

Bootstrap属性有高优先级，默认不会被本地配置所覆盖。Bootstrap Context和Application Context有**不同的规则**，所以新增一个bootstrap.yaml，==保证Bootstrap Context和Application Context配置的分离==。因此，对于Client模块下的application.yaml需要修改为bootstrap.yaml

+ 测试：启动Eureka7001,3344以及3355服务，3344自测运行，再测试3355能否读取到3355从github上获取的配置信息

![image-20220505195952956](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220505195952956.png)

![image-20220505200004096](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220505200004096.png)



## 3. Config动态刷新问题

以上存在的问题：如果Linux运维修改Github上的配置文件内容，**刷新ConfigServer3344服务，发现ConfigServer配置中心可以立刻响应**。而==刷新ConfigClient3355服务，发现ConfigClient客户端并没有发生响应，除非服务自身重启才能读取到最新的配置信息==

+ 在ConfigClient3355服务中引入actuator监控
+ 修改YAML，暴露监控端口
+ 在controller中增加注解**==RefreshScope==**
+ 最后需要运维人员发送一个POST请求刷新3355服务

`curl -X POST "http://localhost:3355/actuator/refresh"`

+ 不用需要重启3355，就可以获取到最新的配置信息

但是，此方法依旧存在问题，如果有多个微服务3355/3366...，每个微服务都需要执行一次POST请求的手动刷新工作，能否**使用广播机制，做到一次通知处处生效？并且可以做到精确通知，某些服务需要通知，某些服务不需要通知？** ==**引入消息总线Bus**==



# 13. 消息总线SpringCloud Bus

## 1. 简介

分布式自动刷新配置功能，**SpringCloud Bus**配合**SpringCloud Config**使用可==**实现配置的自动刷新**==

Bus支持两种消息代理：**RabbitMQ和Kafka**

SpringCloud Bus用来将分布式系统的节点与轻量级消息系统链接起来的框架，==整合了Java的事件处理机制和消息中间件的功能==

SpringCloud Bus能管理和传播分布式系统之间的消息，类似一个分布式执行器，可用于广播状态更改，事件推送等，也可以当作微服务间的通信通道

**<span style='font-size:20px; color:blue'>什么是总线</span>**

在微服务架构的系统中，通常会使用==轻量级的消息代理==来构建一个共用的消息主题，并且让系统中所有微服务实例都连接上来，由于==该主题中产生的消息会被所有实例监听和消费，所以称之为消息总线。==在总线上的每个实例，都可以方便的广播一些需要其他连接在该主题上的实例都知道的消息

**<span style='font-size:20px; color:blue'>基本原理</span>**

ConfigClient实例都监听MQ中同一个topic（默认是SpringCloudBus），当一个服务刷新数据时，会将这个信息存入到topic中，于是其他监听同一个topic的服务就能获取到最新通知，然后去更新自身配置

## 2. Bus动态刷新

+ 配置好RabbitMQ环境

![image-20220506203253840](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220506203253840.png)

![image-20220506203330310](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220506203330310.png)

+ 新建config-client3366工程，与config-client3355工程类似
+ **设计思想**

**利用消息总线触发一个客户端/bus/refresh，进而刷新所有的客户端配置**

**利用消息总线触发一个服务端ConfigServer的/bus/refresh端点，进而刷新所有客户端的配置**

方法一的缺点

1. 打破了微服务 的职责单一性，微服务本身是业务模块，本不应该承担配置刷新的职责
2. 破坏了微服务每个节点的对等性
3. 有一定的局限性，在微服务迁移时，网络地址会发生变化，想要做到自动刷新，会增加更多的修改

+ 给config-center3344工程配置 ==中心服务端，添加消息总线支持==

三个工程的POM中都需要添加RabbitMQ依赖

~~~xml
        <!--添加消息总线RabbitMQ的支持-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
~~~

YAML中添加RabbitMQ的配置，以及暴露ConfigCenter的监控端点

~~~yaml
spring:
# RabbitMQ的配置 注意15672是web界面的访问端口 5672才是RabbitMQ的访问端口
  rabbitmq:
    host: 主机IP地址
    port: 5672
    username: xxxx
    password: xxxx
# 暴露Bus刷新配置的端点
management:
  endpoints:
    web:
      exposure:
        include: "bus-refresh"
~~~

+ 给config-client3355工程==添加消息总线支持==

+ 给config-client3366工程==添加消息总线支持==

两个Config-Client都需要在yaml中增加RabbitMQ的配置

~~~yaml
spring:  
  # 配置rabbitMQ
  rabbitmq:
    host: 主机ip地址
    username: xxxx
    password: xxxx
    port: 5672
~~~

+ 最后需要运维人员发送一个POST请求刷新Config-Center配置中心即可通知全部配置客户端

`curl -X POST "http://localhost:3344/actuator/bus-refresh"`

+ 测试

![image-20220506165845704](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220506165845704.png)

![image-20220506165900659](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220506165900659.png)

![image-20220506165921808](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220506165921808.png)

## 3. 定点通知

**可以指定某一个实例生效而不是全部通知**

`http://localhost:配置中心端口号/actuator/bus-refresh/{destination}`

/bus/refresh请求不再发送到具体的服务实例上，而是发给Config Server并通过destination参数指定具体的服务实例

只需要通知config-client3355，发送POST请求 `http://localhost:配置中心端口号/actuator/bus-refresh/config-client:3355`



# 14. 消息驱动SpringCloud Stream

## 1. 简介

SpringCloud Stream是一个构建消息驱动微服务的框架，应用程序通过inputs或outputs与SpringCloud Stream中==**binder对象**==交互。通过配置来binding，而SpringCloud Stream的binder对象负责与消息中间件进行交互。只需要专注如何与SpringCloud Stream交互即可方便使用消息驱动的方式

通过使用Spring Integration来连接消息代理中间件以实现消息事件驱动。SpringCloud Stream为不同的消息中间件提供个性化的自动化配置实现，引用了==发布-订阅，消费组，分组==三个核心概念，SpringCloud Stream**屏蔽底层消息中间件的差异，降低切换成本，统一消息的编程模型**

**<span style='font-size:20px; color:blue'>设计思想</span>**

+ 标准MQ

生产者消费者之间靠==消息==传递信息内容；消息必须走特定的==通道==；消息通道的消息如何被消费，谁负责==收发处理==

+ SpringCloud Stream

如果使用RabbitMQ和Kafka，由于两个消息中间件架构上的不同，RabbitMQ有exchange，Kafka有Topic和Partition分区。SpringCloud Stream提供一种**解耦的方式**，通过定义绑定器作为中间层，完美的==实现应用程序与消息中间件细节之间的隔离==；通过向应用程序暴露统一的Channel通道，使得应用程序不需要再考虑各种不用的消息中间件的实现。==通过定义绑定器Binder作为中间层，实现了应用程序与消息中间件细节之间的隔离==

**Binder中的INPUT对应于消费者，OUTPUT对应于生产者**

Stream中的**消息通信方式遵循了发布-订阅模式，Topic主题进行广播（RabbitMQ中就是Exchange）**

## 2. 常用注解和API

+ **Binder：**方便连接中间件，屏蔽底层不同消息中间件的差异
+ **Channel：**队列queue的一种抽象，在消息通讯系统中就是实现存储和转发的媒介，通过channel对队列进行配置
+ **Source和Sink：**从Stream发布消息就是输出，接受消息就是输入

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220506202513303.png" alt="image-20220506202513303" style="zoom:50%;" />



| 组成            | 说明                                                         |
| --------------- | ------------------------------------------------------------ |
| Middleware      | 中间件，支持RabbitMQ和Kafka                                  |
| Binder          | 应用程序与消息中间件之间的封装，通过Binder可以方便去连接中间件，也可以动态的改变消息类型(Kafka中的topic，RabbitMQ中的exchange)，都可以通过配置文件实现 |
| @Input          | 注解标识输入通道，通过该输入通道接收到的消息进入应用程序（消费者） |
| @output         | 注解标识输出通道，发布的消息通过该通道离开应用程序（生产者） |
| @StreamListener | 监听队列，用于消费者的队列的消息接受                         |
| @EnableBinding  | 信道channel和exchange绑定在一起                              |

## 3. 使用

+ 新建子工程cloud-stream-rabbitmq-provider8101，作为生产者进行发布消息
+ 新建子工程cloud-stream-rabbitmq-consumer8102，作为消息接受模块
+ 新建子工程cloud-stream-rabbitmq-consumer8103，作为消息接收模块





## 4. 两个消息接收模块存在的问题

### a. 重复消费问题

==同一条消息被两个消息接受模块都接受并处理==。在订单系统中做集群部署，都会从RabbitM中获取订单信息，那么一个订单如果同时被两个服务同时获取到，那么会造成数据错误。

![image-20220507133344490](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507133344490.png)

![image-20220507133400469](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507133400469.png)

![image-20220507133411361](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507133411361.png)

使用Stream中的**==消息分组==**来解决，在Stream中处于**同一个group中的多个消费者是竞争关系**，才能够保证消息只会被其中一个应用消费一次，**不同组是可以重复消费的**

导致原因：两个消息接受模块默认分组的group是不同的，组流水号不同，所以造成重复消费

![image-20220507133537210](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507133537210.png)

解决：**自定义配置分组，将两个消息接受模块配置成为一个组**

**<span style='font-size:20px; color:blue'>8102和8103实现同组内的轮询</span>**

在两个消息接受模块中的yaml文件中增加group的配置

~~~yaml
spring:
  cloud:
    stream:
      bindings:
        input: # 消费者
          destination: studyExchange      # Exchange名称
          content-type: application/json # 设置消息类型，使用json，文本数据使用"text/plain"
          binder: {defaultRabbit}       # 设置需要绑定哪一个消息服务(对应消息服务名)
          group: receiveSame
~~~

重新测试发送8条消息，查看8102和8103的接受消息情况

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507134627035.png" alt="image-20220507134627035" style="zoom:80%;" />

<img src="C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507134646304.png" alt="image-20220507134646304" style="zoom:80%;" />

![image-20220507134659480](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507134659480.png)

### b. 消息持久化问题

当停止8102和8103服务，并且去掉8102中的分组配置`group：serviceSame`，只保留8103中的分组配置信息。重新启动两个消息接受服务，8101发送4条消息，8102服务的后台并没有打印出来任何消息，而8103服务的后台打印出MQ中全部的消息，由于使用了**消息分组**不会造成==消息的丢失==





# 15. 服务链路跟踪Sleuth

在微服务框架中，一个由客户端发起的请求在后端系统中会经过多个不同的服务节点调用来协同产生最后的请求结果，**每一个前段请求都会形成一条复杂的分布式服务调用链路，链路中的任何一环出现高延迟或错误都会引起整个请求最后的失败**

## 1. Sleuth之zipkin安装

1. 下载地址：https://repo1.maven.org/maven2/io/zipkin/zipkin-server/
2. 运行：`java -jar zipkin-server-2.23.0-exec.jar`
3. web页面访问：http://localhost:9411/zipkin（只有后台运行着zipkin的jar包，才能打开web页面）

调用链路中的概念

表示-请求链路，一条链路通过==Trace ID==唯一标识，==Span==标识发起的请求信息，每个Span通过==parent Id==关联起来

Trace：**类似于树结构中的span集合，表示一条调用链路，存在唯一标识**

span：**表示调用链路来源，也就是一次请求信息**

## 2. Sleuth链路监控

以Eureka7001，payment8001和Order80为例

+ 在payment8001和Order80服务中添加sleuth依赖

~~~xml
        <!--引入zipkin依赖(内置了Sleuth)-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zipkin</artifactId>
        </dependency>
~~~

+ payment8001和Order80的配置文件中添加zipkin和sleuth配置

~~~yaml
spring:
  # 添加关于Sleuth和zipkin的配置
  zipkin:
    base-url: http://localhost:9411/ # zipkin的web地址
  sleuth:
    sampler:
      probability: 1  # 采样率在0-1之间，1表示全部采样
~~~

+ payment8001和Order80的controller中增加方法

~~~java
    // Sleuth链路监控测试
    @GetMapping("/payment/sleuth")
    public String getZipkin(){
        return "this is Payment8001 service base on Eureka, trying use sleuth to monitor...";
    }

~~~

~~~java
    // Sleuth链路监控测试
    @GetMapping("/consumer/payment/sleuth")
    public String getZipkin(){
        String res = restTemplate.getForObject("http://localhost:8001" + "/payment/sleuth", String.class);
        return res + "\t this order80 service base on Eureka...";
    }
~~~

+ 测试order80中controller中的方法
+ 打开sleuth的web页面查找cloud-payment-service或者cloud-order-service的Trace

![image-20220507150633487](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507150633487.png)

![image-20220507150653082](C:\Users\Huzhen\AppData\Roaming\Typora\typora-user-images\image-20220507150653082.png)















































































