package com.mr.newsenserss;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

import com.mr.newsenserss.dao.ItemDao;
import com.mr.newsenserss.dao.SourceDao;

@SpringBootApplication
public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
	SpringApplication.run(App.class);
    }

    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory() {
        return new HibernateJpaSessionFactoryBean();
    }
    
    @Bean
    public CommandLineRunner demo(ItemDao itemDao, SourceDao linkDao) {
	return (args) -> {
	    RssReader rr = new RssReader();
	    while (true) {
		List<Source> links = (List<Source>) linkDao.findAll();
		for (Source link : links) {
		    Item checkItem = itemDao.getLastAdedByHostUrl(link.getHost());
		    List<Item> items = rr.read(link.getUrl());
		    for (Item item : items) {
			if (checkItem == null) {
			    itemDao.save(item);
			    log.info("Item was saved!");
			} else {
			    if (!item.equals(checkItem)) {
				if (!itemDao.isExist(item)){
				    itemDao.save(item);
				    log.info("Item was saved!");
				}
			    } else {
				break;
			    }
			}
		    }
		    Thread.sleep(1200000/links.size());
		}
	    }
	};
    }
}