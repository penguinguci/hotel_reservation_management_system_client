package interfaces;

import entities.Room;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RoomDAO extends GenericDAO<Room, String>, Remote {
    List<String> addAmenity(String roomId, String amenity)throws RemoteException;
    List<String> updateAmenity(String roomId, int amenityIndex, String newAmenity)throws RemoteException;
    List<Room> findByCriteria(Map<String, Object> criteria)throws RemoteException;
    List<Room> findAvailableRooms(Date checkInDate, Date checkOutDate, Integer capacity, String roomType, Double minPrice, Double maxPrice)throws RemoteException;
    boolean isRoomAvailable(String roomId, Date checkIn, Date checkOut)throws RemoteException;
    double calculateRoomPrice(String roomId, Date checkInDate, Date checkOutDate)throws RemoteException;
    List<Room> getAllRoomTypes()throws RemoteException;
    List<Room> getRoomsByStatus(int status)throws RemoteException;
    List<Room> getAllRooms()throws RemoteException;
    List<Integer> getAllFloors()throws RemoteException;
    boolean updateRoomStatus(String roomId, int status)throws RemoteException;
    List<Room> findAvailableRoomsForHourlyBooking(Date startTime, Date endTime, Integer capacity, String roomType)throws RemoteException;
    boolean isRoomAvailableForHourlyBooking(String roomId, Date startTime, Date endTime)throws RemoteException;
}
