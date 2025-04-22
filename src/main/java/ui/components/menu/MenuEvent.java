/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.components.menu;

import java.rmi.RemoteException;

public interface MenuEvent {
    public void selected(int index, int subIndex) throws RemoteException;
}
