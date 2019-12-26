package com.godboot.app;

import com.godboot.app.cache.Cache;
import com.godboot.app.model.dto.AccountDTO;
import com.godboot.app.service.ICacheService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
class DemoApplicationTests {

    @Autowired
    private ICacheService cacheService;

    private Cache cache = new Cache() {
        @Override
        public String prefix() {
            return "cache-01";
        }
    };

    @Test
    void contextLoads() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainApplication.class);
        log.error("context: {}", applicationContext);
    }

    @Test
    void demoTest() {
        AccountDTO accountDTO = new AccountDTO(1, "4878", "123456", 1, 0);
        Boolean key101 = cacheService.cacheValue(cache, "key101", accountDTO);
        log.error("{}", key101);
    }

    @Test
    void getValue() {
        AccountDTO key101 = cacheService.getCacheValue(cache,"key101", AccountDTO.class);
        log.error("{}", key101);
    }

    @Test
    void cacheList() {
        ArrayList<AccountDTO> value = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            AccountDTO accountDTO = new AccountDTO(i, "4878", "123456", 1, 0);
            value.add(accountDTO);
        }

        Cache cache = new Cache() {
            @Override
            public String prefix() {
                return "cache-02";
            }
        };

        cacheService.cacheList(cache, "1001", value);
        List<AccountDTO> cacheList = cacheService.getCacheList(cache, "1001", AccountDTO.class);
        log.error("{}", cacheList);
    }

    @Test
    void cacheHash() {
        HashMap<Object, Object> objectHashMap = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            AccountDTO accountDTO = new AccountDTO(i, "4878", "123456", 1, 0);
            objectHashMap.put(accountDTO.getUserId(), accountDTO);
        }

        Cache cache = new Cache() {
            @Override
            public String prefix() {
                return "cache-03";
            }
        };
        cacheService.cacheHash(cache, "1001", objectHashMap);

        Map<Object, AccountDTO> cacheHash = cacheService.getCacheHash(cache, "1001", AccountDTO.class);
        log.error("{}", cacheHash);
    }
}
