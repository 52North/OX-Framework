package org.n52.oxf.feature;

/**
 * oxf's feature model depends heavily on DataType feature modeled by GeoAPI. As
 * oxf will remove the GeoAPI dependency the enums defined here will simulate
 * GeoAPI's org.opengis.feature.DataType class transitionally.
 * 
 * @deprecated as oxf will remove GeoAPI dependency
 */
@Deprecated
public enum DataType {
	INTEGER, DECIMAL, DOUBLE, STRING, DATETIME, OBJECT, GEOMETRY, STYLE;
}
