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
package org.eclipse.jst.pagedesigner.jsp.core.internal.pagevar;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.pagedesigner.jsp.core.IJSPCoreConstants;
import org.eclipse.jst.pagedesigner.jsp.core.JSPCorePlugin;
import org.eclipse.jst.pagedesigner.jsp.core.pagevar.IVariableInfo;
import org.eclipse.jst.pagedesigner.jsp.core.util.CMUtil;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.w3c.dom.Element;

/**
 * @author mengbo
 * @version 1.5
 */
public class PageVariableAdatperRegistry {
	/**
	 * pageVar
	 */
	public static final String PAGEVAR = "pageVar"; //$NON-NLS-1$

	/**
	 * taglib
	 */
	public static final String TAGLIB = "taglib"; //$NON-NLS-1$

	/**
	 * tag
	 */
	public static final String TAG = "tag"; //$NON-NLS-1$

	/**
	 * varName
	 */
	public static final String VARNAME = "varName"; //$NON-NLS-1$

	/**
	 * varNameIsAttr
	 */
	public static final String VARNAMEISATTR = "varNameIsAttr"; //$NON-NLS-1$

	/**
	 * varTypeMode
	 */
	public static final String VARTYPEMODE = "varTypeMode"; //$NON-NLS-1$

	/**
	 * tagName
	 */
	public static final String TAGNAME = "tagName"; //$NON-NLS-1$

	/**
	 * varTypeString
	 */
	public static final String VARTYPESTRING = "varTypeString"; //$NON-NLS-1$

	/**
	 * varTypeStringIsAttr
	 */
	public static final String VARTYPESTRINGISATTR = "varTypeStringIsAttr"; //$NON-NLS-1$

	/**
	 * uri
	 */
	public static final String URI = "uri"; //$NON-NLS-1$

	private static PageVariableAdatperRegistry _instance = null;

	private Map _registry;

	private TagVarDescriptor _useBeanDescriptor;

	/**
	 * @return the singleton instance
	 */
	public static PageVariableAdatperRegistry getInstance() {
		if (_instance == null) {
			_instance = new PageVariableAdatperRegistry();
		}
		return _instance;
	}

	private PageVariableAdatperRegistry() {
		_registry = readAllDescriptors();
	}

	// public IPageVariableAdapter createAdapter(IDOMElement ele, String uri,
	// String tagname)
	// {
	// String key = uri + "#" + tagname;
	// TagVarDescriptor desc = (TagVarDescriptor) _registry.get(key);
	// if (desc != null)
	// {
	// PageVariableAdapter adapter = new PageVariableAdapter(ele);
	// adapter.setVarName(desc.getVarName());
	// adapter.setVarNameAttrName(desc.isVarNameIsAttr());
	// adapter.setVarTypeString(desc.getVarTypeString());
	// adapter.setVarTypeAttrName(desc.isVarTypeStringIsAttr());
	// adapter.setVarTypeMode(desc.getVarTypeMode());
	// return adapter;
	// }
	// else
	// {
	// return null;
	// }
	// }

