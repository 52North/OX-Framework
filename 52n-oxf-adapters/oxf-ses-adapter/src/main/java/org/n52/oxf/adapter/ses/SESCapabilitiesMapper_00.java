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

package org.n52.oxf.adapter.ses;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.opengis.ows.x11.AddressType;
import net.opengis.ows.x11.ContactType;
import net.opengis.ows.x11.DomainType;
import net.opengis.ows.x11.LanguageStringType;
import net.opengis.ows.x11.OnlineResourceType;
import net.opengis.ows.x11.OperationDocument;
import net.opengis.ows.x11.OperationsMetadataDocument.OperationsMetadata;
import net.opengis.ows.x11.ResponsiblePartySubsetType;

import org.n52.oxf.OXFException;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.Address;
import org.n52.oxf.ows.capabilities.Contact;
import org.n52.oxf.ows.capabilities.Contents;
import org.n52.oxf.ows.capabilities.DCP;
import org.n52.oxf.ows.capabilities.GetRequestMethod;
import org.n52.oxf.ows.capabilities.OnlineResource;
import org.n52.oxf.ows.capabilities.Operation;
import org.n52.oxf.ows.capabilities.Parameter;
import org.n52.oxf.ows.capabilities.PostRequestMethod;
import org.n52.oxf.ows.capabilities.RequestMethod;
import org.n52.oxf.ows.capabilities.ServiceContact;
import org.n52.oxf.ows.capabilities.ServiceIdentification;
import org.n52.oxf.ows.capabilities.ServiceProvider;

/**
 * maps the Capabilities of the SES into a ServiceDescriptor object.
 * 
 * NOT YET COMPLETED!
 * TODO
 * - filterCapabilities need to be handled somewhere; take a look @ SOS-stuff -> mapContents(...)
 * 
 * @author <a href="mailto:broering@52north.org">Arne Br&ouml;ring</a>
 * @author <a href="mailto:ehjuerrens@uni-muenster.de">Eike Hinderk J&uuml;rrens</a>
 * 
 */
public class SESCapabilitiesMapper_00 {

    public ServiceDescriptor mapCapabilities(net.opengis.ses.x00.CapabilitiesDocument capsDoc) throws OXFException {

        String version = mapVersion(capsDoc);
        ServiceIdentification serviceIdentification = mapServiceIdentification(capsDoc.getCapabilities().getServiceIdentification());
        ServiceProvider serviceProvider = mapServiceProvider(capsDoc.getCapabilities().getServiceProvider());
        // FIXME Methods not finished
        org.n52.oxf.ows.capabilities.OperationsMetadata operationsMetadata = mapOperationsMetadata(capsDoc.getCapabilities().getOperationsMetadata());
        Contents contents = mapContents(capsDoc.getCapabilities().getContents());
        
        capsDoc.getCapabilities().getFilterCapabilities();

        ServiceDescriptor serviceDesc = new ServiceDescriptor(version,
                serviceIdentification,
                serviceProvider,
                operationsMetadata,
                contents);
        return serviceDesc;
    }

    private String mapVersion(net.opengis.ses.x00.CapabilitiesDocument capsDoc) {
        return capsDoc.getCapabilities().getVersion();
    }

