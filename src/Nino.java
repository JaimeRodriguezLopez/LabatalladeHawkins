import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Nino extends Thread {

    private String Nombre; //NXXXX
    private Boolean haEstadoEnColmena=false;
    private int Sangre=0;
    private final Random rng = new Random();
    private Lugares lugares;
    private LinkedBlockingQueue<Nino> portalAlUD;
    private List<Nino> destino;
    private EventosAleatorios eventosAleatorios;
    private Log log;

    public Nino(String Nombre,Lugares lugares,EventosAleatorios eventosAleatorios,Log log) {
        this.Nombre = Nombre;
        this.lugares = lugares;
        this.eventosAleatorios = eventosAleatorios;
        this.log = log;

    }
    public void setSangre(int Sangre) {
        this.Sangre = Sangre;
    }
    public int getSangre() {
        return Sangre;
    }

    public boolean sufreAtaque(long duracionMs) throws InterruptedException {
        ControlPausa.getInstance().dormir(duracionMs);
        boolean aguanta = rng.nextInt(3)!=1;
        if(haEstadoEnColmena){ return false; }
        else{
            if(!aguanta) {
                haEstadoEnColmena=true;
            }
        }return aguanta;
        /* Devuelve true si resiste el ataque 2/3 */
    }
    public String getNombre() {
        return Nombre;
    }

    @Override
    public void run() {
        while (true){
            try{
                ControlPausa.getInstance().esperarSiPausado();
                lugares.getCallePrincipal().add(this);//Inician en la calle principal de Hawkins
                log.log("El niño "+ Nombre +" ha llegado a la calle principal de Hawkings");
                CicloNino();
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }
    public void CicloNino() throws BrokenBarrierException, InterruptedException {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                ControlPausa.getInstance().esperarSiPausado();
                lugares.getCallePrincipal().remove(this);
                lugares.getSotanoByers().add(this);

                int CooldownPreparacion = 1000 + (int) (Math.random() * (2000 - 1000 + 1)); //Creación del Cooldown
                log.log("El niño "+ Nombre +" ha llegado al sotano byers, recibirá ordenes allí durante "+ CooldownPreparacion/1000+" segundos.");
                ControlPausa.getInstance().dormir(CooldownPreparacion); //Espera entre 1 y 2 segundos
                //Elección aleatoria de Portal
                int Portal = (int) (Math.random() * (4));//Selección Portal aleatorio
                switch (Portal) {//Espera a grupo deseado
                    case 0:
                        log.log("El niño "+Nombre+" irá hacia al bosque, esperando su equipo.");
                        lugares.getIrBosque().await();
                        portalAlUD = lugares.getPortalNormalBosque();
                        destino = lugares.getBosqueUpsideDown();
                        break;
                    case 1:
                        log.log("El niño "+Nombre+" irá hacia al centro comercial, esperando su equipo.");
                        lugares.getIrCentroComercial().await();
                        portalAlUD = lugares.getPortalNormalCentroComercial();
                        destino= lugares.getCentroComercialUpsideDown();
                        break;
                    case 2:
                        log.log("El niño "+Nombre+" irá hacia al laboratorio, esperando su equipo.");
                        lugares.getIrLaboratorio().await();
                        portalAlUD = lugares.getPortalNormalLaboratorio();
                        destino = lugares.getLaboratorioUpsideDown();
                        break;
                    case 3:
                        log.log("El niño "+Nombre+" irá hacia al alcantarillado, esperando su equipo.");
                        lugares.getIrAlcantarillado().await();
                        portalAlUD = lugares.getPortalNormalAlcantarillado();
                        destino = lugares.getAlcantarilladoUpsideDown();
                        break;
                }
                lugares.getSotanoByers().remove(this);
                portalAlUD.add(this);
                log.log("El niño "+Nombre+" ha llegado a su portal, esperando a que haya hueco para ir al upside down.");
                while(portalAlUD.contains(this)){
                    ControlPausa.getInstance().dormir(1);
                }

                destino.add(this);
                int TiempoEnUpsideDown = 3000 + (int) (Math.random() * (5000 - 3000 + 1));
                if(eventosAleatorios.getTormentaUpsideDown()){
                    TiempoEnUpsideDown = TiempoEnUpsideDown*2;
                }
                log.log("El niño "+Nombre+" ha pasado al upside down permanecerá allí "+ TiempoEnUpsideDown/1000 +" segundos.");
                ControlPausa.getInstance().dormir(TiempoEnUpsideDown);
                if(haEstadoEnColmena){
                    log.log("El niño "+ Nombre +" ha sido atrapado por un demogorgon, actualmente en la colmena");
                    while(lugares.getLaColmena().contains(this)){
                        ControlPausa.getInstance().dormir(1);
                    }
                    log.log("El niño "+ Nombre +" ha sido liberado de la colmena");
                }else {

                    LinkedBlockingQueue<Nino> portalAlMN = lugares.getPortalPareja(portalAlUD);
                    destino.remove(this);
                    portalAlMN.add(this);
                    log.log("El niño "+Nombre+" está en el portal del upside down, esperando a poder pasar");
                    while (portalAlMN.contains(this)) {
                        ControlPausa.getInstance().dormir(1);
                    }

                }
                log.log("El niño "+Nombre+" ha vuelto del upside down, reportará a radio WSQK");
                lugares.getRadioWSQK().add(this);
                if (!haEstadoEnColmena){
                    setSangre(1);
                    log.log("El niño "+Nombre+" deposita uno de sangre en el bote, llevan por ahora "+ lugares.sangreAlmacenada.addAndGet(1));
                }else{
                    setSangre(0);
                    log.log("El niño "+Nombre+" ha vuelto sin sangre");
                }
                haEstadoEnColmena=false;
                int TiempoWSQK = 2000 + (int) (Math.random() * (4000 - 2000 + 1));
                ControlPausa.getInstance().dormir(TiempoWSQK);
                lugares.getRadioWSQK().remove(this);
                lugares.getCallePrincipal().add(this);

                int TiempoCallePrincipal = 3000 + (int) (Math.random() * (5000 - 3000 + 1));
                log.log("El niño "+Nombre+" irá a la calle principal durante "+TiempoCallePrincipal/1000 +" segundos, para evitar sospechas del ejercito");
                ControlPausa.getInstance().dormir(TiempoCallePrincipal);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