	private Map readAllDescriptors() {
		Map map = new HashMap();

		IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
				.getExtensionPoint(JSPCorePlugin.getPluginId(), PAGEVAR);
		IExtension[] extensions = extensionPoint.getExtensions();

		for (int i = 0; i < extensions.length; i++) {
			IExtension ext = extensions[i];
			IConfigurationElement[] facs = ext.getConfigurationElements();

			for (int j = 0; j < facs.length; j++) {
				if (facs[j].getName().equals(TAGLIB)) {
					String uri = facs[j].getAttribute(URI);
					if (uri == null || uri.length() == 0) {
						continue;
					}
					IConfigurationElement[] tags = facs[j].getChildren(TAG);
					for (int k = 0; k < tags.length; k++) {
						TagVarDescriptor desc = new TagVarDescriptor();

						String tag = tags[k].getAttribute(TAGNAME);
						String varName = tags[k].getAttribute(VARNAME);
						boolean varNameIsAttr = "true".equalsIgnoreCase(tags[k] //$NON-NLS-1$
								.getAttribute(VARNAMEISATTR));
						String varTypeString = tags[k]
								.getAttribute(VARTYPESTRING);
						boolean varTypeStringIsAttr = "true" //$NON-NLS-1$
								.equalsIgnoreCase(tags[k]
										.getAttribute(VARTYPESTRINGISATTR));
						String varTypeModeString = tags[k]
								.getAttribute(VARTYPEMODE);
						int varTypeMode = toVarTypeMode(varTypeModeString);

						desc.setVarName(varName);
						desc.setVarNameIsAttr(varNameIsAttr);
						desc.setVarTypeMode(varTypeMode);
						desc.setVarTypeString(varTypeString);
						desc.setVarTypeStringIsAttr(varTypeStringIsAttr);
						desc.setTagName(tag);

						// Use uri#tag as key.
						map.put(uri + "#" + tag, desc); //$NON-NLS-1$
					}
				}
			}
		}

		return map;
	}

	/**
	 * @param varTypeModeString
	 * @return variable typemode
	 */
	public static int toVarTypeMode(String varTypeModeString) {
		if ("CLASSNAME".equalsIgnoreCase(varTypeModeString)) { //$NON-NLS-1$
			return IVariableInfo.CLASSNAME;
		} else if ("EXPRESSION".equalsIgnoreCase(varTypeModeString)) { //$NON-NLS-1$
			return IVariableInfo.EXPRESSION;
		} else if ("EXPRESSION_COLLECTION_ITEM" //$NON-NLS-1$
				.equalsIgnoreCase(varTypeModeString)) {
			return IVariableInfo.EXPRESSION_LISTITEM;
		} else if ("BUNDLE".equalsIgnoreCase(varTypeModeString)) { //$NON-NLS-1$
			return IVariableInfo.RESOURCEBUNDLE;
		} else {
			return IVariableInfo.EXPRESSION; // default
		}
	}

	/**
	 * @param uri
	 * @param tagname
	 * @return the tag variable descriptor for the given tag (may be null)
	 */
	public TagVarDescriptor getTagVarDescriptor(String uri, String tagname) {
		String key = uri + "#" + tagname; //$NON-NLS-1$
		return (TagVarDescriptor) _registry.get(key);
	}

	/**
	 * @param localName
	 * @return
	 */
	private TagVarDescriptor getJSPTagVarDescriptor(String localName) {
		if (IJSPCoreConstants.TAG_USEBEAN.equals(localName)) {
			if (_useBeanDescriptor == null) {
				_useBeanDescriptor = new TagVarDescriptor();
				_useBeanDescriptor.setVarName(IJSPCoreConstants.ATTR_ID);
				_useBeanDescriptor.setVarNameIsAttr(true);
				_useBeanDescriptor.setVarTypeMode(IVariableInfo.CLASSNAME);
				_useBeanDescriptor
						.setVarTypeString(IJSPCoreConstants.ATTR_CLASS);
				_useBeanDescriptor.setVarTypeStringIsAttr(true);
			}

			return _useBeanDescriptor;
		}

		return null;
	}

	/**
	 * @param target 
	 * @return the tag variable descriptor for the target
	 */
	public TagVarDescriptor getTagVarDescriptor(Element target) {
		if (target instanceof IDOMElement) {
			IDOMElement xmlEle = (IDOMElement) target;
			CMElementDeclaration decl = CMUtil.getElementDeclaration(xmlEle);
			if (decl != null) {
			    if (CMUtil.isJSP(decl)) {
			        return getJSPTagVarDescriptor(xmlEle.getLocalName());
			    }
                String uri = CMUtil.getTagURI(decl);
                if (uri != null) {
                    return getTagVarDescriptor(uri, xmlEle.getLocalName());
                }
			}
		}
        return null;
	}

}
