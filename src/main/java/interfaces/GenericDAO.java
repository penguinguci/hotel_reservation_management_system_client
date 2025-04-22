package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GenericDAO<T, ID> extends Remote {
    boolean create(T entity) throws RemoteException;
    boolean update(T entity) throws RemoteException;
    boolean delete(ID id) throws RemoteException;
    T findById(ID id) throws RemoteException;
    List<T> findAll() throws RemoteException;
}
