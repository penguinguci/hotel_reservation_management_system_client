package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GenericDAO<T, ID> extends Remote {
    boolean create(T entity) throws RemoteException;
    boolean update(T entity) throws RemoteException;
    boolean delete(ID id) throws RemoteException;
    T findById(ID id) throws RemoteException;
    List<T> findAll() throws RemoteException;

    /**
     * Đăng ký client để nhận thông báo khi dữ liệu thay đổi
     * @param callback Đối tượng callback từ client
     * @throws RemoteException Nếu có lỗi RMI
     */
    void registerClient(ClientCallback callback) throws RemoteException;

    /**
     * Hủy đăng ký client
     * @param callback Đối tượng callback từ client
     * @throws RemoteException Nếu có lỗi RMI
     */
    void unregisterClient(ClientCallback callback) throws RemoteException;

    /**
     * Thông báo cho tất cả clients khi dữ liệu thay đổi
     * @param dataType Loại dữ liệu thay đổi
     * @param changedData Dữ liệu được thay đổi
     * @throws RemoteException Nếu có lỗi RMI
     */
    void notifyAllClients(String dataType, Object changedData) throws RemoteException;
}
