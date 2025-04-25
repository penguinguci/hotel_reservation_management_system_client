package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote {
    void onDataChange(String message) throws RemoteException;
}