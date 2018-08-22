package cn.springcache.redis.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DefaultRedisManager {


    @Bean(name="redisCacheManager")
    public RedisCacheManager createCacheManager(JedisConnectionFactory jedisConnectionFactory) {
        //return RedisCacheManager.create(connectionFactory); //默认管理器
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
                .RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory);
        Set<String> cacheNames = new HashSet<String>() {{
            add("batch");
        }};

        builder.initialCacheNames(cacheNames);

        return builder.build(); //设置多个缓存;
    }
}