    /**
     * this method goes through the supported operations declared in the OperationsMetadata-section of the
     * SAS-Capabilities and maps the provided informations to the OXF internal capabilities-model (e.g. the
     * class org.n52.oxf.ows.capabilities.OperationsMetadata)
     * 
     * @param metadata
     * 
     * @param xb_opMetadata
     * @return
     */
    private static org.n52.oxf.ows.capabilities.OperationsMetadata mapOperationsMetadata(OperationsMetadata xb_opMetadata) {

        //
        // map the operations:
        //

        OperationDocument.Operation[] xb_operations = xb_opMetadata.getOperationArray();

        Operation[] oc_operations = new Operation[xb_operations.length];

        for (int i = 0; i < xb_operations.length; i++) {
            OperationDocument.Operation xb_operation = xb_operations[i];

            String oc_operationName = xb_operation.getName();

            //
            // map the operations DCPs:
            //

            net.opengis.ows.x11.DCPDocument.DCP[] xb_dcps = xb_operation.getDCPArray();
            DCP[] oc_dcps = new DCP[xb_dcps.length];
            for (int j = 0; j < xb_dcps.length; j++) {
                net.opengis.ows.x11.DCPDocument.DCP xb_dcp = xb_dcps[j];

                //
                // map the RequestMethods:
                //

                List<RequestMethod> oc_requestMethods = new ArrayList<RequestMethod>();

                net.opengis.ows.x11.RequestMethodType[] xb_getRequestMethods = xb_dcp.getHTTP().getGetArray();
                for (int k = 0; k < xb_getRequestMethods.length; k++) {
                    net.opengis.ows.x11.RequestMethodType xb_getRequestMethod = xb_getRequestMethods[k];
                    OnlineResource oc_onlineRessource = new OnlineResource(xb_getRequestMethod.getHref());
                    RequestMethod oc_requestMethod = new GetRequestMethod(oc_onlineRessource);
                    oc_requestMethods.add(oc_requestMethod);
                }

                net.opengis.ows.x11.RequestMethodType[] xb_postRequestMethods = xb_dcp.getHTTP().getPostArray();
                for (int k = 0; k < xb_postRequestMethods.length; k++) {
                    net.opengis.ows.x11.RequestMethodType xb_postRequestMethod = xb_postRequestMethods[k];
                    OnlineResource oc_onlineRessource = new OnlineResource(xb_postRequestMethod.getHref());
                    RequestMethod oc_requestMethod = new PostRequestMethod(oc_onlineRessource);
                    oc_requestMethods.add(oc_requestMethod);
                }

                oc_dcps[j] = new DCP(oc_requestMethods);
            }

            //
            // map the operations parameters:
            //

            DomainType[] xb_parameters = xb_operation.getParameterArray();

            List<Parameter> oc_parameters = new ArrayList<Parameter>();

            for (int j = 0; j < xb_parameters.length; j++) {

                DomainType xb_parameter = xb_parameters[j];

                String parameterName = xb_parameter.getName();

                // FIXME Which parameters will be used by the SES?
                /*
                if (parameterName.equalsIgnoreCase("eventTime")) {
                    // do nothing! because the eventTime Parameter will be added
                    // from Contents section
                }
                else {
                    boolean oc_required;
                    if (xb_parameter.getUse() != null && xb_parameter.getUse().equals("required")) {
                        oc_required = true;
                    }
                    else {
                        oc_required = false;
                    }

                    //
                    // map the parameters' values to StringValueDomains
                    //

                    ValueType[] xb_values = xb_parameter.getValueArray();
                    StringValueDomain oc_values = new StringValueDomain();
                    for (int k = 0; k < xb_values.length; k++) {
                        ValueType xb_value = xb_values[k];

                        oc_values.addPossibleValue(xb_value.getStringValue());
                    }

                    Parameter oc_parameter = new Parameter(parameterName, oc_required, oc_values, null);
                    oc_parameters.add(oc_parameter);
                }
                 */
            }

            Parameter[] parametersArray = new Parameter[oc_parameters.size()];
            oc_parameters.toArray(parametersArray);

            oc_operations[i] = new Operation(oc_operationName, parametersArray, null, oc_dcps);
        }

        return new org.n52.oxf.ows.capabilities.OperationsMetadata(oc_operations);
    }

    /**
     * The SES content part of the GetCapabilitiesResponse only contains the IDs of the
     * registered Sensors and some OGC FilterCapbilities
     * FIXME Filter_Capabilities are not included in the XMLBeans
     */
    private Contents mapContents(net.opengis.ses.x00.ContentsDocument.Contents xb_contents) throws OXFException {

        Contents oc_contents = new Contents();

        //
        // mapping of Registered Sensors:
        //
        if (xb_contents == null) {
            throw new NullPointerException();
        }
        if (xb_contents.getRegisteredSensors() == null) {
            return new Contents();
        }
        String[] xb_registeredSensorIDsArray = xb_contents.getRegisteredSensors().getSensorIDArray();

        for (int i = 0; i < xb_registeredSensorIDsArray.length; i++) {

            String xb_registeredSensorID = xb_registeredSensorIDsArray[i];

            oc_contents.getDataIdentification(xb_registeredSensorID);
        }

        return oc_contents;
    }

