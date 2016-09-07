package com.mr.newsenserss.dao;

import com.mr.newsenserss.Item;

public interface ItemDao {
    void save(Item item);
    Item getLastAdedByHostUrl(String host);
    boolean isExist(Item item);
}
