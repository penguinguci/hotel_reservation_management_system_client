package interfaces;

import entities.RoomType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RoomTypesDAO extends GenericDAO<RoomType, String>, Remote {
    List<RoomType> getAllRoomTypes()throws RemoteException;
}
