package interfaces;

import entities.Customer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CustomerDAO extends GenericDAO<Customer, String>, Remote {
    List<Customer> searchCustomerById(String id)  throws RemoteException;
    List<Customer> searchCustomersByPhone(String phone) throws RemoteException;
    List<Customer> searchCustomersByName(String name)throws RemoteException;
    List<Customer> searchCustomersByPhoneOrCCCD(String keyword)throws RemoteException;
    List<Customer> getAllCustomers()  throws RemoteException;
    Customer getCustomerByPhone(String phone) throws RemoteException;
    boolean isEmailExists(String email) throws RemoteException;
    boolean isPhoneExists(String phone)throws RemoteException;
    List<Customer> searchCustomerAdvanced(String id, String name, String phone, Boolean gender, String cccd)throws RemoteException;
    List<String> getAllCustomerIds() throws RemoteException;
    List<Customer> searchCustomers(String keyword)throws RemoteException;
}
