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
package org.n52.oxf.ows.capabilities;

/**
 * This class builds the foundation to inspect the possible operations of the
 * WebService. This class contains all important information to access an OWS.
 * 
 * @author <a href="mailto:foerster@52north.org">Theodor Foerster</a>
 * @author <a href="mailto:broering@52north.org">Arne Broering</a>>
 */
public class OperationsMetadata {

	/**
	 * Metadata for the operations that this server interface implements <br>
	 * <br>
	 * One or more (mandatory)
	 */
	private Operation[] operations;

	/**
	 * ParameterContainer applies to one or more operations which this server
	 * implements.<br>
	 * The parameter can be an input and/or output parameter of the operations.<br>
	 * <br>
	 * Zero or more (optional)
	 */
    //TODO: these parameters should be applied to all Operations
	private Parameter[] parameters;

	/**
	 * Constraint on valid domain of a nonparameter quantity that applies to
	 * this server.<br>
	 * <br>
	 * Zero or more (optional)
	 */
	private String[] constraints;

	/**
	 * this constructor has all required attributes as its parameters.
	 * 
	 * @param operations
	 */
	public OperationsMetadata(final Operation[] operations) {
		setOperations(operations);
	}

	/**
	 * this constructor has all attributes of the class as its parameters.
	 * 
	 * @param operations
	 * @param parameters
	 * @param constraints
	 */
	public OperationsMetadata(final Operation[] operations, final Parameter[] parameters,
			final String[] constraints) {
		setOperations(operations);
		setParameters(parameters);
		setConstraints(constraints);
	}

	/**
	 * @return a XML representation of this OperationsMetadata-section.
	 */
	public String toXML() {
		String res = "<OperationsMetadata>";

		res += "<Operations>";
		if (operations != null) {
			for (final Operation operation : operations) {
				res += operation.toXML();
			}
		}
		res += "</Operations>";

		res += "<Parameters>";
		if (parameters != null) {
			for (final Parameter parameter : parameters) {
				res += parameter.toXML();
			}
		}
		res += "</Parameters>";

		res += "<Constraints>";
		if (constraints != null) {
			for (final String c : constraints) {
				res += "<Constraint>";
				res += c;
				res += "<Constraint>";
			}
		}
		res += "</Constraints>";

		res += "</OperationsMetadata>";

		return res;
	}

	/**
	 * Representation of possible Constraints. These constraints will be
	 * inherited by all underlying operations.
	 * 
	 * @return a String representation of the constraints.
	 */
	public String[] getConstraints() {
		return constraints;
	}

	/**
	 * Representation of possible Constraints. These constraints will be
	 * inherited by all underlying operations.
	 * 
	 * @param constraints
	 *            The constraints to set.
	 */
	protected void setConstraints(final String[] constraints) {
		this.constraints = constraints;
	}

	/**
	 * @param operationName
	 * @return the Operation with the specified operationName. Returns
	 *         <code>null</code> if there isn't such an Operation.
	 */
	public Operation getOperationByName(final String operationName) {
		for (final Operation op : operations) {
			if (op != null && op.getName().equals(operationName)) {
				return op;
			}
		}
		return null;
	}

	/**
	 * @return Returns the operations.
	 */
	public Operation[] getOperations() {
		return operations;
	}

	/**
	 * @param operations
	 *            The operations to set.
	 */
	protected void setOperations(final Operation[] operations) {
		this.operations = operations;
	}

	/**
	 * This parameters are inherited by ALL operations. (ie. parameter version)
	 * 
	 * @return Returns the parameters.
	 */
	public Parameter[] getParameters() {
		return parameters;
	}

	/**
	 * This parameters are inherited by ALL operations
	 * 
	 * @param parameters
	 *            The parameters to set.
	 */
	protected void setParameters(final Parameter[] parameters) {
		this.parameters = parameters;
	}
}