    /**
     * 
     * @param capsDoc
     * @return
     */
    private ServiceIdentification mapServiceIdentification(net.opengis.ows.x11.ServiceIdentificationDocument.ServiceIdentification xb_serviceIdentification) {

        String oc_title = xb_serviceIdentification.getTitleArray(0).getStringValue();
        String oc_serviceType = xb_serviceIdentification.getServiceType().getStringValue();
        String[] oc_serviceTypeVersions = xb_serviceIdentification.getServiceTypeVersionArray();

        String oc_fees = xb_serviceIdentification.getFees();
        String[] oc_accessConstraints = xb_serviceIdentification.getAccessConstraintsArray();
        String oc_abstract = xb_serviceIdentification.getAbstractArray(0).getStringValue();
        String[] oc_keywords = null;

        Vector<String> oc_keywordsVec = new Vector<String>();
        for (int i = 0; i < xb_serviceIdentification.getKeywordsArray().length; i++) {
            LanguageStringType[] xb_keywords = xb_serviceIdentification.getKeywordsArray(i).getKeywordArray();
            for (LanguageStringType xb_keyword : xb_keywords) {
                oc_keywordsVec.add(xb_keyword.getStringValue());
            }
        }
        oc_keywords = new String[oc_keywordsVec.size()];
        oc_keywordsVec.toArray(oc_keywords);

        return new ServiceIdentification(oc_title,
                oc_serviceType,
                oc_serviceTypeVersions,
                oc_fees,
                oc_accessConstraints,
                oc_abstract,
                oc_keywords);
    }

    /**
     * 
     * @param xb_serviceProvider
     * @return
     */
    private ServiceProvider mapServiceProvider(net.opengis.ows.x11.ServiceProviderDocument.ServiceProvider xb_serviceProvider) {
        ResponsiblePartySubsetType xb_contact = xb_serviceProvider.getServiceContact();
        ContactType xb_contactType = xb_contact.getContactInfo();
        
        String providerName = xb_serviceProvider.getProviderName();
        String individualName = xb_contact.getIndividualName();
        String positionName = xb_contact.getPositionName();
        
        String[] telephones = (xb_contactType.getPhone()!=null?
                (xb_contactType.getPhone().getVoiceArray()!=null?
                        xb_contactType.getPhone().getVoiceArray():null):null);
        
        String[] fax = (xb_contactType.getPhone()!=null?
                (xb_contactType.getPhone().getFacsimileArray()!=null?
                        xb_contactType.getPhone().getFacsimileArray():null):null);
        
        String hoursOfService = xb_contactType.getHoursOfService();
        String contactInstructions = xb_contactType.getContactInstructions();
        
        AddressType xb_address = xb_contactType.getAddress();
        
        String city = xb_address!=null?xb_address.getCity():null;
        String adminArea = xb_address!=null?xb_address.getAdministrativeArea():null;
        String postalCode = xb_address!=null?xb_address.getPostalCode():null;
        String country = xb_address!=null?xb_address.getCountry():null;
        String[] deliveryPoints = xb_address!=null?xb_address.getDeliveryPointArray():null;
        String[] eMails = xb_address!=null?xb_address.getElectronicMailAddressArray():null;
        
        Address oc_address = new Address(city,adminArea,postalCode,country,deliveryPoints,eMails);
        
        OnlineResourceType xb_onlineRes = xb_contactType.getOnlineResource();
        
        String type = xb_onlineRes!=null?xb_onlineRes.getType().toString():null;
        String href = xb_onlineRes!=null?xb_onlineRes.getHref():null;
        String role = xb_onlineRes!=null?xb_onlineRes.getRole():null;
        String arcrole = xb_onlineRes!=null?xb_onlineRes.getArcrole():null;
        String show = xb_onlineRes!=null?xb_onlineRes.getShow().toString():null;
        String actuate = xb_onlineRes!=null?xb_onlineRes.getActuate().toString():null;
        String title = xb_onlineRes!=null?xb_onlineRes.getTitle():null;
        
        OnlineResource oc_onlineRes = new OnlineResource(type,href,role,arcrole,show,actuate,title);

        Contact oc_contactInfo = new Contact(telephones,fax,hoursOfService,contactInstructions,oc_address,oc_onlineRes);
        ServiceContact oc_serviceContact = new ServiceContact(individualName, providerName ,positionName, oc_contactInfo);
        
        return  new ServiceProvider(providerName,oc_serviceContact,oc_onlineRes);
    }
}
