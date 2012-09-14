/**
 * 
 */
package com.lyncode.oai.proxy.job;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.lyncode.oai.proxy.core.RepositoryManager;
import com.lyncode.oai.proxy.harvest.ProxyHarvester;
import com.lyncode.oai.proxy.util.DateUtils;
import com.lyncode.oai.proxy.xml.repository.Repository;
import com.lyncode.xoai.dataprovider.exceptions.MarshallingException;

/**
 * @author Lyncode
 *
 */
public class HarvestJob implements Job {
	private static Boolean isRunning = new Boolean(false);
	
	private static boolean isRunning () {
		synchronized (isRunning) {
			return isRunning.booleanValue();
		}
	}
	
	private static void setRunning (boolean b) {
		synchronized (isRunning) {
			isRunning = new Boolean(b);
		}
	}
	
	private static Logger log = LogManager.getLogger(HarvestJob.class);
	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		log.info("Aggregation started");
		if (!isRunning()) {
			setRunning(true);
			for (Repository r : RepositoryManager.getRepositories()) {
				if (r.isActive()) {
					ProxyHarvester h = new ProxyHarvester(r);
					h.harvest();
					r.setLastHarvest(DateUtils.formatToSolr(new Date()));
					try {
						RepositoryManager.save(r);
					} catch (MarshallingException e) {
						log.error(e.getMessage(), e);
					} catch (IOException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
			setRunning(false);
		} else log.info("Another aggregation is running");
		log.info("Aggregation ended");
	}

}
