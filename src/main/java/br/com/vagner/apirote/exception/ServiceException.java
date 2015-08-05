package br.com.vagner.apirote.exception;

/**
 * Exception used to report errors to access database
 * 
 * @author vagner
 * 
 */
public class ServiceException extends Exception {

	private static final long serialVersionUID = -4794674295148729908L;

	public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}