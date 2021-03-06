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
package org.eclipse.jst.jsf.core.tests.validation;

import java.util.zip.ZipFile;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jst.common.project.facet.core.JavaFacet;
import org.eclipse.jst.jsf.common.internal.types.TypeComparatorDiagnosticFactory;
import org.eclipse.jst.jsf.core.tests.TestsPlugin;
import org.eclipse.jst.jsf.core.tests.util.JSFCoreUtilHelper;
import org.eclipse.jst.jsf.test.util.JSFTestUtil;
import org.eclipse.jst.jsf.test.util.WebProjectTestEnvironment;
import org.eclipse.jst.jsf.validation.internal.XMLViewDefnValidator;
import org.eclipse.jst.jsf.validation.internal.strategy.ContainmentValidatingStrategy;
import org.eclipse.jst.jsp.core.internal.domdocument.DOMModelForJSP;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

public class TestJSPSemanticsValidator_AttributeValues extends TestCase
{
    private WebProjectTestEnvironment _webProject;
    private boolean					  _containmentValidationEnabled;
    

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        JSFTestUtil.setValidationEnabled(false);
        JSFTestUtil.setInternetProxyPreferences(true, "www-proxy.uk.oracle.com", "80");

        /* https://bugs.eclipse.org/bugs/show_bug.cgi?id=296496
        final ZipFile zipFile = JSFTestUtil.createZipFile(TestsPlugin.getDefault().getBundle()
                , "/testfiles/testzips/ValidationTestProject1.zip");

        _webProject = new WebProjectTestEnvironment(this, JavaFacetUtils.JAVA_50, ProjectFacetsManager.getProjectFacet( "jst.web" ).getVersion("2.4"));
        _webProject.createFromZip(zipFile, true);
        */
        final ZipFile zipFile = JSFTestUtil.createZipFile(
        		TestsPlugin.getDefault().getBundle(),
        		"/testfiles/testzips/ValidationTestProject2.zip");
        _webProject = new WebProjectTestEnvironment(
        		this,
        		JavaFacet.VERSION_1_5,
        		ProjectFacetsManager.getProjectFacet( "jst.web" ).getVersion("2.4"));
        _webProject.createFromZip2(zipFile, true);
        JSFCoreUtilHelper.injectTestTagRegistryFactoryProvider(JSFCoreUtilHelper.createSimpleRegistryFactory());
        Job.getJobManager().beginRule(_webProject.getTestProject(), null);
        _containmentValidationEnabled = ContainmentValidatingStrategy.isEnabled();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
        Job.getJobManager().endRule(_webProject.getTestProject());
        JSFCoreUtilHelper.injectTestTagRegistryFactoryProvider(null);
    }

    public void testSanity() throws Exception
    {
        final IProject project = _webProject.getTestProject();
        assertNotNull(project);
        assertTrue(project.isAccessible());

        final IFile jspFile = project.getFile(new Path("WebContent/NonELValidation.jsp"));
        assertTrue(jspFile.isAccessible());

        IStructuredModel jspModel = null;
        try
        {
            jspModel = StructuredModelManager.getModelManager().getModelForRead(jspFile);
            assert(jspModel instanceof DOMModelForJSP);
        }
        finally
        {
            if (jspModel != null)
            {
                jspModel.releaseFromRead();
            }
        }
    }

    public void testNonELValidation() throws Exception
    {
        final IProject project = _webProject.getTestProject();
        assertNotNull(project);
        assertTrue(project.isAccessible());

        final IFile jspFile = project.getFile(new Path("WebContent/NonELValidation.jsp"));
        assertTrue(jspFile.isAccessible());

        final MockValidationReporter  mockReporter = new MockValidationReporter();
        final XMLViewDefnValidator validator = new XMLViewDefnValidator();
        validator.validateView(jspFile, mockReporter);

        // there should only be 3, but because we don't want to have the jars
        // in the path, we trigger a containment warning on the loadBundle
        // since the f:view in the doc can't be fully resolved.
        assertEquals(_containmentValidationEnabled ? 4 : 3, mockReporter.getReportedProblems().size());

        mockReporter.assertExpectedMessage(591, 25, IMessage.HIGH_SEVERITY);
        mockReporter.assertExpectedMessage(936, 1, IMessage.NORMAL_SEVERITY);
        mockReporter.assertExpectedMessage(969, 9, IMessage.NORMAL_SEVERITY);
    }

    public void testELValidation() throws Exception
    {
        final IProject project = _webProject.getTestProject();
        assertNotNull(project);
        assertTrue(project.isAccessible());

        final IFile jspFile = project.getFile(new Path("WebContent/ELValidation.jsp"));
        assertTrue(jspFile.isAccessible());

        final MockValidationReporter  mockReporter = new MockValidationReporter();
        final XMLViewDefnValidator validator = new XMLViewDefnValidator();
        validator.validateView(jspFile, mockReporter);

        // there should only be 5, but because we don't want to have the jars
        // in the path, we trigger a containment warning on the loadBundle
        // since the f:view in the doc can't be fully resolved.
        // at 845 we also get two, one for syntax error and one for missing bracket
        assertEquals(_containmentValidationEnabled ? 10 : 9, mockReporter.getReportedProblems().size());

        mockReporter.assertExpectedMessage(603, 2, IMessage.NORMAL_SEVERITY);
        mockReporter.assertExpectedMessage(648, 4, IMessage.NORMAL_SEVERITY);
        // the default severity for no var messages is now low.
        mockReporter.assertExpectedMessage(696, 5, IMessage.LOW_SEVERITY);
        mockReporter.assertExpectedMessage(753, 6, IMessage.NORMAL_SEVERITY);
        mockReporter.assertExpectedMessage(802, 4, IMessage.HIGH_SEVERITY);

        // two on this one: syntax error and missing bracket
        mockReporter.assertExpectedMessage(846, 5, IMessage.HIGH_SEVERITY);
        mockReporter.assertExpectedMessage(847, 3, IMessage.NORMAL_SEVERITY);

        mockReporter.assertExpectedMessage(946, 24, IMessage.NORMAL_SEVERITY, TypeComparatorDiagnosticFactory.PROPERTY_NOT_WRITABLE_ID);

        mockReporter.assertExpectedMessage(1015, 40, IMessage.HIGH_SEVERITY);
    }
}
