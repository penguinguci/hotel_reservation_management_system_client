package interfaces;

import entities.Account;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AccountDAO extends GenericDAO<Account, String>, Remote {
    void deleteAccount(String username) throws RemoteException;
    void updateAccount(Account account) throws RemoteException;
    List<Account> getAllAccounts() throws RemoteException;
    Account getAccount(String username) throws RemoteException;
    void createAccount(Account account) throws RemoteException;
    Account findAccoutByStaffID(String staffID) throws RemoteException;
    boolean isUsernameExists(String username) throws RemoteException;
    public Account getAccountByEmail(String email) throws RemoteException;
}
