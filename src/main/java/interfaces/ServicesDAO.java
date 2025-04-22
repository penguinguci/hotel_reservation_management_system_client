package interfaces;

import entities.Service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServicesDAO extends GenericDAO<Service, String>, Remote {
    public List<Service> searchServices(String keyword, boolean availableOnly)throws RemoteException;
    public Service findServiceByID(int id)throws RemoteException;
    public List<Service> getAllServices()throws RemoteException;
    public List<Service> searchServicesByName(String keyword)throws RemoteException;
}
