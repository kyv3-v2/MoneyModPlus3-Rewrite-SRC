package javassist.tools.rmi;

public class RemoteException extends RuntimeException
{
    public RemoteException(final String msg) {
        super(msg);
    }
    
    public RemoteException(final Exception e) {
        super("by " + e.toString());
    }
}
