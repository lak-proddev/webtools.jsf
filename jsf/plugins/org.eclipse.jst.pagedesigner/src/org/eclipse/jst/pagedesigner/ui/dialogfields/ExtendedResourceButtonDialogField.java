/*******************************************************************************
 * Copyright (c) 2006 Sybase, Inc. and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sybase, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.pagedesigner.ui.dialogfields;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.pagedesigner.common.dialogfield.ResourceButtonDialogField;
import org.eclipse.jst.pagedesigner.utils.WebAppUtil;

/**
 * @author mengbo
 * @version 1.5
 */
public class ExtendedResourceButtonDialogField extends
		ResourceButtonDialogField {
	public ExtendedResourceButtonDialogField(IProject project) {
		super(project);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jst.pagedesigner.common.dialogfield.ResourceButtonDialogField#browseButtonPressed()
	 */
	protected String browseButtonPressed() {
		String url = super.browseButtonPressed();
		url = WebAppUtil.transformJSPURL(url, this.getReferredFile());
		return url;
	}
}