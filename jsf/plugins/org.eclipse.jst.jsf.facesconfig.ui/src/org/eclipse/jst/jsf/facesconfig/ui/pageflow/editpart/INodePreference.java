/*******************************************************************************
 * Copyright (c) 2004, 2005 Sybase, Inc. and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sybase, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jsf.facesconfig.ui.pageflow.editpart;


/**
 * This interface can be used to set NodeEditPart's preference
 * 
 * @author Xiaoguang Zhang
 * 
 */

public interface INodePreference extends IFigurePreference {

	/**
	 * Sets the text placement of the label relative to its icon. The default is
	 * {@link org.eclipse.draw2d.PositionConstants#EAST}. Other possible values are
	 * {@link org.eclipse.draw2d.PositionConstants#NORTH}, {@link org.eclipse.draw2d.PositionConstants#SOUTH} and
	 * {@link org.eclipse.draw2d.PositionConstants#WEST}.
	 * 
	 * @param where
	 *            the text placement
	 */
	void setTextPlacement(int where);

}
