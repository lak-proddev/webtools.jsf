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
package org.eclipse.jst.pagedesigner.css2.list;

/**
 * A simple template objec, when we resole content in detail, we will create
 * more complicated object reference structure.
 * 
 * @author mengbo
 */
public class ContentObject {
	private ICounterValueGenerator _counter;

	/**
	 * @return Returns the _counter.
	 */
	public ICounterValueGenerator getCounter() {
		return _counter;
	}

	/**
	 * @param counter
	 *            The _counter to set.
	 */
	public void setCounter(ICounterValueGenerator counter) {
		this._counter = counter;
	}
}
