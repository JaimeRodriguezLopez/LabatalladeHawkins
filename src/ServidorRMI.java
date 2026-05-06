import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServidorRMI {
    public static final String NOMBRE_SERVICIO = "HawkinsService";
    public static final int PUERTO = 1099;

    private ServidorRMI() {
    }

    public static void iniciar(Lugares lugares, EventosAleatorios eventos, Log log) {
        try {
            try {
                LocateRegistry.createRegistry(PUERTO);
                log.log("Registro RMI iniciado en el puerto " + PUERTO);
            } catch (Exception e) {
                log.log("El registro RMI ya estaba iniciado o no se pudo crear: " + e.getMessage());
            }

            HawkinsService servicio = new HawkinsServiceImpl(lugares, eventos, log);
            Naming.rebind("rmi://localhost:" + PUERTO + "/" + NOMBRE_SERVICIO, servicio);
            log.log("Servidor RMI disponible en rmi://localhost:" + PUERTO + "/" + NOMBRE_SERVICIO);
        } catch (Exception e) {
            log.log("ERROR iniciando servidor RMI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
