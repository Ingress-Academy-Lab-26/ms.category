package az.ingress.mscategory.service.concrete;

import az.ingress.mscategory.dao.entity.CategoryEntity;
import az.ingress.mscategory.util.cache.CacheUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static az.ingress.mscategory.util.cache.CacheConstraints.CACHE_EXPIRATION_COUNT;
import static az.ingress.mscategory.util.cache.CacheConstraints.CACHE_EXPIRATION_UNIT;
import static az.ingress.mscategory.util.cache.CacheConstraints.CACHE_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
@Async
public class CacheService {
    private final CacheUtil cacheUtil;

    @CircuitBreaker(name = "redisCacheBreaker", fallbackMethod = "fallbackSaveToCache")
    public CompletableFuture<Void> saveCategoriesToCache(List<CategoryEntity> categoryEntityList) {
        cacheUtil.saveToCache(CACHE_KEY, categoryEntityList, CACHE_EXPIRATION_COUNT, CACHE_EXPIRATION_UNIT);
        log.info("Categories saved to cache with key: {}", CACHE_KEY);
        return CompletableFuture.completedFuture(null);
    }

    @CircuitBreaker(name = "redisCacheBreaker", fallbackMethod = "fallbackGetFromCache")
    public List<CategoryEntity> getCategoriesFromCache() {
        return cacheUtil.getBucket(CACHE_KEY);
    }

    public void fallbackSaveToCache(Iterable<CategoryEntity> categoryEntityList, Throwable throwable) {
        log.error("Failed to save to cache due to Redis outage. Circuit breaker triggered.", throwable);
    }

    public CompletableFuture<List<CategoryEntity>> fallbackGetFromCache(Throwable throwable) {
        log.error("Failed to get from cache due to Redis outage. Circuit breaker triggered.", throwable);
        return CompletableFuture.completedFuture(null);
    }
}
