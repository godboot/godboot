package com.godboot.app.cache;

import com.godboot.foundation.cache.Cache;

public class AccountCache extends Cache {
    @Override
    public String prefix() {
        return "account-cache";
    }
}
