package interfaces;

import entities.PaymentMethod;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RevenueDAO extends  Remote {
    List<Double> getQuarterlyRevenue(int year)throws RemoteException;
    List<Double> getYearlyRevenue()throws RemoteException;
    double getRoomRevenueByDateRange1(Date startDate, Date endDate)throws RemoteException;
    double getTotalRevenueByDateRange(Date startDate, Date endDate)throws RemoteException;
    List<Double> getMonthlyRevenue(int year)throws RemoteException;
    double getTotalRevenue(int year)throws RemoteException;
    double getServiceRevenue(int year)throws RemoteException;
    double getRoomRevenue(int year)throws RemoteException;
    List<String> getRoomLabels(int year)throws RemoteException;
    double getTotalRevenueByMonth(int year, int month)throws RemoteException;
    double getServiceRevenueByMonth(int year, int month)throws RemoteException;
    double getRoomRevenueByMonth(int year, int month)throws RemoteException;
    double getTotalRevenueByQuarter(int year, int quarter)throws RemoteException;
    double getServiceRevenueByQuarter(int year, int quarter)throws RemoteException;
    double getRoomRevenueByQuarter(int year, int quarter)throws RemoteException;
    List<Integer> getAvailableYears()throws RemoteException;
    List<Double> getMonthlyTotalRevenue(int year)throws RemoteException;
    List<Double> getMonthlyRoomRevenue(int year)throws RemoteException;
    List<Double> getMonthlyServiceRevenue(int year)throws RemoteException;
    List<Double> getYearlyRoomRevenue()throws RemoteException;
    List<Double> getYearlyServiceRevenue()throws RemoteException;
    List<Double> getQuarterlyRoomRevenue(int year)throws RemoteException;
    List<Double> getQuarterlyServiceRevenue(int year)throws RemoteException;
    List<Double> getRevenueByDateRange(Date startDate, Date endDate)throws RemoteException;
    double getServiceRevenueByDateRange1(Date startDate, Date endDate)throws RemoteException;
    List<String> getDateRangeLabels(Date startDate, Date endDate)throws RemoteException;
    List<Double> getRoomRevenueByDateRange(Date startDate, Date endDate)throws RemoteException;
    List<Double> getServiceRevenueByDateRange(Date startDate, Date endDate)throws RemoteException;
    Map<Integer, Double> getRevenueByStatus(int year)throws RemoteException;
    Map<PaymentMethod, Double> getRevenueByPaymentMethod(int year)throws RemoteException;
    Map<String, Double> getFeeBreakdown(int year)throws RemoteException;
    List<Map<String, Double>> getMonthlyFeeBreakdown(int year)throws RemoteException;
}
