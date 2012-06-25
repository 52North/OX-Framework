/**********************************************************************************
 Copyright (C) 2009
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk 
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under the
 terms of the GNU General Public License version 2 as published by the Free
 Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with this 
 program (see gnu-gplv2.txt). If not, write to the Free Software Foundation, Inc., 
 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit the Free Software
 Foundation web page, http://www.fsf.org.
 
 Created on: 11.08.2005
 *********************************************************************************/

package org.n52.oxf.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

/**
 * Some little helper methods for IO-handling.
 * 
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>
 */
public class IOHelper {

    private static Logger LOGGER = LoggingHandler.getLogger(IOHelper.class);

    public static InputStream sendGetMessage(String serviceURL, List<NameValuePair> parameters) throws IOException {

        HttpClient httpClient = getDefaultHttpClient(serviceURL);
        
        GetMethod method = new GetMethod(serviceURL);
        NameValuePair[] paramArray = new NameValuePair[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            paramArray[i] = parameters.get(i);
        }
        method.setQueryString(paramArray);

        httpClient.executeMethod(method);

        LOGGER.debug("GET-method sended to: " + method.getURI());

        return method.getResponseBodyAsStream();
    }
    
	private static HttpClient getDefaultHttpClient(String serviceURL) throws MalformedURLException {
		HttpClient httpClient = new HttpClient();
        httpClient.setHostConfiguration(getHostConfiguration(new URL(serviceURL)));
        return httpClient;
	}

	public static InputStream sendGetMessage(String serviceURL, String queryString) throws IOException {
        HttpClient httpClient = getDefaultHttpClient(serviceURL);
        
        GetMethod method = new GetMethod(serviceURL);
        method.setQueryString(queryString);

        httpClient.executeMethod(method);

        LOGGER.debug("GET-method sended to: " + method.getURI());

        return method.getResponseBodyAsStream();
    }

    /**
     * sends a POST-request using org.apache.commons.httpclient.HttpClient.
     * 
     * @param serviceURL
     * @param request
     * @return
     */
    public static InputStream sendPostMessage(String serviceURL, String request) throws IOException {
        HttpClient httpClient = getDefaultHttpClient(serviceURL);
        
        PostMethod method = new PostMethod(serviceURL.trim());
        method.setRequestEntity(new StringRequestEntity(request, "text/xml", "UTF-8"));

        LOGGER.trace("Service Endpoint: " + method.getURI());
        LOGGER.trace("Request to send: " + request);
      
        httpClient.executeMethod(method);
        return method.getResponseBodyAsStream();
    }
    
    protected static HostConfiguration getHostConfiguration(URL serviceURL) {
        HostConfiguration hostConfig = new HostConfiguration();
        
        
        // apply proxy settings:
        String host = System.getProperty("http.proxyHost");
        if (host != null && host.isEmpty()) {
            return hostConfig;
        }
        String port = System.getProperty("http.proxyPort");
        String nonProxyHosts = System.getProperty("http.nonProxyHosts");
        
        // check if service url is among the non-proxy-hosts:
        boolean serviceIsNonProxyHost = false;
        if (nonProxyHosts != null && nonProxyHosts.length() > 0)
        {   
            String[] nonProxyHostsArray = nonProxyHosts.split("\\|");
            String serviceHost = serviceURL.getHost();
            
            for (String nonProxyHost : nonProxyHostsArray) {
                if ( nonProxyHost.equals(serviceHost)) {
                    serviceIsNonProxyHost = true;
                    break;
                }
            }
        }
        // set proxy:
        if ( serviceIsNonProxyHost == false
          && host != null && host.length() > 0
          && port != null && port.length() > 0)
        {
            int portNumber = Integer.parseInt(port);
            hostConfig.setProxy(host, portNumber);
            LOGGER.info("Using proxy: " + host + " on port: " + portNumber);
        }
        
        return hostConfig;
    }

