/*******************************************************************************
 * Copyright (c) 2001, 2008 Oracle Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Oracle Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jsf.context.symbol.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for all context.symbol tests
 * 
 * @author cbateman
 *
 */
public class AllTests {
    /**
     * @return the all tests suite
     */
    public static Test suite() 
    {
        TestSuite suite = new TestSuite("Test for base symbols framework");
        //$JUnit-BEGIN$
        suite.addTestSuite(TestIJavaTypeDescriptor2.class);
        suite.addTestSuite(TestIPropertySymbolItemProvider.class);
        suite.addTestSuite(TestTypeCoercion.class);
        // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=217135
       // suite.addTestSuite(TestIJavaTypeDescriptor2_ChangeStability.class);
        //$JUnit-END$
        return suite;
    }
}
