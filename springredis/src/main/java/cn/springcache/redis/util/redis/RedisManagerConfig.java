package cn.springcache.redis.util.redis;

import cn.springcache.redis.util.serializer.FastJson2JsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.HashSet;
import java.util.Set;

//@Configuration
public class RedisManagerConfig {

	@Autowired
	private RedisConnectionFactory connectionFactory;



   /* public JedisConnectionFactory jedisConnectionFactory(RedisStandaloneConfiguration standaloneConfig) {
        return new JedisConnectionFactory(standaloneConfig);
    }
*/
    @Bean(name="redisCacheManager")
	public RedisCacheManager createCacheManager() {
		//return RedisCacheManager.create(connectionFactory); //默认管理器
		RedisCacheManagerBuilder builder = RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory);
		Set<String> cacheNames = new HashSet<String>() {{  
	        add("batch");
	    }};

		builder.initialCacheNames(cacheNames);

		return builder.build(); //设置多个缓存;
	}
	@Bean
	public RedisSerializer fastJson2JsonRedisSerializer() {
		return new FastJson2JsonRedisSerializer(Object.class);
	}

	/*


	@Bean
	public RedisSerializer StringRedisSerializer() {
		return new StringRedisSerializer();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisSerializer fastJson2JsonRedisSerializer,
													   RedisSerializer StringRedisSerializer) {

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);

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
	}*/


}
