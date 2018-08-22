package cn.springcache.redis.util.redis;

import cn.springcache.redis.util.serializer.FastJson2JsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * spring data redis 2.0及以上版本
 */
@Configuration
public class RedisManagerConfig {

    private Duration timeToLive = Duration.ZERO;
    public void setTimeToLive(Duration timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Bean(name="defaultRedisManager")
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

    @Bean(name="redisCacheManager")
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
                                          RedisSerializer fastJson2JsonRedisSerializer) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(this.timeToLive)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJson2JsonRedisSerializer))
                .disableCachingNullValues();

        Set<String> cacheNames = new HashSet<String>() {{
            add("batch");
            add("user");
        }};

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                //.initialCacheNames(cacheNames)
                .build();

        return redisCacheManager;
    }

	@Bean
	public RedisSerializer fastJson2JsonRedisSerializer() {
		return new FastJson2JsonRedisSerializer(Object.class);
	}



	@Bean
	public RedisSerializer StringRedisSerializer() {
		return new StringRedisSerializer();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisSerializer fastJson2JsonRedisSerializer,
                                                       RedisSerializer StringRedisSerializer,
                                                       JedisConnectionFactory jedisConnectionFactory) {

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);

		// 全局开启AutoType，不建议使用
		// ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
		// 建议使用这种方式，小范围指定白名单
		//ParserConfig.getGlobalInstance().addAccept("com.xiaolyuh.");

		// 设置值（value）的序列化采用FastJsonRedisSerializer。
		redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);
		redisTemplate.setHashValueSerializer(fastJson2JsonRedisSerializer());

		// 设置键（key）的序列化采用StringRedisSerializer。
		redisTemplate.setKeySerializer(StringRedisSerializer);
		redisTemplate.setHashKeySerializer(StringRedisSerializer);
		redisTemplate.afterPropertiesSet();

		return redisTemplate;
	}


    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> valueSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

}
