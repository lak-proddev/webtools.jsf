/*******************************************************************************
 * Copyright (c) 2004, 2006 Sybase, Inc. and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Sybase, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.jsf.facesconfig.ui.pageflow.synchronization;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.jst.jsf.facesconfig.common.logging.Logger;
import org.eclipse.jst.jsf.facesconfig.emf.NavigationCaseType;
import org.eclipse.jst.jsf.facesconfig.ui.EditorPlugin;
import org.eclipse.jst.jsf.facesconfig.ui.pageflow.model.PageflowElement;
import org.eclipse.jst.jsf.facesconfig.ui.pageflow.model.PageflowLink;
import org.eclipse.jst.jsf.facesconfig.ui.pageflow.model.PageflowNode;
import org.eclipse.jst.jsf.facesconfig.ui.pageflow.model.PageflowPackage;
import org.eclipse.jst.jsf.facesconfig.ui.pageflow.model.PageflowPage;

/**
 * The adapter that listens to modification of pageflow and updates faces-config
 * as needed.
 * 
 * @author hmeng
 * 
 */
public class PF2FCSynchronizer extends AdapterImpl {
	FC2PFTransformer tranformer;

	private final boolean DEBUG = false;

	private static final Logger logger = EditorPlugin
			.getLogger(PF2FCSynchronizer.class);

	public PF2FCSynchronizer(FC2PFTransformer tranformer) {
		this.tranformer = tranformer;
	}

	public Notifier getTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTarget(Notifier newTarget) {
		// TODO Auto-generated method stub

	}

	public boolean isAdapterForType(Object type) {
		return type == PF2FCSynchronizer.class;
	}

	public void notifyChanged(Notification notification) {
		if (!tranformer.isListenToNotify()) {
			return;
		}
		tranformer.setInEvent(true);
		try {
			if (!(notification.getNotifier() instanceof PageflowElement)) {
				return;
			}
			processChange(notification);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (notification.getEventType() != Notification.REMOVING_ADAPTER) {
				tranformer.refreshPFAdapter(tranformer.getPageflow());
				tranformer.refreshFCAdapter(tranformer.getFacesConfig());
			}
			tranformer.setInEvent(false);
		}
	}

	private void processChange(Notification notification) {
		PageflowElement element;
		element = (PageflowElement) notification.getNotifier();
		int type = notification.getEventType();
		switch (type) {
		case Notification.ADD: {
			int featureId = notification.getFeatureID(PageflowPackage.class);
			switch (featureId) {
			case PageflowPackage.PAGEFLOW_NODE__OUTLINKS:
				addOutLink(notification, element);
				break;
			case PageflowPackage.PAGEFLOW_NODE__INLINKS:
				addInLink(notification, element);
				break;
			case PageflowPackage.PAGEFLOW:
				break;
			}
			if (!tranformer.isNeedBatchOperations()) {
				updateAndNotify(notification);
			}
			break;
		}
		case Notification.SET:
			if (notification.getNewValue() != null) {
				processSet(notification, element);
				updateAndNotify(notification);
			}
			break;
		case Notification.REMOVE:
		case Notification.REMOVE_MANY: {
			int featureId = notification.getFeatureID(PageflowPackage.class);
			switch (featureId) {
			case PageflowPackage.PF_PAGE:
				// removePage(notification, (PageflowPage) element);
				break;
			case PageflowPackage.PAGEFLOW_NODE__OUTLINKS:
			case PageflowPackage.PAGEFLOW_NODE__INLINKS:
				removeLink(notification, element);
				break;
			// case PageflowPackage.PAGEFLOW:
			// updateAndNotify(notification);
			// break;
			}
			if (!tranformer.isNeedBatchOperations()) {
				updateAndNotify(notification);
			}
			break;
		}
		}
	}

	private void processSet(Notification notification, PageflowElement element) {
		if (notification.getNewValue() != null) {
			int value = notification.getFeatureID(PageflowPackage.class);
			if (element instanceof PageflowPage) {
				setProperties(notification, element, value);
			} else if (element instanceof PageflowLink) {
				if (DEBUG)
					System.out.println("The link property is changed");
				setProperties(notification, element, value);
			}
		}
	}

	private void setProperties(Notification notification,
			PageflowElement element, int value) {
		if (notification.getFeature() == PageflowPackage.eINSTANCE
				.getPFLink_Source()) {
			if (notification.getNewValue() instanceof String) {
				tranformer.changePFLinkStart((PageflowLink) element,
						(String) notification.getNewValue());
			}
		} else if (notification.getFeature() == PageflowPackage.eINSTANCE
				.getPFLink_Target()) {
			if (notification.getNewValue() instanceof String) {
				tranformer.changePFLinkEnd((PageflowLink) element,
						(String) notification.getNewValue());
			}
		} else {
			element.getFCElements().set(
					(EStructuralFeature) notification.getFeature(),
					notification.getNewValue());
		}
	}

	private void addInLink(Notification notification, PageflowElement element) {
		Object value = notification.getNewValue();
		PageflowLink link = (PageflowLink) value;
		if (element instanceof PageflowPage) {
			PageflowNode source = link.getSource();
			if (source instanceof PageflowPage) {
				tranformer.addLink((PageflowPage) source,
						(PageflowPage) element, link);
			}
		}

	}

	private void addOutLink(Notification notification, PageflowElement element) {
		Object value = notification.getNewValue();
		// for outLink remove, only target is referenced.
		PageflowLink link = (PageflowLink) value;
		if (element instanceof PageflowPage) {
			PageflowNode target = link.getTarget();
			// page->page
			if (target instanceof PageflowPage) {
				tranformer.addLink((PageflowPage) element,
						(PageflowPage) target, link);
			}
		}
	}

	private void removeLink(Notification notification, PageflowElement element) {
		Object value = notification.getOldValue();
		PageflowLink link = (PageflowLink) value;

		if (!link.getFCElements().isEmpty()) {
			NavigationCaseType caseFC = (NavigationCaseType) link
					.getFCElements().getData().get(0);
			if (caseFC != null)
				tranformer.removeCase(caseFC);
		}
		link.getFCElements().update();
	}

	private void updateAndNotify(Notification notification) {
		tranformer.getPageflow().notifyModelChanged(
				new ENotificationImpl((InternalEObject) notification
						.getNotifier(), Notification.SET,
						PageflowPackage.PAGEFLOW, null, null));
	}

	public void dispose() {

	}
	// /**
	// * All the fromViewID or toViewID
	// *
	// * @param path
	// * @return
	// */
	// public List findFacesPages(String path) {
	// List rules = facesConfig.getNavigationRule();
	// List result = new ArrayList();
	// for (int i = 0; i < rules.size(); i++) {
	// NavigationRuleType rule = (NavigationRuleType) rules.get(i);
	// String fromViewID = TransformUtil.getFromViewID(rule);
	// if (fromViewID.equals(path)) {
	// result.add(rule.getFromViewId());
	// }
	// List cases = rule.getNavigationCase();
	// for (int j = 0; j < cases.size(); j++) {
	// NavigationCaseType navCase = (NavigationCaseType) cases
	// .get(j);
	// String toViewID = TransformUtil.getToViewID(navCase);
	// if (toViewID.equals(path)) {
	// result.add(navCase.getToViewId());
	// }
	// }
	// }
	// return result;
	// }

}