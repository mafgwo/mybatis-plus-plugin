package com.econ.platformcloud.plugins.mybatisplus.util;

import java.util.Collection;

/**
 * @author yanglin
 */
public final class CollectionUtils {

    private CollectionUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return null == collection || 0 == collection.size();
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

}
