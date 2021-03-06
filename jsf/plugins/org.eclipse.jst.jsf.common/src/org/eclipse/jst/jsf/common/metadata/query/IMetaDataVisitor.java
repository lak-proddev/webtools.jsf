/*******************************************************************************
 * Copyright (c) 2007 Oracle Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Oracle - initial API and implementation
 *    
 ********************************************************************************/
package org.eclipse.jst.jsf.common.metadata.query;

/**
 * Common interface for Entity and Trait visitors
 * <p>NOT to implemented by clients directly.   Clients should subclass AbstractMetaDataVisitor instead.
 * <p><b>Provisional API - subject to change</b></p>
 */
public interface IMetaDataVisitor {
	/**
	 * @return true if visitor has recognized that visiting should stop
	 */
	public boolean stopVisiting();
}
