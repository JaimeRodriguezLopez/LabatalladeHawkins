import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EstadoHawkins implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int ninosHawkins;
    private final int ninosCallePrincipal;
    private final int ninosSotanoByers;
    private final int ninosRadioWSQK;

    private final int portalBosqueNormal;
    private final int portalBosqueUpsideDown;
    private final int portalLaboratorioNormal;
    private final int portalLaboratorioUpsideDown;
    private final int portalCentroNormal;
    private final int portalCentroUpsideDown;
    private final int portalAlcantarilladoNormal;
    private final int portalAlcantarilladoUpsideDown;

    private final int ninosBosque;
    private final int ninosLaboratorio;
    private final int ninosCentroComercial;
    private final int ninosAlcantarillado;
    private final int ninosColmena;

    private final int demogorgonsBosque;
    private final int demogorgonsLaboratorio;
    private final int demogorgonsCentroComercial;
    private final int demogorgonsAlcantarillado;
    private final int demogorgonsActivos;

    private final int sangreDisponible;
    private final String eventoActivo;
    private final boolean pausado;
    private final String[] rankingDemogorgons;
    private final String fechaActualizacion;

    public EstadoHawkins(
            int ninosHawkins,
            int ninosCallePrincipal,
            int ninosSotanoByers,
            int ninosRadioWSQK,
            int portalBosqueNormal,
            int portalBosqueUpsideDown,
            int portalLaboratorioNormal,
            int portalLaboratorioUpsideDown,
            int portalCentroNormal,
            int portalCentroUpsideDown,
            int portalAlcantarilladoNormal,
            int portalAlcantarilladoUpsideDown,
            int ninosBosque,
            int ninosLaboratorio,
            int ninosCentroComercial,
            int ninosAlcantarillado,
            int ninosColmena,
            int demogorgonsBosque,
            int demogorgonsLaboratorio,
            int demogorgonsCentroComercial,
            int demogorgonsAlcantarillado,
            int demogorgonsActivos,
            int sangreDisponible,
            String eventoActivo,
            boolean pausado,
            String[] rankingDemogorgons) {
        this.ninosHawkins = ninosHawkins;
        this.ninosCallePrincipal = ninosCallePrincipal;
        this.ninosSotanoByers = ninosSotanoByers;
        this.ninosRadioWSQK = ninosRadioWSQK;
        this.portalBosqueNormal = portalBosqueNormal;
        this.portalBosqueUpsideDown = portalBosqueUpsideDown;
        this.portalLaboratorioNormal = portalLaboratorioNormal;
        this.portalLaboratorioUpsideDown = portalLaboratorioUpsideDown;
        this.portalCentroNormal = portalCentroNormal;
        this.portalCentroUpsideDown = portalCentroUpsideDown;
        this.portalAlcantarilladoNormal = portalAlcantarilladoNormal;
        this.portalAlcantarilladoUpsideDown = portalAlcantarilladoUpsideDown;
        this.ninosBosque = ninosBosque;
        this.ninosLaboratorio = ninosLaboratorio;
        this.ninosCentroComercial = ninosCentroComercial;
        this.ninosAlcantarillado = ninosAlcantarillado;
        this.ninosColmena = ninosColmena;
        this.demogorgonsBosque = demogorgonsBosque;
        this.demogorgonsLaboratorio = demogorgonsLaboratorio;
        this.demogorgonsCentroComercial = demogorgonsCentroComercial;
        this.demogorgonsAlcantarillado = demogorgonsAlcantarillado;
        this.demogorgonsActivos = demogorgonsActivos;
        this.sangreDisponible = sangreDisponible;
        this.eventoActivo = eventoActivo;
        this.pausado = pausado;
        this.rankingDemogorgons = rankingDemogorgons;
        this.fechaActualizacion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public int getNinosHawkins() { return ninosHawkins; }
    public int getNinosCallePrincipal() { return ninosCallePrincipal; }
    public int getNinosSotanoByers() { return ninosSotanoByers; }
    public int getNinosRadioWSQK() { return ninosRadioWSQK; }
    public int getPortalBosqueNormal() { return portalBosqueNormal; }
    public int getPortalBosqueUpsideDown() { return portalBosqueUpsideDown; }
    public int getPortalLaboratorioNormal() { return portalLaboratorioNormal; }
    public int getPortalLaboratorioUpsideDown() { return portalLaboratorioUpsideDown; }
    public int getPortalCentroNormal() { return portalCentroNormal; }
    public int getPortalCentroUpsideDown() { return portalCentroUpsideDown; }
    public int getPortalAlcantarilladoNormal() { return portalAlcantarilladoNormal; }
    public int getPortalAlcantarilladoUpsideDown() { return portalAlcantarilladoUpsideDown; }
    public int getNinosBosque() { return ninosBosque; }
    public int getNinosLaboratorio() { return ninosLaboratorio; }
    public int getNinosCentroComercial() { return ninosCentroComercial; }
    public int getNinosAlcantarillado() { return ninosAlcantarillado; }
    public int getNinosColmena() { return ninosColmena; }
    public int getDemogorgonsBosque() { return demogorgonsBosque; }
    public int getDemogorgonsLaboratorio() { return demogorgonsLaboratorio; }
    public int getDemogorgonsCentroComercial() { return demogorgonsCentroComercial; }
    public int getDemogorgonsAlcantarillado() { return demogorgonsAlcantarillado; }
    public int getDemogorgonsActivos() { return demogorgonsActivos; }
    public int getSangreDisponible() { return sangreDisponible; }
    public String getEventoActivo() { return eventoActivo; }
    public boolean isPausado() { return pausado; }
    public String[] getRankingDemogorgons() { return rankingDemogorgons; }
    public String getFechaActualizacion() { return fechaActualizacion; }
}
