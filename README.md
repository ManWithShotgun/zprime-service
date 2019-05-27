# # Issues

## ## maven compiler plugin invalid target release: 11
Resolution: JAVA_HOME java should be the same as in version of java in project

## ## /bin/bash^M: bad interpreter: No such file or directory

or **Caused by: java.io.IOException: Cannot run program "/app/pythia_runner.sh": error=2, No such file or directory**

[source](https://stackoverflow.com/a/14219160)

In Notepad++ open `.sh` and in the bottom right of the screen select `Unix (LF)` then save file

## ## Spring Autowired throws NPE

[source](https://stackoverflow.com/a/41838937)

NPE when try access to Autowired field in constructor. Field injection is done by Spring after the constructor is called.

```java
@Service
public class ServiceImpl {
    
    @Autowired
    private Environment env;
    
    public ServiceImpl() {
        env.getProperty("prop"); // NPE here
    }
}
```

Resolution:

```java
@Service
public class ServiceImpl {
    
    @Autowired
    public ServiceImpl(Environment env) {
        env.getProperty("prop"); // NPE here
    }
}
```

## ## org.springframework.context.ApplicationContextException: Failed to start bean 'subProtocolWebSocketHandler'; nested exception is java.lang.IllegalArgumentException: No handlers
[source](https://github.com/spring-projects/spring-framework/issues/21889#issuecomment-453477953)

Reproduced when run compiled jar file. It happens because call bean `webSocketMessageBrokerStats` (in `@PostConstruct`) hasn't(?) initialized inner beans.

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketMessageBrokerStats webSocketMessageBrokerStats;

    @PostConstruct
    public void init() {
        webSocketMessageBrokerStats.setLoggingPeriod(600 * 1000);
    }
...
```

Resolution: implements `BeanPostProcessor` for `WebSocketConfiguration` (not need override methods).
The solution adds few INFO logs before start app.

---
# Debug

For server attach (for JDK 9 and later):
```dockerfile
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Xdebug", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000", "-jar","/app.jar"]
```

and Remote connection from IDEA

# Hot-reload

**the section in alf-guide/spring/boot-hot-reload**

# Run

1. run docker
2. run config with org.springframework.boot.devtools.RemoteSpringApplication
3. run remote debug

hot reload class: Ctrl + Shift + F9

---
# # Docker Compose Run

```shell
docker-compose up
```

Stop and remove containers
```shell
docker-compose down
```

Stop containers
```shell
docker-compose stop 
```

---
docker build -t zprime:demo .

docker run -p 8080:8080 -p 8000:8000 --name zprime --rm=true zprime:demo

docker stop zprime

---
# Mount (volume)

---
docker run -v d:/MyPrograms/Java/_spring/zprime-service/target:/data -p 8080:8080 -p 8000:8000 --name zprime --rm=true zprime:demo
docker run -v d:/Root/MyPrograms/Java/zprime-service/target:/data -p 8080:8080 -p 8000:8000 --name zprime --rm=true zprime:demo

sudo docker run -p 8080:8080 -d manwithshotgun/zprime:prod

# Copy result to host

docker cp <containderId>:/app/pythia8226_export/zprime/table_0.003.txt table_0.003.txt

---
# Helpful (Linux)


[source](https://stackoverflow.com/a/823525)
Q: Multiple threads reading from the same file
A: If you don't write to them, no need to take care of sync / race condition.

---
# # Redis

docker run -p 6379:6389 --name redis --rm=true redis

[source](https://stackoverflow.com/a/51647172)

---
## ## Redis persistence

[source](https://redis.io/topics/persistence)

Used RDB (by default) strategy and Amazon S3 for volume

---
## ## Spring Jedis uses connection pool OOB
> Redis Template is thread safe but only when it uses connection pooling

I have checked `JedisConnectionFactory` uses pool by default as it can be inspected by using `getUsePool` method 
(returns true) and checking the pool config using `getPoolConfig` method gives you `maxTotal=8`, `maxIdle=8` and `minIdle=0`

Yes, `JedisConnectionFactory` uses pool by default but the number of simultaneous connections 
(multi-threading environment) are 8 by default and for heavy load applications, these default configurations does not work efficiently.

Code that worked correctly:

```java
@Bean
JedisConnectionFactory jedisConnectionFactory() {

    JedisConnectionFactory jedisConnectionFactory = null;

    try {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostName,
                port);
        jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        jedisConnectionFactory.getPoolConfig().setMaxTotal(50);
        jedisConnectionFactory.getPoolConfig().setMaxIdle(50);
    } catch (RedisConnectionFailureException e) {
        e.getMessage();
    }

    return jedisConnectionFactory;
}


@Bean
public RedisTemplate<String, Object> redisTemplate() {
    final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
    template.setConnectionFactory(jedisConnectionFactory());
    template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
    template.setEnableTransactionSupport(true);
    return template;
}
```

## ## Save raw jedis code

The code implemented for using pool of connection. The `Callback` in `execute` guarantees that `multi` and `exec` will be invoke in one thread. 

```java
// aMap is hashOperations
// HashOperations<String, MyKey, MyValue> aMap = redisTemplate.opsForHash();

//        if (aMap.hasKey("qqq", id)) {
//            String res = (String) aMap.get("qqq", id);
//            log.info(id + " already exists: " + res);
//            return res;
//        }
//        aMap.put("qqq", id, "-1");
        String res = (String) aMap.get("qqq", id);
        log.info("Before: " + id + " - Has " + res);
        template.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.watch("qqq");
                log.info("Start calc");
                String result = pythiaService.calculate(new PythiaRequest(ksi, mass));
                operations.multi();
                aMap.put("qqq", id, result);
                return operations.exec();
            }
        });
        // if empty - call calculate
