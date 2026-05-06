import java.util.List;
import java.util.concurrent.Semaphore;

public class Demogorgon extends Thread {

    public String Nombre; //DXXXX
    public Lugares lugares;
    public EventosAleatorios eventos;
    public Log log;
    private int capturas=0;
    private Semaphore exclusionEscogerNinos;

    public Demogorgon(Lugares lugares,EventosAleatorios eventos,Log log,Semaphore exclusionEscogerNinos) {
        this.Nombre= "D0000";
        this.lugares=lugares;
        this.eventos = eventos;
        this.log = log;
        this.exclusionEscogerNinos = exclusionEscogerNinos;
        this.lugares.registrarDemogorgon(this);
    }
    public Demogorgon(String Nombre, Lugares lugares, EventosAleatorios eventos, Log log, Semaphore exclusionEscogerNinos) {
        this.Nombre = Nombre;
        this.lugares=lugares;
        this.eventos= eventos;
        this.log = log;
        this.exclusionEscogerNinos = exclusionEscogerNinos;
        this.lugares.registrarDemogorgon(this);
    }


    public String getNombre() {
        return Nombre;
    }

    public int getCapturas() {
        return capturas;
    }

    @Override
    public void run() {
        List<Demogorgon> destino=null;
        while (true) {
            try {
                ControlPausa.getInstance().esperarSiPausado();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            while(eventos.getIntervencionDeEleven()){
                try {
                    ControlPausa.getInstance().dormir(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                if(!eventos.getLaRedMental()){
                    if (!eventos.getApagonLaboratorio()){
                        int lugar = (int) (Math.random() * (4));

                        switch (lugar) {
                            case 0:
                                destino = lugares.getBosqueUpsideDownDemogorgon();
                                log.log("El demogorgon "+ Nombre+" se dirige al Bosque");
                                break;
                            case 1:
                                destino = lugares.getAlcantarilladoUpsideDownDemogorgon();
                                log.log("El demogorgon "+ Nombre+" se dirige al Alcantarillado");
                                break;
                            case 2:
                                destino = lugares.getCentroComercialUpsideDownDemogorgon();
                                log.log("El demogorgon "+ Nombre+" se dirige al Centro Comercial");
                                break;
                            case 3:
                                destino = lugares.getLaboratorioUpsideDownDemogorgon();
                                log.log("El demogorgon "+ Nombre+" se dirige al Laboratorio");
                                break;
                        }
                        }else{
                            if(destino ==  null){
                                destino = lugares.getLaboratorioUpsideDownDemogorgon();//si justo se inicializa el evento al crearse el demogorgon no tendra desino, entonces le asigno uno aleatorio
                            }
                            //sigue en el mismo destino
                        }
                }

                else{
                    List<Nino> max=null;
                    String mensaje=null;
                    if (lugares.getLaboratorioUpsideDown().size()>lugares.getAlcantarilladoUpsideDown().size()) {
                        max = lugares.getLaboratorioUpsideDown();
                        destino= lugares.getLaboratorioUpsideDownDemogorgon();
                        mensaje="El demogorgon "+ Nombre+" se dirige al Laboratorio";
                    }
                    else{
                        max = lugares.getAlcantarilladoUpsideDown();
                        destino = lugares.getAlcantarilladoUpsideDownDemogorgon();
                        mensaje = "El demogorgon "+ Nombre+" se dirige al Alcantarillado";
                    }
                    if(max.size()< lugares.getBosqueUpsideDown().size()){
                        max = lugares.getBosqueUpsideDown();
                        destino = lugares.getBosqueUpsideDownDemogorgon();
                        mensaje = "El demogorgon "+ Nombre+" se dirige al Bosque";

                    }
                    if(max.size()< lugares.getCentroComercialUpsideDown().size()){
                        destino = lugares.getCentroComercialUpsideDownDemogorgon();
                        mensaje= "El demogorgon "+ Nombre+" se dirige al Centro Comercial";

                    }
                    log.log(mensaje);
                }
                destino.add(this);
                List<Nino> victimas = lugares.getVictimas(destino);
                exclusionEscogerNinos.acquire();
                if(victimas.isEmpty()){
                    exclusionEscogerNinos.release();
                    int TiempoEnZona = 4000 + (int) (Math.random() * (5000 - 4000 + 1));
                    if(eventos.getTormentaUpsideDown()){
                        TiempoEnZona=TiempoEnZona/2;
                    }
                    ControlPausa.getInstance().dormir(TiempoEnZona);
                }else{

                    int numNinosEnZona = victimas.size();
                    int ninoVictimaEnArray = (int) (Math.random() * numNinosEnZona);
                        Nino ninoVictima = victimas.get(ninoVictimaEnArray);
                        victimas.remove(ninoVictimaEnArray);

                        int longitudAtaque = 500 + (int) (Math.random() * (1500 - 500 + 1));
                        if (eventos.getTormentaUpsideDown()) {
                            longitudAtaque = longitudAtaque / 2;
                        }
                    log.log("El demogorgon "+ Nombre+" ataca a niño "+ ninoVictima.getNombre()+".");
                    exclusionEscogerNinos.release();
                    if(!ninoVictima.sufreAtaque(longitudAtaque)){

                        int longitudPuestaANinoEnColmena= 500 + (int) (Math.random() * (1000 - 500 + 1));
                        ControlPausa.getInstance().dormir(longitudPuestaANinoEnColmena);
                        lugares.getLaColmena().add(ninoVictima);
                        lugares.acumuladorVictimas.incrementAndGet();
                        capturas++;
                        log.log("El ataque de "+Nombre+ " a "+ ninoVictima.getNombre()+" ha sido realizado correctamente.(Capturas: "+capturas+")");

                    }else {
                        victimas.add(ninoVictima);
                        log.log("El ataque de "+Nombre+ " a "+ ninoVictima.getNombre()+" ha fallado.(Capturas: "+capturas+")");
                    }
                }
            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            destino.remove(this);
        }
    }
}



