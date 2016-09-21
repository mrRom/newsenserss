package com.mr.newsenserss.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mr.newsenserss.Item;
import com.mr.newsenserss.dao.ItemDao;

@Repository("itemDao")
@Transactional
public class ItemDaoImpl implements ItemDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Item item) {
	sessionFactory.getCurrentSession().save(item);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Item getLastAdedByHostUrl(String host) {
	String param = "%" + host + "%";
	List<Item> result = (List<Item>) sessionFactory
		.getCurrentSession()
		.createQuery(
			"from Item where url like :url order by publishDate desc")
		.setParameter("url", param).setMaxResults(1).list();
	if (result.size() != 1) {
	    return null;
	}
	return result.get(0);
    }

    @Override
    public boolean isExist(Item item) {
	return (sessionFactory.getCurrentSession()
		.createQuery("select 1 from Item where url = :url")
		.setString("url", item.getUrl()).uniqueResult() != null);
    }
}