//        pythiaService.calculate();
        res = (String) aMap.get("qqq", id);
        log.info(String.valueOf(aMap.size("qqq")));
```

[examples](https://www.concretepage.com/spring-4/spring-data-redis-example)
Simple code for test that Jedis connected to redis and value save works
```java
ZprimeItem zprimeItem = new ZprimeItem(213L, "qwe", "qqq", "qwe");
zprimeRepository.save(zprimeItem);
zprimeRepository.save(zprimeItem);
zprimeRepository.findAll().forEach(zprimeItem1 -> {
    System.out.println(zprimeItem1.getId());
});

or

MyKey key = new MyKey("Jhon", "+138129129113");
MyValue value = new MyValue("Pushkina street", "Moscow");
HashOperations<String, MyKey, MyValue> hashOperations = redisTemplate.opsForHash();
hashOperations.put("myKey", key, value);
MyValue mappedValue = hashOperations.get("myKey", key);
MyValue newValue = new MyValue("Tverskaya street", "Moscow");
hashOperations.putIfAbsent("myKey", key, newValue);
```

---
## ## Jedis Cache examples
[examples](https://www.concretepage.com/spring-boot/spring-boot-redis-cache#EnableCaching)
[examples](https://www.journaldev.com/18141/spring-boot-redis-cache)

## ## Redis
[example](https://www.baeldung.com/spring-data-redis-tutorial)

```shell
docker pull redis
```

```shell
docker run -p 6379:6379 -d redis
```

# # Angular part readme file

1. backend `pom.xml`

The plugin will move files from `np-app` to `static` folder of spring executable jar
```xml
<plugin>
    <artifactId>maven-resources-plugin</artifactId>
    <executions>
        <execution>
            <id>copy-resources</id>
            <phase>validate</phase>
            <goals><goal>copy-resources</goal></goals>
            <configuration>
                <outputDirectory>${project.build.directory}/classes/static/</outputDirectory >
                <resources>
                    <resource>
                        <directory>${project.basedir}/tutorial-web/src/main/web/dist/np-app/</directory >
                    </resource>
                </resources>
            </configuration>
        </execution>
    </executions>
</plugin>
```

mvn spring-boot:run

---
# # Run as Jar

1. (optional) build `tutorial-web`
2. `mvn clean package`
3. `cd target` and `java -jar <*.jar>`

# # Run Dev

Run backend:
`mvn spring-boot:run` or run App.class

Run frontend:
**use proxy to backend port**
1. `npm run build` or `ng build --prod --build-optimizer=false`
2. `npm start` or `ng serve --proxy-config proxy.conf.json`

---
# # Spring Boot with WebSockets

[View tutorial on Medium](https://medium.com/oril/spring-boot-websockets-angular-5-f2f4b1c14cee)

---
# # Spring

TODO:
1. use `RxJava`
2. use Swagger [as for the project](https://github.com/ManWithShotgun/spring-boot-dynamodb)

---
# # Angular

TODO:
1. use `RxJS`
2. use `async await`

Issues:
0. npm install -g @angular/cli

1. Cannot find module '@angular-devkit/core'
solve [link](https://stackoverflow.com/questions/48394003/cannot-find-module-angular-devkit-core/48394014#48394014)

2. sockjs_client_1.default is not a constructor
solve [link](https://github.com/angular/angular-cli/issues/9243)

3. `ng build` failure
```shell
ERROR in ./node_modules/stompjs/lib/stomp-node.js
Module not found: Error: Can't resolve 'net' in 'D:\MyPrograms\Java\_spring\angular-spring-websocket\tutorial-web\src\main\web\node_modules\stompjs\lib'
``` 
Solution: `npm i net -S`

4. 
```console
Uncaught ReferenceError: global is not defined
    at Object../node_modules/sockjs-client/lib/utils/browser-crypto.js (browser-crypto.js:3)
```
Solution: [link](https://github.com/sockjs/sockjs-client/issues/439#issuecomment-398032809)

As header for `index.html`
```html
<script>
    var global = window;
</script>
```

5. ERROR in node_modules/rxjs/internal/types.d.ts(81,44): error TS1005: ';' expected.

Resolution: [source](https://stackoverflow.com/a/54529119)
5.1 Go to `package.json` and modify `"rxjs": "^6.0.0"` to `"rxjs": "6.0.0"`
5.2 `npm update`
