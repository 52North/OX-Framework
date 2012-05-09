package org.n52.oxf.feature;

import de.grdc.sampling.x10.GrdcSamplingPointDocument;
import de.grdc.sampling.x10.GrdcSamplingPointType;

public class OXFGrdcSamplingPointType extends OXFAbstractFeatureType {

	public static OXFFeature create(GrdcSamplingPointDocument xb_grdcSaPoDoc) {
		
		GrdcSamplingPointType grdcSamplingPoint = xb_grdcSaPoDoc.getGrdcSamplingPoint();
		
		String id = grdcSamplingPoint.getId();
		
		// FeatureType of the feature:
		OXFGrdcSamplingPointType oxf_grdcSaPointType = new OXFGrdcSamplingPointType();
		
		OXFFeature feature = new OXFFeature(id, oxf_grdcSaPointType);
		
		oxf_grdcSaPointType.initializeFeature(feature, grdcSamplingPoint);
		
		return feature;
	}

}
