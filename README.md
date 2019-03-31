# Issues

# maven compiler plugin invalid target release: 11
Resolution: JAVA_HOME java should be the same as in version of java in project

# /bin/bash^M: bad interpreter: No such file or directory

[source](https://stackoverflow.com/a/14219160)

In Notepad++ open `.sh` and in the bottom right of the screen select `Unix (LF)` then save file

# Spring Autowired throws NPE

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
doccker-compose up
```

Stop and remove containers
```shell
doccker-compose down
```

Stop containers
```shell
doccker-compose stop 
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

---
# # DynamoDB on localhost (Docker)

[example](https://dzone.com/articles/getting-started-with-dynamodb-and-spring)
[git](https://github.com/smartinrub/spring-boot-dynamodb)

## ## Install
[source](https://medium.com/devopslinks/dynamodb-on-localhost-9c502f07056e)

## ## Config
[source](https://github.com/ruanbekker/dynamodb-local-docker/blob/master/README.md)

```shell
pip install awscli
```

```shell
aws configure
```

## ## Docker

```shell
docker pull amazon/dynamodb-local
```

```shell
docker run -p 8000:8000 amazon/dynamodb-local
```

```shell
docker run -it --rm -v d:/Root/MyPrograms/Java/zprime-service/target:/home/dynamodblocal/data -p 8000:8000 amazon/dynamodb-local -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -dbPath ./data
```

## ## Get all items from table

```shell
aws dynamodb scan --table-name Hotels --endpoint-url http://localhost:8000
```

## ## Issue: credentials were not defined
Issue:
Unable to create table: Unable to load AWS credentials from any provider in the chain: 
[EnvironmentVariableCredentialsProvider: 
Unable to load AWS credentials from environment variables (AWS_ACCESS_KEY_ID (or AWS_ACCESS_KEY) and AWS_SECRET_KEY (or AWS_SECRET_ACCESS_KEY)), 
SystemPropertiesCredentialsProvider: Unable to load AWS credentials from Java system properties (aws.accessKeyId and aws.secretKey), 
com.amazonaws.auth.profile.ProfileCredentialsProvider@21457192: profile file cannot be null, com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper@be67096: Unable to load credentials from service endpoint]

Resolution:
Should be defined by `aws-cli` (see Config section here)

# # Redis
[example](https://www.baeldung.com/spring-data-redis-tutorial)

```shell
docker pull redis
```

```shell
docker run -p 6379:6379 -d redis
```
