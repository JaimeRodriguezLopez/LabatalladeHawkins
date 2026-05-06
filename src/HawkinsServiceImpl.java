import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HawkinsServiceImpl extends UnicastRemoteObject implements HawkinsService {
    private final Lugares lugares;
    private final EventosAleatorios eventos;
    private final ControlPausa controlPausa;
    private final Log log;

    public HawkinsServiceImpl(Lugares lugares, EventosAleatorios eventos, Log log) throws RemoteException {
        super();
        this.lugares = lugares;
        this.eventos = eventos;
        this.log = log;
        this.controlPausa = ControlPausa.getInstance();
    }

    @Override
    public EstadoHawkins getEstado() throws RemoteException {
        int calle = lugares.getCallePrincipal().size();
        int sotano = lugares.getSotanoByers().size();
        int radio = lugares.getRadioWSQK().size();

        int pBosqueNormal = lugares.getPortalNormalBosque().size();
        int pBosqueUD = lugares.getPortalUpsideDownBosque().size();
        int pLabNormal = lugares.getPortalNormalLaboratorio().size();
        int pLabUD = lugares.getPortalUpsideDownLaboratorio().size();
        int pCentroNormal = lugares.getPortalNormalCentroComercial().size();
        int pCentroUD = lugares.getPortalUpsideDownCentroComercial().size();
        int pAlcNormal = lugares.getPortalNormalAlcantarillado().size();
        int pAlcUD = lugares.getPortalUpsideDownAlcantarillado().size();

        int ninosHawkins = calle + sotano + radio + pBosqueNormal + pLabNormal + pCentroNormal + pAlcNormal;

        return new EstadoHawkins(
                ninosHawkins,
                calle,
                sotano,
                radio,
                pBosqueNormal,
                pBosqueUD,
                pLabNormal,
                pLabUD,
                pCentroNormal,
                pCentroUD,
                pAlcNormal,
                pAlcUD,
                lugares.getBosqueUpsideDown().size(),
                lugares.getLaboratorioUpsideDown().size(),
                lugares.getCentroComercialUpsideDown().size(),
                lugares.getAlcantarilladoUpsideDown().size(),
                lugares.getLaColmena().size(),
                lugares.getBosqueUpsideDownDemogorgon().size(),
                lugares.getLaboratorioUpsideDownDemogorgon().size(),
                lugares.getCentroComercialUpsideDownDemogorgon().size(),
                lugares.getAlcantarilladoUpsideDownDemogorgon().size(),
                lugares.getDemogorgonsActivos().size(),
                lugares.sangreAlmacenada.get(),
                eventos.getDescripcionEventoActual(),
                controlPausa.estaPausado(),
                obtenerRankingDemogorgons()
        );
    }

    @Override
    public void pausar() throws RemoteException {
        controlPausa.pausar();
        log.log("EJECUCION PAUSADA desde el cliente remoto");
    }

    @Override
    public void reanudar() throws RemoteException {
        controlPausa.reanudar();
        log.log("EJECUCION REANUDADA desde el cliente remoto");
    }

    @Override
    public boolean estaPausado() throws RemoteException {
        return controlPausa.estaPausado();
    }

    private String[] obtenerRankingDemogorgons() {
        List<Demogorgon> copia;
        synchronized (lugares.getDemogorgonsActivos()) {
            copia = new ArrayList<>(lugares.getDemogorgonsActivos());
        }

        return copia.stream()
                .sorted(Comparator.comparingInt(Demogorgon::getCapturas).reversed())
                .limit(3)
                .map(d -> d.getNombre() + " - " + d.getCapturas() + " capturas")
                .toArray(String[]::new);
    }
}
