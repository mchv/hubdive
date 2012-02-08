package util.connection;

public class ConnectionException extends Exception {

	private int errorCode;
	
	public ConnectionException(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public ConnectionException(Throwable throwable) {
		super(throwable);
		this.errorCode = 0;
	}
	
	
	public ConnectionException(String message) {
		super(message);
		this.errorCode = 0;
	}
	
	/**
	 * Get the error code associated to this connection exception
	 * @return the error code
	 * 
	 */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * Check if the exception has an HTTP error code. in this case you could retrieve the code with <code>getErrorCode()</code> otherwise you could retrieve the cause.
	 * @return <code>true</code> if exception has an HTTP error code, <code>false</code> otherwise.
	 * @see ConnectionException#getErrorCode()
	 * @see Exception#getCause()
	 */
	public boolean hasErrorCode() {
		return errorCode != 0;
	}
	
}
