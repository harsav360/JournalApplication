package com.harsav360.journal.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void testRedisWork() {
        redisTemplate.opsForValue().set("email", "abd@email.com");
        Object email = redisTemplate.opsForValue().get("email");
        System.out.println(email);
    }

}
