package com.godboot.foundation.cache;

import java.io.Serializable;

public class Cache implements Serializable {
    public Integer expireTime() {
        return 60 * 60;
    }

    public String prefix() {
        return "";
    }
}
