package interfaces;

import entities.Reservation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface ReservationDAO extends GenericDAO<Reservation, String>, Remote {
    boolean createHourlyReservation(Reservation reservation)throws RemoteException;
    List<Reservation> searchReservations(String keyword)throws RemoteException;
    List<Reservation> getReservationsByCustomerId(String customerId)throws RemoteException;
    boolean checkIn(String reservationId)throws RemoteException;
    boolean checkOut(String reservationId, Date actualCheckOutTime)throws RemoteException;
    boolean batchCheckIn(List<String> reservationIds)throws RemoteException;
    boolean batchCheckOut(List<String> reservationIds, Date actualCheckOutTime)throws RemoteException;
    boolean cancelReservation(String reservationId)throws RemoteException;
    boolean cancelMultipleReservations(List<Reservation> reservations)throws RemoteException;
    List<Reservation> getAllReservations()throws RemoteException;
}
