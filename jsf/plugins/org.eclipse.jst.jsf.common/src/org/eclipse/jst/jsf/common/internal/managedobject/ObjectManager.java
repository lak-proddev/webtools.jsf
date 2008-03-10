package org.eclipse.jst.jsf.common.internal.managedobject;

/**
 * An abstraction for a manager that manages managed objects based on a KEYTYPE
 * 
 * @author cbateman
 *
 * @param <T>
 * @param <KEYTYPE> 
 */
public abstract class ObjectManager<T extends IManagedObject, KEYTYPE>
{
    /**
     * A valid instance of T for the key.  The instance of T may be unique on
     * a per-key basis or may not.
     *  
     * @param key
     * @return an instance of the managed object associated with key
     * @throws ManagedObjectException if an error occurs during construction
     */
    public abstract T getInstance(KEYTYPE key) throws ManagedObjectException;

    /**
     * Indicates a problem that occurred during a managed object operation
     * @author cbateman
     *
     */
    public static class ManagedObjectException extends Exception
    {
        /**
         * 
         */
        private static final long serialVersionUID = -8723548990029368844L;

        /**
         * 
         */
        public ManagedObjectException()
        {
            super();
        }

        /**
         * @param message
         * @param cause
         */
        public ManagedObjectException(String message, Throwable cause)
        {
            super(message, cause);
        }

        /**
         * @param message
         */
        public ManagedObjectException(String message)
        {
            super(message);
        }

        /**
         * @param cause
         */
        public ManagedObjectException(Throwable cause)
        {
            super(cause);
        }
    }
}
