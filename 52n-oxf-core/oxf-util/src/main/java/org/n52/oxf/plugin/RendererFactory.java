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
import org.n52.oxf.render.IRenderer;
import org.n52.oxf.serviceAdapters.IServiceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible to dynamically load the available IRenderers
 * and to ensure that exactly one IRenderer of one type is instantiated.
 * The available IRenderers are listed in an extern propertyFile. The
 * property file is located in org.n52.oxf.render. <br/>
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 *
 */
public class RendererFactory {

    public enum ClassLoaderName{
        URL_CLASSLOADER,
        STANDARD_CLASSLOADER;
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RendererFactory.class);
    
    /**
     * this flag decides which ClassLoader will be used to load the classes.
     */
    private static ClassLoaderName CLASSLOADER_TO_USE = ClassLoaderName.URL_CLASSLOADER;
    
	/**
	 * the Map of available ServiceAdapters.<br>
	 * key: class-name of the ServiceAdapter<br>
	 * value: instance of the ServiceAdapter
	 */
	private static Map<String, IRenderer> rendererMap;

    
	/**
	 * looks for the availabe IRenderer, which are listed in a seperated
	 * config file.
	 * 
	 * @return a List of class names.
	 */
	public static ArrayList<String> getAvailableRendererClassNames()
			throws OXFException {
		if (rendererMap == null) {
			initRendererMap();
		}
		ArrayList<String> rendererNames = new ArrayList<String>();
		for (String s : rendererMap.keySet()) {
			rendererNames.add(s);
		}
		return rendererNames;
	}

	/**
	 * @param className
	 * @return the specific IRenderer-instance.
	 */
	public static IRenderer getRenderer(String className)
			throws OXFException {
		if (rendererMap == null) {
			initRendererMap();
		}
		return rendererMap.get(className);
	}

	/**
	 * @return a Collection of all available instances of IRenderers.
	 * @throws OXFException
	 */
	public static Collection<IRenderer> getAvailableRenderers()
			throws OXFException {
		if (rendererMap == null) {
			initRendererMap();
		}
		return rendererMap.values();
	}

	/**
	 * initializes the list of available IRenderers by utilizing the
	 * renderers.properties file. The propertyfile has the pattern:
	 * className=location;
	 */
	private static void initRendererMap() throws OXFException {
		rendererMap = new HashMap<String, IRenderer>();
		Properties properties = new Properties();
		try {
            properties.load(IServiceAdapter.class.getResourceAsStream("/renderers.properties"));
            
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
				
                IRenderer rendererInstance = null;
				try {
					Class<IRenderer> rendererClass = (Class<IRenderer>) loader.loadClass(className);
					rendererInstance = rendererClass.newInstance();
				} catch (ClassNotFoundException e) {
					throw new OXFException(className + " not found", e);
				} catch (InstantiationException e) {
					throw new OXFException(e);
				} catch (IllegalAccessException e) {
					throw new OXFException(e);
				}
				LOGGER.debug(className + " added to " + RendererFactory.class.getSimpleName() + ".");
				rendererMap.put(className, rendererInstance);
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
			Collection<IRenderer> availableRenderer = RendererFactory.getAvailableRenderers();
			
			IRenderer wcsRenderer = null;
			for (IRenderer renderer : availableRenderer) {
				
				if(renderer instanceof IRenderer){
					wcsRenderer = (IRenderer)renderer;
				}
			}
			
			LOGGER.info(wcsRenderer.getServiceType());
		} catch (OXFException e) {
			e.printStackTrace();
		}
	}
}