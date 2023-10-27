package com.side.community;

import com.side.community.service.RedisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisService redisService;


    @Test
    public void redisTest() throws Exception {
        String key = "key";
        String value = "value";

        redisService.setDataExpire(key, value, 60 * 60L);

        Assertions.assertTrue(redisService.existData("key"));
        Assertions.assertEquals(redisService.getData(key), "value");

        redisService.deleteData(key);
    }

}
