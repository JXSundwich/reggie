package com.sundwich.reggie;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

/**
 * 用Jedis操作Redis
 *
 * @author JX Sun
 * @date 2023.08.03 17:52
 */
@Slf4j
public class JedisTest {
    @Test
    public void testRedis() {
        //1 获取连接
        Jedis jedis=new Jedis("101.200.90.228",6379);
        //2 执行操作
        jedis.set("username","xiaoming");
        String username=jedis.get("username");
        jedis.del("username");
        log.info(username);
        jedis.hset("myhash","key","myhashvalue");
        //3 关闭连接
        jedis.close();
    }
}

