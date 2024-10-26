package az.ingress.mscategory.service.abstracts;

import az.ingress.mscategory.dao.entity.CategoryEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CacheService {
    void saveCategoriesToCache();
    List<CategoryEntity> getCategoriesFromCache();
    void fallbackSaveToCache(Throwable throwable);
    List<CategoryEntity> fallbackGetFromCache(Throwable throwable);
}
