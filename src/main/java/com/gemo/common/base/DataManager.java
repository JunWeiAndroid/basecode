package com.gemo.common.base;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Android
 * DataManager: DataModel 数据管理
 */
public class DataManager {

    private final ConcurrentHashMap<Class, IBaseData> DATA_CACHE;

    private DataManager() {
        DATA_CACHE = new ConcurrentHashMap<>(8);
    }

    public static DataManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final DataManager INSTANCE = new DataManager();
    }

    /**
     * Create dataModel
     *
     * @param <D>   the orderStatus parameter
     * @param clazz the clazz
     * @return the d
     */
    @SuppressWarnings("unchecked")
    public <D extends IBaseData> D create(final Class<D> clazz) {
        IBaseData d = DATA_CACHE.get(clazz);
        if (d != null) {
            return (D) d;
        }
        try {
            synchronized (DATA_CACHE) {
                d = DATA_CACHE.get(clazz);
                if (d == null) {
                    Constructor<D> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    d = constructor.newInstance();
                    DATA_CACHE.put(clazz, d);
                }
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return (D) d;
    }

}
