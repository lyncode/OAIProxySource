package com.lyncode.oai.proxy.core;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.lyncode.oai.proxy.util.XMLBindUtils;
import com.lyncode.oai.proxy.xml.repository.Repository;
import com.lyncode.xoai.dataprovider.exceptions.MarshallingException;

public class RepositoryManager {
	private static Logger log = LogManager.getLogger(RepositoryManager.class);
	private static Map<Repository, String> map = null;
	private static Map<String, Repository> mapIDS = null;
	
	private static void initialize () {
		if (map == null) {
			map = new TreeMap<Repository, String>();
			mapIDS = new TreeMap<String, Repository>();
			
			Configuration config = ConfigurationManager.getConfiguration();
			File dir = new File(config.getString("repositories.dir"));
			
			File[] files = dir.listFiles();

			// This filter only returns directories
			FileFilter fileFilter = new FileFilter() {
			    public boolean accept(File file) {
			        return file.getAbsolutePath().endsWith(".xml");
			    }
			};
			
			files = dir.listFiles(fileFilter);
			
			for (File f : files) {
				try {
					Repository r = (Repository) XMLBindUtils.unmarshal(Repository.class, new FileInputStream(f));
					map.put(r, f.getAbsolutePath());
					mapIDS.put(r.getID(), r);
				} catch (FileNotFoundException e) {
					log.debug(e.getMessage(), e);
				} catch (MarshallingException e) {
					log.debug(e.getMessage(), e);
				}
				
			}
		}
	}
	
	public static List<Repository> getRepositories () {
		initialize();
		return new ArrayList<Repository>(map.keySet());
	}
	
	public static void create (Repository r) throws IOException, MarshallingException {
		initialize();
		
		Configuration config = ConfigurationManager.getConfiguration();
		File dir = new File(config.getString("repositories.dir"));
		
		File file = new File(dir, r.getID() + ".xml");
		FileOutputStream out = new FileOutputStream(file);
		XMLBindUtils.marshal(r, out);
		out.close();
		
		map.put(r, file.getAbsolutePath());
		mapIDS.put(r.getID(), r);
	}
	
	public static void save (Repository r) throws MarshallingException, IOException {
		String file = map.get(r);
		FileOutputStream out = new FileOutputStream(file);
		XMLBindUtils.marshal(r, out);
		out.close();
	}
	
	public static void delete (Repository r) {
		File f = new File(map.get(r));
		f.delete();
		map.remove(r);
		mapIDS.remove(r.getID());
	}
	
	public static long harvestItems (Repository r) {
		return 0; // TODO: Implementar
	}
	
	public static Repository getByID (String id) {
		initialize();
		return mapIDS.get(id);
	}
}
