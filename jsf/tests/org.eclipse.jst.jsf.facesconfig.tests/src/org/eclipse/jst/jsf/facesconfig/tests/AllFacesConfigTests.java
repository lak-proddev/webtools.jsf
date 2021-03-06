/***************************************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM Corporation - initial API and implementation
 **************************************************************************************************/
package org.eclipse.jst.jsf.facesconfig.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.jst.jsf.facesconfig.tests.read.AllReadTests;
import org.eclipse.jst.jsf.facesconfig.tests.write.AllWriteTests;


/**
 * Container for all JSF tooling unit tests.
 *
 * @author spaxton
 */
public class AllFacesConfigTests extends TestSuite {

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

	public static Test suite() {
		return new AllFacesConfigTests();
	}

	public AllFacesConfigTests() {
		super();
		addTest(AllReadTests.suite());
		addTest(AllWriteTests.suite());
	}
}
