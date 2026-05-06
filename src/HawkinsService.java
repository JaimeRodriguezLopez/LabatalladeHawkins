import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HawkinsService extends Remote {
    EstadoHawkins getEstado() throws RemoteException;
    void pausar() throws RemoteException;
    void reanudar() throws RemoteException;
    boolean estaPausado() throws RemoteException;
}