    public static String readText(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        StringBuffer sb = new StringBuffer();
        for (int i=0; (line = br.readLine()) != null; i++) {
            
            // if not first line --> append "\n"
            if (i > 0) {
                sb.append("\n");
            }
            
            sb.append(line);
        }
        br.close();

        return sb.toString();
    }
    
    public static String readText(URL url) throws IOException {
        return readText(url.openStream());
    }
    
    public static String readText(File file) throws IOException {
        return readText(file.toURL());
    }

    public static String supplyProperty(String key, URL url) throws IOException {
        InputStream in = url.openConnection().getInputStream();
        Properties prop = new Properties();
        prop.load(in);

        String erg = prop.getProperty(key);

        return erg;
    }

    public static void saveFile(OutputStream out, InputStream in) throws IOException {
    	BufferedOutputStream bufout = new BufferedOutputStream(out);
        int b;
        while ( (b = in.read()) != -1) {
        	bufout.write(b);
        }
        bufout.flush();
        bufout.close();
        out.flush();
        out.close();
    }
    
    public static void saveFile(String filename, URL url) throws IOException {
        InputStream in = url.openConnection().getInputStream();
        OutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
        saveFile(out, in);
    }
    
    public static void saveFile(File file, InputStream in) throws IOException {
        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        saveFile(out, in);
    }

    public static void saveFile(File filename, File fileIn) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(fileIn));
        OutputStream out = new BufferedOutputStream(new FileOutputStream(filename));
        saveFile(out, in);
    }
    
    public static void saveFile(File filename, String stringToStoreInFile) throws IOException {
        OutputStream out = new FileOutputStream(filename);
        out.write(stringToStoreInFile.getBytes());
        out.flush();
        out.close();
    }

    /**
     * 
     * @param filename
     * @param msg
     * @param append
     *        if <code>true</code>, then bytes will be written to the end of the file rather than the
     *        beginning
     * @throws IOException
     */
    public static void saveFile(String filename, String msg, boolean append) throws IOException {
        FileWriter writer = new FileWriter(new File(filename), append);

        writer.write(msg);
        writer.flush();
        writer.close();
    }

    public static void decompressAll(File zipFile, File targetDirectory) throws IOException,
            ZipException {

        if ( !targetDirectory.isDirectory())
            throw new IOException("2nd Parameter targetDirectory is not a valid diectory!");

        byte[] buf = new byte[4096];
        ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile));
        while (true) {
            // N�chsten Eintrag lesen
            ZipEntry entry = in.getNextEntry();
            if (entry == null) {
                break;
            }

            // Ausgabedatei erzeugen
            FileOutputStream out = new FileOutputStream(targetDirectory.getAbsolutePath() + "/"
                    + new File(entry.getName()).getName());
            // Lese-Vorgang
            int len;
            while ( (len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();

            // Eintrag schlie�en
            in.closeEntry();
        }
        in.close();
    }

    public static void compressFilesToZip(File[] files, File zipFile) throws IOException,
            ZipException {
        File[] clearedFiles = removeDoubleFiles(files);

        byte[] buf = new byte[4096];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

        for (int i = 0; i < clearedFiles.length; i++) {
            String fname = clearedFiles[i].getAbsolutePath();

            FileInputStream in = new FileInputStream(fname);
            ZipEntry entry = new ZipEntry(fname);
            out.putNextEntry(entry);
            int len;
            while ( (len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
        }
        out.close();
    }

    /**
     * helper-method for compressFilesToZip
     */
    private static File[] removeDoubleFiles(File[] files) {
        ArrayList<File> tmpList = new ArrayList<File>();

        for (int i = 0; i < files.length; i++) {
            if ( !tmpList.contains(files[i])) {
                tmpList.add(files[i]);
            }
        }

        File[] res = new File[tmpList.size()];
        for (int i = 0; i < tmpList.size(); i++) {
            res[i] = tmpList.get(i);
        }

        return res;
    }

}