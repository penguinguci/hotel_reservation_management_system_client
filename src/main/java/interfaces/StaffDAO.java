package interfaces;

import entities.Staff;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface StaffDAO extends GenericDAO<Staff, String>, Remote {
    long countByPrefix(String prefix)throws RemoteException;
    boolean isEmailExists(String email)throws RemoteException;
    boolean isPhoneExists(String phone)throws RemoteException;
    List<Staff> searchStaffAdvanced(String id, String name, String phone, Boolean gender)throws RemoteException;
}
