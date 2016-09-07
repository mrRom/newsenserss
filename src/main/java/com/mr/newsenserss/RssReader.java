package com.mr.newsenserss;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class RssReader {
    private static final Logger log = LoggerFactory.getLogger(RssReader.class);

    public List<Item> read(String link) {
	List<Item> list = new ArrayList<Item>();
	try {
	    URL url = new URL(link);
	    HttpURLConnection httpcon = (HttpURLConnection) url
		    .openConnection();
	    log.info("Connecting to: "+ link);
	    // Reading the feed
	    SyndFeedInput input = new SyndFeedInput();
	    SyndFeed feed = input.build(new XmlReader(httpcon));
	    log.info("Feed type: " + feed.getFeedType());
	    List<?> entries = feed.getEntries();
	    log.info("Feed has "+ entries.size() + " entries.");
	    Iterator<?> itEntries = entries.iterator();
	    while (itEntries.hasNext()) {
		SyndEntry entry = (SyndEntry) itEntries.next();
		Date date;
		if (entry.getPublishedDate() == null) {
		    date = new Date();
		} else {
		    date = entry.getPublishedDate();
		}
		Item i = new Item(entry.getTitle(), entry.getLink(),
			entry.getAuthor(), entry.getDescription().getValue(),
			date);
		list.add(i);
	    }
	} catch (IllegalArgumentException | FeedException | IOException e) {
	    log.warn("Couldn't read RSSFeed from "+ link + ":" + e);
	    return new ArrayList<Item>();
	}
	return list;
    }
}
