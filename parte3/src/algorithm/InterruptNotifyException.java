package algorithm;

import java.rmi.RemoteException;

public class InterruptNotifyException extends RemoteException{
	final String message;
	
	public InterruptNotifyException(final String message)
	{
		this.message= message;
	}

	public String getMessage()
	{
		return message;
	}
}