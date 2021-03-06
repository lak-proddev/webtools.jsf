package org.eclipse.jst.jsf.facelet.core.internal.view;

import org.eclipse.jst.jsf.designtime.context.DTFacesContext;
import org.eclipse.jst.jsf.designtime.internal.view.DefaultDTUIViewRoot;

/**
 * Facelet view root
 * 
 * @author cbateman
 *
 */
public class FaceletUIViewRoot extends DefaultDTUIViewRoot
{
    /**
     * 
     */
    private static final long serialVersionUID = -7289148553566455867L;

    /**
     * @param facesContext
     */
    public FaceletUIViewRoot(final DTFacesContext facesContext)
    {
        super(facesContext);
    }
}
