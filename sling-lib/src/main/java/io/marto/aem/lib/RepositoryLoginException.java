package io.marto.aem.lib;

import org.apache.sling.api.resource.LoginException;

/**
 * A {@link RuntimeException} equivalent of {@link LoginException}.
 *
 * @author Martin Petrovsky (martin at marto.io).
 */
public class RepositoryLoginException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RepositoryLoginException(String message, LoginException cause) {
        super(message, cause);
    }
}
