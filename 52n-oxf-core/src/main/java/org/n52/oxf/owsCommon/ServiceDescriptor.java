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
 
 Created on: 30.03.2005
 *********************************************************************************/

package org.n52.oxf.owsCommon;

import org.n52.oxf.*;
import org.n52.oxf.owsCommon.capabilities.*;

/**
 * This class provides access to all the serviceMetadata which is described in the OWS Common spec (04-016r5h)
 * of the OGC. This Descriptor has to be filled by each ServiceAdapter.
 * 
 * The main parts of that model are:
 * <ul>
 * <li>ServiceIdentification</li>
 * <li>ServiceProvider</li>
 * <li>OperationsMetadata</li>
 * <li>Contents</li>
 * </ul>
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster </a>
 * @author <a href="mailto:broering@52north.org">Arne Broering </a>
 */
public class ServiceDescriptor {

    /**
     * required attribute.
     */
    private ServiceIdentification serviceIdentification;

    /**
     * required attribute.
     */
    private ServiceProvider serviceProvider;

    /**
     * required attribute.
     */
    private OperationsMetadata operationsMetadata;

    /**
     * required attribute.
     */
    private Contents contents;

    /**
     * Zero or one(optional) value for updateSequence is possible.
     */
    private String updateSequence;

    /**
     * The version of the GetCapabilities operation response. <br>
     * <br>
     * One (mandatory) value for version is required. <br>
     * <br>
     * Example value for version: "1.0.0"
     */
    private String version;

    /**
     * this constructor has all required attributes of the class as its parameters.
     * 
     * @param url
     * @param version
     * @param si
     * @param sp
     * @param om
     * @param c
     * @throws OXFException
     */
    public ServiceDescriptor(String version,
                             ServiceIdentification si,
                             ServiceProvider sp,
                             OperationsMetadata om,
                             Contents c) {
        setVersion(version);
        setServiceIdentification(si);
        setServiceProvider(sp);
        setOperationsMetadata(om);
        setContents(c);
    }

    /**
     * this constructor has all attributes of the class as its parameters.
     * 
     * @param url
     * @param version
     * @param si
     * @param sp
     * @param om
     * @param c
     * @param updateSequence
     * @throws OXFException
     */
    public ServiceDescriptor(String url,
                             String version,
                             ServiceIdentification si,
                             ServiceProvider sp,
                             OperationsMetadata om,
                             Contents c,
                             String updateSequence) {
        setVersion(version);
        setUpdateSequence(updateSequence);
        setServiceIdentification(si);
        setServiceProvider(sp);
        setOperationsMetadata(om);
        setContents(c);
    }

    /**
     * @return a XML representation of this ServiceDescriptor.
     */
    public String toXML() {
        String res = "<ServiceDescriptor version=\"" + version + "\" updateSequence=\"" + updateSequence + "\">";
        res += contents.toXML();
        res += operationsMetadata.toXML();
        res += serviceIdentification.toXML();
        res += serviceProvider.toXML();

        res += "</ServiceDescriptor>";
        return res;
    }

    /**
     * @return Returns the updateSequence.
     */
    public String getUpdateSequence() {
        return updateSequence;
    }

    /**
     * @param updateSequence
     *        The updateSequence to set.
     */
    protected void setUpdateSequence(String updateSequence) {
        this.updateSequence = updateSequence;
    }

    /**
     * @return Returns the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * sets the version of the GetCapabilities operation response.
     * 
     * @param version
     * @throws IllegalArgumentException
     *         if version.equals("").
     */
    protected void setVersion(String version) throws IllegalArgumentException {
        if (version.equals("")) {
            throw new IllegalArgumentException("The parameter 'version' is illegal");
        }
        this.version = version;
    }

    /**
     * @return Returns the operationsMetadata.
     */
    public OperationsMetadata getOperationsMetadata() {
        return operationsMetadata;
    }

    /**
     * @param operationsMetadata
     *        The operationsMetadata to set.
     */
    protected void setOperationsMetadata(OperationsMetadata operationsMetadata) {
        this.operationsMetadata = operationsMetadata;
    }

    /**
     * @return Returns the serviceIdentification.
     */
    public ServiceIdentification getServiceIdentification() {
        return serviceIdentification;
    }

    /**
     * @param serviceIdentification
     *        The serviceIdentification to set.
     */
    protected void setServiceIdentification(ServiceIdentification serviceIdentification) {
        this.serviceIdentification = serviceIdentification;
    }

    /**
     * @return Returns the serviceProvider.
     */
    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    /**
     * @param serviceProvider
     *        The serviceProvider to set.
     */
    protected void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    /**
     * @param contents
     */
    protected void setContents(Contents contents) {
        this.contents = contents;
    }

    /**
     * @return Returns the contents.
     */
    public Contents getContents() {
        return contents;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ServiceDescriptor #####################\nversion: ");
        sb.append(this.version);
        sb.append("\n");
        sb.append(this.serviceProvider);
        sb.append("\n");
        sb.append(this.serviceIdentification);
        sb.append("\n");
        sb.append(this.contents);
        sb.append("\n");
        sb.append(this.operationsMetadata);
        sb.append("\n");
        sb.append(this.updateSequence);
        sb.append("\n#######################################\n");
        return sb.toString();
    }
}