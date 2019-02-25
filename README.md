# mcqueen
mcqueen[pistencup](https://github.com/pistencup/introduction)工具包的Java部分, 关于pistencup工具包请参考：[pistencup](https://github.com/pistencup/introduction).

# 其他语言支持
如果你在寻找其他语言支持, 请参考: [已提供的支持](https://github.com/pistencup/introduction#%E5%B7%B2%E6%8F%90%E4%BE%9B%E7%9A%84%E6%94%AF%E6%8C%81)

# 如何使用
1. 目前需要自行编译mcqueen-racetrack和mcqueen-camera并添加到依赖.
2. mcqueen-racetrack包提供在多个远程调用间传递请求上下文(`CloudContext`)的能力.  
添加依赖后, 直接在`SpringBootApplication`上添加注解:`@EnableMcqueenRacetrack`即可启用.
```java
@SpringBootApplication
@EnableMcqueenRacetrack
@EnableFeignClients
public class SampleServerAApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleServerAApplication.class, args);
	}
	
}
```
3. mcqueen-camera包提供远程调用的记录能力.  
添加依赖后, 直接在`SpringBootApplication`上添加注解:`@EnableMcqueenCamera`即可启用.  
`@EnableMcqueenCamera`会自动引入`@EnableMcqueenRacetrack`.
```java
@SpringBootApplication
@EnableMcqueenCamera
@EnableFeignClients
public class SampleServerAApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleServerAApplication.class, args);
	}

}
```

# 配置项
你可以在没有任何配置的情况下运行mcqueen toolkit. 下列配置项可以帮助你更细致的控制程序的行为, 全部配置项及默认值如下:
```yaml
mcqueen:
  racetrack:
    #mcqueen.racetrack默认通过`McqueenRequestFilter`提供一个`HttpServletRequestWrapper`来支持后续功能中对输入流的读取功能.  
    #如果你不需要这个功能, 或者已经通过其他途径提供了这个能力, 请设置为`false`来避免对内存的浪费.
    enable-request-wrapper: true
  camera:
    #下列4个配置项是对请求处理记录的限制, 超过配置长度或数量的请求信息将不会被记录.  
    
    #从InputStream中读取的body的最大长度. 
    #如果发生截断, 则设置ActionRecord.request.isBodyTruncated字段为true
    max-body-log-length: 2147483647 #Integer.MAX_VALUE
    
    #从request.getParameterMap()中获取参数的最大数量
    #如果发生截断,则ActionRecord.request.isParamTruncated会被设置为true
    max-param-key-length: 2147483647 #Integer.MAX_VALUE
    
    #从request.getParameterMap()中获取参数的最大数量
    #如果发生截断,则ActionRecord.request.isParamTruncated会被设置为true
    max-param-count: 2147483647 #Integer.MAX_VALUE
    #从request.getParameterMap()中获取的参数值的最大长度
    #如果发生截断,则ActionRecord.request.isParamTruncated会被设置为true
    max-param-value-length: 2147483647 #Integer.MAX_VALUE
    
    #kafka配置
    kafka:
      topic: pistencup-camera
      batch-size: 1000
      linger: 0
      bootstrap-servers: localhost:9092
```
如果配置中不包含kafka配置节, 则使用默认的记录器.  
默认的记录器会使用`org.apache.commons.logging.Log`将信息输出到日志.

# 一些细节
1. `@EnableMcqueenRacetrack`注解  
启用racetrack包的能力, 在所有请求中增加`CloudContext`, 同一外部请求处理过程中, 
所有处理节点可以获得一个一致的标识并对节点自身也进行标识, 进而获得完整的处理链条,
或者在多个处理过程间共享数据. racetrack包工作方式如下:  
![racetrack_flow.png](https://github.com/pistencup/introduction/blob/master/racetrack_flow.png?raw=true)  

2. `@EnableMcqueenCamera`注解  
启用camera包的请求记录能力, 将处理链和每个处理节点的参数和处理结果记录到指定的输出中.  
它包含2个参数:  
2.1 `basePackages`(`value`): 需要启用处理记录功能的命名空间数组.  
2.2 `basePackageClasses`: 需要启用处理记录功能的命名空间的标记类型数组.  
这两个参数的行为与`ComponentScan`的行为类似.

3. `@Shadow`注解
指定单个方法不被camera记录. 用来在处理记录中排除不必要记录的方法.

4. 处理记录的规则, 请求记录切面的定义规则如下:  
```java
@Aspect
public class CameraAspect {
    @Pointcut(
            "(!@within(org.springframework.cloud.openfeign.FeignClient)) &&" + //except feign clients
            "(!@annotation(com.github.pistencup.mcqueen.camera.Shadow)) && " + //except Shadow method
            //contains all web action
            "(@annotation(org.springframework.web.bind.annotation.RequestMapping) ||" +
            " @annotation(org.springframework.web.bind.annotation.GetMapping) ||" +
            " @annotation(org.springframework.web.bind.annotation.PostMapping) ||" +
            " @annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
            " @annotation(org.springframework.web.bind.annotation.DeleteMapping) ||" +
            " @annotation(org.springframework.web.bind.annotation.PatchMapping))"
    )
    public void request() {
    }
}
```

# 关于`CloudContext`对象: 
`CloudContext`是调用上下文的持有者, 主要提供以下成员:
   
   | Field | 功能 | 描述 |
   | :--- | :--- | :--- |
   | RequestID | 请求标识 | 当前请求第一次到达服务端时生成的唯一标识, 这个标识会在所有后续调用中传递 |
   | PreviousSpanID | 上一个处理节点标识 | 请求头中获得的上一个处理块的标识, 用于构造请求链, 如果请求头中这个数据为空, 则说明这是第一个处理节点 |
   | CurrentSpanID | 当前处理块的标识 | 为当前处理节点生成的标识, 对于请求链中的第一个请求,这个值与RequestID一致 |
   | CallIndex | 调用序号 | 从请求头中获得的当前处理块在同级处理过程中的调用序号 |
   | GroupName | 处理分组 | 未完成功能, 用于标记当前请求由哪个分组的服务节点处理 |
   
应用中请 ***不要*** 从构造函数构造`CloudContext`对象,  
使用`CloudContext.getCurrent()`或IOC容器来获取对每个请求共享的对象,避免对调用过程分析造成困扰.
   
更多例子,请clone并参考示例项目.
