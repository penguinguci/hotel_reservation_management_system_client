package interfaces;

import entities.Orders;
import entities.Reservation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface OrderDAO extends GenericDAO<Orders, String>, Remote {
    boolean createOrder(Orders order) throws RemoteException;
    List<Orders> getAllOrders() throws RemoteException;
    List<Orders> searchOrders(String orderId, String customerId, String staffId,
                              Date fromDate, Date toDate, Integer status,
                              Double priceFrom, Double priceTo)throws RemoteException;
    Orders getOrderDetails(String orderId)throws RemoteException;
    Reservation findReservationForOrder(Orders order)throws RemoteException;
}
