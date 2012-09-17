package com.lyncode.oai.proxy.harvest;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.lyncode.oai.proxy.core.ConfigurationManager;
import com.lyncode.oai.proxy.core.SolrServerManager;
import com.lyncode.oai.proxy.data.ProxyItem;
import com.lyncode.oai.proxy.util.DateUtils;
import com.lyncode.oai.proxy.util.XMLBindUtils;
import com.lyncode.oai.proxy.xml.oaidc.ElementType;
import com.lyncode.oai.proxy.xml.oaidc.OaiDcType;
import com.lyncode.oai.proxy.xml.repository.Repository;
import com.lyncode.xoai.dataprovider.exceptions.MarshallingException;
import com.lyncode.xoai.serviceprovider.HarvesterManager;
import com.lyncode.xoai.serviceprovider.configuration.Configuration;
import com.lyncode.xoai.serviceprovider.data.Record;
import com.lyncode.xoai.serviceprovider.exceptions.BadResumptionTokenException;
import com.lyncode.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.exceptions.NoRecordsMatchException;
import com.lyncode.xoai.serviceprovider.exceptions.NoSetHierarchyException;
import com.lyncode.xoai.serviceprovider.iterators.RecordIterator;
import com.lyncode.xoai.serviceprovider.verbs.ListRecords;

public class ProxyHarvester {
	private static Logger log = LogManager.getLogger(ProxyHarvester.class);
	private static final String METADATA_PREFIX = "oai_dc";
	private static final String DELETED_TOKEN = "deleted";
	private Repository repository;

	public ProxyHarvester(Repository repository) {
		super();
		this.repository = repository;
	}
	
	public void harvest () throws CannotDisseminateFormatException, NoSetHierarchyException, InternalHarvestException {
		Configuration config = new Configuration();
		config.setResumptionInterval(ConfigurationManager.getConfiguration().getInt("oai.proxy.interval"));
		HarvesterManager manager = new HarvesterManager(config, repository.getURL());
		ListRecords b = new ListRecords(null, null, null);
		ListRecords.ExtraParameters extra = b.new ExtraParameters();
		if (repository.getSet() != null)
			extra.setSet(repository.getSet());
		
		Date lastHarvest;
		try {
			lastHarvest = DateUtils.parseDate(repository.getLastHarvest());
			extra.setFrom(lastHarvest);
		} catch (ParseException e1) {
			log.debug(e1.getMessage(), e1);
		} 
		
		
		RecordIterator iterator = manager.listRecords(METADATA_PREFIX, extra).iterator();
		try {
			while (iterator.hasNext()) {
				Record r = iterator.next();
				String query = ProxyItem.IDENTIFIER_FIELD+ ":" + ClientUtils.escapeQueryChars(r.getHeader().getIdentifier());
				SolrQuery q = new SolrQuery(query);
				try {
					SolrDocumentList results = SolrServerManager.getServer().query(q).getResults();
					boolean add = true;
					if (!results.isEmpty()) {
						Date solrModified = (Date) results.get(0).getFieldValue(ProxyItem.DATE_FIELD);
						Date parseModified = DateUtils.parseDate(r.getHeader().getDatestamp());
						if (parseModified.after(solrModified)) {
							// Delete the record
							SolrServerManager.getServer().deleteByQuery(query);
						} else add = false;
					}
					
					if (add) {
						SolrInputDocument doc = new SolrInputDocument();
						doc.addField(ProxyItem.IDENTIFIER_FIELD, r.getHeader().getIdentifier());
						doc.addField(ProxyItem.DATE_FIELD, DateUtils.parseDate(r.getHeader().getDatestamp()));
						if (r.getHeader().getStatus() != null)
							doc.addField(ProxyItem.DELETED_FIELD, r.getHeader().getStatus().toLowerCase().contains(DELETED_TOKEN));
						else
							doc.addField(ProxyItem.DELETED_FIELD, false);
						doc.addField(ProxyItem.REPOSITORY_ID, repository.getID());
						
						if (r.getMetadata() != null) {
							JAXBElement<OaiDcType> metadata = (JAXBElement<OaiDcType>) XMLBindUtils.unmarshal(OaiDcType.class, r.getMetadata().getMetadata());
							
							for (JAXBElement<ElementType> e : metadata.getValue().getTitleOrCreatorOrSubject()) {
								doc.addField(ProxyItem.METADATA_START + e.getName().getLocalPart().toLowerCase(), e.getValue().getValue());
							}
						}
						
						SolrServerManager.getServer().add(doc);
						SolrServerManager.getServer().commit();
					}
				} catch (SolrServerException e) {
					log.debug(e.getMessage(), e);
				} catch (IOException e) {
					log.debug(e.getMessage(), e);
				} catch (MarshallingException e) {
					log.debug(e.getMessage(), e);
				} catch (ParseException e) {
					log.debug(e.getMessage(), e);
				}
				
			}
		} catch (NoRecordsMatchException e) {
			log.debug(e.getMessage(), e);
		} catch (BadResumptionTokenException e) {
			log.debug(e.getMessage(), e);
		}
	}
}
