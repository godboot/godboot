package com.godboot.app.cache;

public interface Cache {
    default Integer expireTime() {
        return 60 * 60;
    }

    String prefix();
}
