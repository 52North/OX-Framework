/**
 * ﻿Copyright (C) 2012
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.oxf.plugin;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.IServiceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible to dynamically load the available IServiceAdapters
 * and to ensure that exactly one IServiceAdapter of one type is instantiated.
 * The available IServiceAdapters are listed in an extern propertyFile. The
 * property file is located in org.n52.oxf.adapter. <br/>
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 */
public class ServiceAdapterFactory {

    public enum ClassLoaderName{
        URL_CLASSLOADER,
        STANDARD_CLASSLOADER;
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAdapterFactory.class);
    
    /**
     * this flag decides which ClassLoader will be used to load the classes.
     */
    private static ClassLoaderName CLASSLOADER_TO_USE = ClassLoaderName.URL_CLASSLOADER;
    
	/**
	 * the Map of available ServiceAdapters.<br>
	 * key: class-name of the ServiceAdapter<br>
	 * value: instance of the ServiceAdapter
	 */
	private static Map<String, IServiceAdapter> serviceAdapters;

    
	/**
	 * looks for the availabeServiceAdapters, which are listed in a seperated
	 * config file.
     * 
	 * @return a List of class names.
	 */
	public static ArrayList<String> getAvailableServiceAdapterClassNames()
			throws OXFException {
		if (serviceAdapters == null) {
			initServiceAdapters();
		}
		ArrayList<String> adapterNames = new ArrayList<String>();
		for (String s : serviceAdapters.keySet()) {
			adapterNames.add(s);
		}
		return adapterNames;
	}

	/**
	 * @param className
	 * @return the specific Object to connect to a OWS.
	 */
	public static IServiceAdapter getServiceAdapter(String className)
			throws OXFException {
		if (serviceAdapters == null) {
			initServiceAdapters();
		}
		return serviceAdapters.get(className);
	}

	/**
	 * @return a Collection of all available instances of IServiceAdapters.
	 * @throws OXFException
	 */
	public static Collection<IServiceAdapter> getAvailableServiceAdapters()
			throws OXFException {
		if (serviceAdapters == null) {
			initServiceAdapters();
		}
		return serviceAdapters.values();
	}

	/**
	 * initializes the list of available Service Adapters by utilizing the
	 * serviceAdapters.properties file. The propertyfile has the pattern:
	 * className=location;
	 */
	private static void initServiceAdapters() throws OXFException {
        serviceAdapters = new HashMap<String, IServiceAdapter>();
		Properties properties = new Properties();
		try {
            properties.load(ServiceAdapterFactory.class.getResourceAsStream("/serviceAdapters.properties"));
            
			Iterator propertyIterator = properties.entrySet().iterator();
			while (propertyIterator.hasNext()) {
				Map.Entry entry = (Map.Entry) propertyIterator.next();
				String className = (String) entry.getKey();
				String path = (String) entry.getValue();
                
                ClassLoader loader = null;
                if(CLASSLOADER_TO_USE == ClassLoaderName.URL_CLASSLOADER){
				    loader = URLClassLoader.newInstance(new URL[] { new URL(path) });
                }
                else if(CLASSLOADER_TO_USE == ClassLoaderName.STANDARD_CLASSLOADER){
                    loader = ServiceAdapterFactory.class.getClassLoader();
                }
                
                IServiceAdapter adapterInstance = null;
				try {
					Class<IServiceAdapter> adapterClass = (Class<IServiceAdapter>) loader.loadClass(className);
					adapterInstance = adapterClass.newInstance();
				} catch (ClassNotFoundException e) {
					throw new OXFException(className + " not found", e);
				} catch (InstantiationException e) {
					throw new OXFException(e);
				} catch (IllegalAccessException e) {
					throw new OXFException(e);
				}
				LOGGER.debug(className + " added to " + ServiceAdapterFactory.class.getSimpleName() + ".");
				serviceAdapters.put(className, adapterInstance);
			}

		} catch (IOException e) {
			throw new OXFException(e);
		}
	}
    
    public static void setClassLoaderToUse(ClassLoaderName clName){
        CLASSLOADER_TO_USE = clName;
    }

	/**
	 * test...
	 */
	public static void main(String[] args) {
		try {
			Collection<IServiceAdapter> availableServiceAdapter = getAvailableServiceAdapters();
			
			IServiceAdapter wcsAdapter = null;
			for (IServiceAdapter adapter : availableServiceAdapter) {
				if(adapter.getServiceType().equals("OGC:WCS")){
					wcsAdapter = adapter;
				}
			}
			
			LOGGER.info(wcsAdapter.getDescription());
		} catch (OXFException e) {
			e.printStackTrace();
		}
	}
}