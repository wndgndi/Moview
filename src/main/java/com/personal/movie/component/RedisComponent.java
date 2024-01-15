package com.personal.movie.component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisComponent {

    private final StringRedisTemplate redisTemplate;

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, String value) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, duration, TimeUnit.SECONDS);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public void setDataSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public void setDataSetExpire(String key, String value) {
        Duration duration = calculateTimeUnitMidnight();
        redisTemplate.opsForSet().add(key, value);
        redisTemplate.expire(key, duration);
    }

    public Set<String> getDataSet(String key) {
        Long length = redisTemplate.opsForSet().size(key);
        return length == 0 ? new HashSet<>()
            : redisTemplate.opsForSet().members(key);
    }

    public boolean isExistData(String key, String value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    private Duration calculateTimeUnitMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT)
            .plusDays(1);
        return Duration.between(now, midnight);
    }
}
