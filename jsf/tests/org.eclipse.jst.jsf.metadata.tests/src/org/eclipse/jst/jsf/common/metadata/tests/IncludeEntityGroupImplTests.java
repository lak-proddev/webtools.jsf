/*******************************************************************************
 * Copyright (c) 2001, 2007 Oracle Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Oracle Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jsf.common.metadata.tests;

import org.eclipse.jst.jsf.common.metadata.Entity;
import org.eclipse.jst.jsf.common.metadata.IncludeEntityGroup;
import org.eclipse.jst.jsf.common.metadata.internal.MetaDataModelContextImpl;
import org.eclipse.jst.jsf.common.metadata.query.IMetaDataModelContext;
import org.eclipse.jst.jsf.common.metadata.query.MetaDataQueryHelper;

public class IncludeEntityGroupImplTests extends AbstractBaseMetaDataTestCase {
	protected IMetaDataModelContext baseContext;
	IncludeEntityGroup group;
	
	public void setUp() throws Exception {
		super.setUp();
		
		baseContext = new MetaDataModelContextImpl(project, domain, baseTestUri);
		Entity entity = MetaDataQueryHelper.getEntity(baseContext, "loaded");
		assertNotNull(entity);
		group = (IncludeEntityGroup)entity.getIncludeGroups().get(0);
	}
	public void testGetId() {
		assertEquals("eg2", group.getId());
	}

	public void testSetId() {
		String id = group.getId();
		group.setId("new");
		assertEquals("new",group.getId());
		group.setId(id);
	}

	public void testGetModelUri() {
		assertNull(group.getModelUri());		
	}

	public void testSetModelUri() {
		String uri = null;
		group.setModelUri("new");
		assertEquals("new",group.getModelUri());
		group.setModelUri(uri);
	}

}
