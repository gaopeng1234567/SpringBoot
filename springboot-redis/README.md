### redisson实现流程

1、线程一加锁lock，如果获取到第二步，否则进入4

2、开启一个守护线程timer定时器，每隔10秒判断是否还持有锁，有则给锁过期时间续命

3、释放锁unlock，结束。

4、while循环,一直尝试枷锁，成功进入第二步

### redisson实现原理

* lua脚本

```
 if (redis.call('exists', KEYS[1]) == 0)    //第一个上锁的操作
     then redis.call('hset', KEYS[1], ARGV[2], 1);  //hash结构存储 
     redis.call('pexpire', KEYS[1], ARGV[1]); 
     return nil;
 end; 
if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) //支持当前线线程重入锁
    then redis.call('hincrby', KEYS[1], ARGV[2], 1);  //当前的key的value上+1
    redis.call('pexpire', KEYS[1], ARGV[1]); 
    return nil;
end; 
return redis.call('pttl', KEYS[1]); //如果其他线程访问，返回当前锁的剩余过期时间
    
```

**Redis客户端命令对应的RedisTemplate中的方法列表：**

| **String类型结构**                                         |                                                             |
| ---------------------------------------------------------- | ----------------------------------------------------------- |
| Redis                                                      | RedisTemplate rt                                            |
| set key value                                              | rt.opsForValue().set("key","value")                         |
| get key                                                    | rt.opsForValue().get("key")                                 |
| del key                                                    | rt.delete("key")                                            |
| strlen key                                                 | rt.opsForValue().size("key")                                |
| getset key value                                           | rt.opsForValue().getAndSet("key","value")                   |
| getrange key start end                                     | rt.opsForValue().get("key",start,end)                       |
| append key value                                           | rt.opsForValue().append("key","value")                      |
|                                                            |                                                             |
| **
Hash结构**                                               |                                                             |
| hmset key field1 value1 field2 value2...                   | rt.opsForHash().putAll("key",map) //map是一个集合对象       |
| hset key field value                                       | rt.opsForHash().put("key","field","value")                  |
| hexists key field                                          | rt.opsForHash().hasKey("key","field")                       |
| hgetall key                                                | rt.opsForHash().entries("key") //返回Map对象                |
| hvals key                                                  | rt.opsForHash().values("key") //返回List对象                |
| hkeys key                                                  | rt.opsForHash().keys("key") //返回List对象                  |
| hmget key field1 field2...                                 | rt.opsForHash().multiGet("key",keyList)                     |
| hsetnx key field value                                     | rt.opsForHash().putIfAbsent("key","field","value"           |
| hdel key field1 field2                                     | rt.opsForHash().delete("key","field1","field2")             |
| hget key field                                             | rt.opsForHash().get("key","field")                          |
|                                                            |                                                             |
| **
List结构**                                               |                                                             |
| lpush list node1 node2 node3...                            | rt.opsForList().leftPush("list","node")                     |
| rt.opsForList().leftPushAll("list",list) //list是集合对象  |                                                             |
| rpush list node1 node2 node3...                            | rt.opsForList().rightPush("list","node")                    |
| rt.opsForList().rightPushAll("list",list) //list是集合对象 |                                                             |
| lindex key index                                           | rt.opsForList().index("list", index)                        |
| llen key                                                   | rt.opsForList().size("key")                                 |
| lpop key                                                   | rt.opsForList().leftPop("key")                              |
| rpop key                                                   | rt.opsForList().rightPop("key")                             |
| lpushx list node                                           | rt.opsForList().leftPushIfPresent("list","node")            |
| rpushx list node                                           | rt.opsForList().rightPushIfPresent("list","node")           |
| lrange list start end                                      | rt.opsForList().range("list",start,end)                     |
| lrem list count value                                      | rt.opsForList().remove("list",count,"value")                |
| lset key index value                                       | rt.opsForList().set("list",index,"value")                   |
|                                                            |                                                             |
| **
Set结构**                                                |                                                             |
| sadd key member1 member2...                                | rt.boundSetOps("key").add("member1","member2",...)          |
| rt.opsForSet().add("key", set) //set是一个集合对象         |                                                             |
| scard key                                                  | rt.opsForSet().size("key")                                  |
| sidff key1 key2                                            | rt.opsForSet().difference("key1","key2") //返回一个集合对象 |
| sinter key1 key2                                           | rt.opsForSet().intersect("key1","key2")//同上               |
| sunion key1 key2                                           | rt.opsForSet().union("key1","key2")//同上                   |
| sdiffstore des key1 key2                                   | rt.opsForSet().differenceAndStore("key1","key2","des")      |
| sinter des key1 key2                                       | rt.opsForSet().intersectAndStore("key1","key2","des")       |
| sunionstore des key1 key2                                  | rt.opsForSet().unionAndStore("key1","key2","des")           |
| sismember key member                                       | rt.opsForSet().isMember("key","member")                     |
| smembers key                                               | rt.opsForSet().members("key")                               |
| spop key                                                   | rt.opsForSet().pop("key")                                   |
| srandmember key count                                      | rt.opsForSet().randomMember("key",count)                    |
| srem key member1 member2...                                | rt.opsForSet().remove("key","member1","member2",...)        |