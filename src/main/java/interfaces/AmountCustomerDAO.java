package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface AmountCustomerDAO extends Remote {
    List<Integer> getAvailableYears() throws RemoteException;
    List<String> getDateRangeLabels(Date startDate, Date endDate) throws RemoteException;
    List<Integer> getCustomerCountByDateRange(Date startDate, Date endDate) throws RemoteException;
    int getTotalCustomerCountByDateRange(Date startDate, Date endDate) throws RemoteException;
    List<Integer> getMonthlyCustomerCount(int year)throws RemoteException;
    List<Integer> getQuarterlyCustomerCount(int year)throws RemoteException;
    List<Integer> getYearlyCustomerCount()throws RemoteException;
    int getTotalCustomerCountByQuarter(int year, int quarter)throws RemoteException;
    int getTotalCustomerCount(int year)throws RemoteException;
}
