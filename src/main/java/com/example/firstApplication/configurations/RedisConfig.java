package com.example.firstApplication.configurations;
    import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.data.redis.cache.RedisCacheConfiguration;
	import org.springframework.data.redis.connection.RedisConnectionFactory;
	import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
	import org.springframework.data.redis.serializer.RedisSerializationContext;
	import org.springframework.data.redis.connection.RedisConnectionFactory;
	import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
	import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
	import org.springframework.data.redis.core.RedisTemplate;

	@Configuration
	public class RedisConfig {
		@Bean
	    public RedisConnectionFactory redisConnectionFactory() {
			try {
	            // Primary Redis Configuration
	            RedisStandaloneConfiguration primaryConfig = new RedisStandaloneConfiguration();
	            primaryConfig.setHostName("primary-redis-host");
	            primaryConfig.setPort(6379);

	            LettuceConnectionFactory primaryConnectionFactory = new LettuceConnectionFactory(primaryConfig);
	            primaryConnectionFactory.afterPropertiesSet(); // Validate connection
	            System.out.println("Connected to Primary Redis");
	            return primaryConnectionFactory;

	        } catch (Exception primaryException) {
	            System.err.println("Primary Redis connection failed: " + primaryException.getMessage());
	            System.err.println("Falling back to Secondary Redis...");

	            // Secondary Redis Configuration (Fallback)
	            RedisStandaloneConfiguration secondaryConfig = new RedisStandaloneConfiguration();
	            secondaryConfig.setHostName("secondary-redis-host");
	            secondaryConfig.setPort(6379);

	            LettuceConnectionFactory secondaryConnectionFactory = new LettuceConnectionFactory(secondaryConfig);
	            try {
	                secondaryConnectionFactory.afterPropertiesSet(); // Validate connection
	                System.out.println("Connected to Secondary Redis");
	                return secondaryConnectionFactory;
	            } catch (Exception secondaryException) {
	                throw new RuntimeException("Both Primary and Secondary Redis instances failed: " + secondaryException.getMessage());
	            }
	        }
	    }

		@Bean
	    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
	        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
	        redisTemplate.setConnectionFactory(redisConnectionFactory);
	        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
	        redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());
	        return redisTemplate;
	    }

		
		@Bean
	    public RedisCacheConfiguration cacheConfiguration() {
	        return RedisCacheConfiguration.defaultCacheConfig()
	            .serializeValuesWith(
	                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	    }
	}



