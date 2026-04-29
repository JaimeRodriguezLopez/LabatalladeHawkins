import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Lugares extends Thread{
    public EventosAleatorios eventos;
    public Log log;
    public Lugares(EventosAleatorios eventos, Log log) {
        this.eventos=eventos;
        this.log=log;
    }

    public AtomicInteger sangreAlmacenada=new AtomicInteger(0);
    public AtomicInteger acumuladorVictimas=new AtomicInteger(0);

    private final List<Nino> callePrincipal = Collections.synchronizedList(new ArrayList<>());
    private final List<Nino> sotanoByers = Collections.synchronizedList(new ArrayList<>());

    private final List<Nino> BosqueUpsideDown = Collections.synchronizedList(new ArrayList<>());
    private final List<Nino> LaboratorioUpsideDown = Collections.synchronizedList(new ArrayList<>());
    private final List<Nino> AlcantarilladoUpsideDown = Collections.synchronizedList(new ArrayList<>());
    private final List<Nino> CentroComercialUpsideDown = Collections.synchronizedList(new ArrayList<>());

    private final List<Demogorgon> BosqueUpsideDownDemogorgon = Collections.synchronizedList(new ArrayList<>());
    private final List<Demogorgon> LaboratorioUpsideDownDemogorgon = Collections.synchronizedList(new ArrayList<>());
    private final List<Demogorgon> AlcantarilladoUpsideDownDemogorgon = Collections.synchronizedList(new ArrayList<>());
    private final List<Demogorgon> CentroComercialUpsideDownDemogorgon = Collections.synchronizedList(new ArrayList<>());

    private final List<Nino> LaColmena = Collections.synchronizedList(new ArrayList<>());


    private final List<Nino> RadioWSQK = Collections.synchronizedList(new ArrayList<>());
    //Sincronización de niños en el sótano byers, solo continuan cuando han llegado al numero de integrantes necesarios
    private final CyclicBarrier irBosque = new CyclicBarrier(2);
    private final CyclicBarrier irLaboratorio= new CyclicBarrier(3);
    private final CyclicBarrier irCentroComercial= new CyclicBarrier(4);
    private final CyclicBarrier irAlcantarillado= new CyclicBarrier(2);

    private final LinkedBlockingQueue<Nino> portalNormalBosque = new LinkedBlockingQueue<>();//Capacidad máxima
    private final LinkedBlockingQueue<Nino> portalUpsideDownBosque = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Nino> portalNormalLaboratorio = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Nino> portalUpsideDownLaboratorio = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Nino> portalNormalCentroComercial = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Nino> portalUpsideDownCentroComercial = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Nino> portalNormalAlcantarillado = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Nino> portalUpsideDownAlcantarillado = new LinkedBlockingQueue<>();


    private final Semaphore portalAlcantarillaSemaphore = new Semaphore(1);
    private final Semaphore portalCentroComercialSemaphore = new Semaphore(1);
    private final Semaphore portalLaboratorioSemaphore = new Semaphore(1);
    private final Semaphore portalBosqueSemaphore = new Semaphore(1);

    public List<Demogorgon> getBosqueUpsideDownDemogorgon() {
        return BosqueUpsideDownDemogorgon;
    }

    public List<Demogorgon> getLaboratorioUpsideDownDemogorgon() {
        return LaboratorioUpsideDownDemogorgon;
    }

    public List<Demogorgon> getAlcantarilladoUpsideDownDemogorgon() {
        return AlcantarilladoUpsideDownDemogorgon;
    }

    public List<Demogorgon> getCentroComercialUpsideDownDemogorgon() {
        return CentroComercialUpsideDownDemogorgon;
    }
    public List<Nino> getRadioWSQK() {
        return RadioWSQK;
    }
    public Semaphore getPortalAlcantarillaSemaphore() {
        return portalAlcantarillaSemaphore;
    }

    public Semaphore getPortalCentroComercialSemaphore() {
        return portalCentroComercialSemaphore;
    }

    public Semaphore getPortalLaboratorioSemaphore() {
        return portalLaboratorioSemaphore;
    }

    public Semaphore getPortalBosqueSemaphore() {
        return portalBosqueSemaphore;
    }

    public List<Nino> getLaboratorioUpsideDown() {
        return LaboratorioUpsideDown;
    }

    public List<Nino> getCentroComercialUpsideDown() {
        return CentroComercialUpsideDown;
    }

    public List<Nino> getAlcantarilladoUpsideDown() {
        return AlcantarilladoUpsideDown;
    }

    public List<Nino> getBosqueUpsideDown() {
        return BosqueUpsideDown;
    }

    public LinkedBlockingQueue<Nino> getPortalNormalCentroComercial() {
        return portalNormalCentroComercial;
    }

    public LinkedBlockingQueue<Nino> getPortalNormalBosque() {
        return portalNormalBosque;
    }

    public LinkedBlockingQueue<Nino> getPortalUpsideDownBosque() {
        return portalUpsideDownBosque;
    }

    public LinkedBlockingQueue<Nino> getPortalNormalLaboratorio() {
        return portalNormalLaboratorio;
    }

    public LinkedBlockingQueue<Nino> getPortalUpsideDownLaboratorio() {
        return portalUpsideDownLaboratorio;
    }

    public LinkedBlockingQueue<Nino> getPortalUpsideDownCentroComercial() {
        return portalUpsideDownCentroComercial;
    }

    public LinkedBlockingQueue<Nino> getPortalNormalAlcantarillado() {
        return portalNormalAlcantarillado;
    }

    public LinkedBlockingQueue<Nino> getPortalUpsideDownAlcantarillado() {
        return portalUpsideDownAlcantarillado;
    }

    public List<Nino> getCallePrincipal() {
        return callePrincipal;
    }

    public List<Nino> getSotanoByers() {
        return sotanoByers;
    }

    public CyclicBarrier getIrBosque() {
        return irBosque;
    }

    public CyclicBarrier getIrLaboratorio() {
        return irLaboratorio;
    }

    public CyclicBarrier getIrCentroComercial() {
        return irCentroComercial;
    }

    public CyclicBarrier getIrAlcantarillado() {
        return irAlcantarillado;
    }

    public List<Nino> getLaColmena() {
        return LaColmena;
    }

    public void takeConDelay(LinkedBlockingQueue<Nino> queue) throws InterruptedException {
        Semaphore semaphore = getSemaphoreAdecuado(queue);
        if (queue == getPortalNormalAlcantarillado()|| queue == getPortalNormalLaboratorio()||queue == getPortalNormalCentroComercial() ||queue == getPortalNormalBosque()){
            if (getPortalPareja(queue).isEmpty()){
                try{
                    semaphore.acquire();
                    queue.poll(1, TimeUnit.SECONDS);
                    semaphore.release();
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }

        }else{

            try{
                semaphore.acquire();
                queue.poll(1, TimeUnit.SECONDS);
                semaphore.release();
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }

        }
    }

    public LinkedBlockingQueue<Nino> getPortalPareja(LinkedBlockingQueue<Nino> portal) {
        if (portal == getPortalNormalAlcantarillado()) {
            return getPortalUpsideDownAlcantarillado();
        }
        else if (portal == getPortalUpsideDownAlcantarillado()) {
            return getPortalNormalAlcantarillado();
        }
        else if (portal == getPortalUpsideDownCentroComercial()) {
            return getPortalNormalCentroComercial();
        }
        else if (portal == getPortalNormalCentroComercial()) {
            return getPortalUpsideDownCentroComercial();
        }
        else if (portal == getPortalUpsideDownBosque()) {
            return getPortalNormalBosque();
        }
        else if (portal == getPortalNormalBosque()) {
            return getPortalUpsideDownBosque();
        }
        else if (portal == getPortalUpsideDownLaboratorio()) {
            return getPortalNormalLaboratorio();
        }
        else {
            return getPortalUpsideDownLaboratorio();
        }
    }
    public Semaphore getSemaphoreAdecuado(LinkedBlockingQueue<Nino> queue) {
        if (queue == getPortalNormalAlcantarillado()|| queue== getPortalUpsideDownAlcantarillado()) {
            return getPortalAlcantarillaSemaphore();
        }else if (queue == getPortalNormalCentroComercial()|| queue== getPortalUpsideDownCentroComercial()) {
            return getPortalCentroComercialSemaphore();
        } else if (queue == getPortalNormalBosque()||  queue== getPortalUpsideDownBosque())  {
            return getPortalBosqueSemaphore();
        }else{
            return getPortalLaboratorioSemaphore();
        }
    }

    public List<Nino> getVictimas(List<Demogorgon> pareja){
        if (pareja == getAlcantarilladoUpsideDownDemogorgon()){
            return  getAlcantarilladoUpsideDown();
        } else if (pareja== getCentroComercialUpsideDownDemogorgon()) {
            return  getCentroComercialUpsideDown();
        } else if (pareja == getLaboratorioUpsideDownDemogorgon()) {
            return  getLaboratorioUpsideDown();
        }else{
            return getBosqueUpsideDown();
        }
    }
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if(!eventos.getApagonLaboratorio()) {
                    // Bosque
                    moverParDePortales(portalNormalBosque, portalUpsideDownBosque);
                    // Laboratorio
                    moverParDePortales(portalNormalLaboratorio, portalUpsideDownLaboratorio);
                    // Centro comercial
                    moverParDePortales(portalNormalCentroComercial, portalUpsideDownCentroComercial);
                    // Alcantarillado
                    moverParDePortales(portalNormalAlcantarillado, portalUpsideDownAlcantarillado);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (eventos.getIntervencionDeEleven()) {
                int ninosALiberar = sangreAlmacenada.get();
                int j = Math.min(ninosALiberar, getLaColmena().size());
                if (j > 0) {
                    sangreAlmacenada.addAndGet(-j);
                    for (int i = 0; i < j; i++) {
                        Nino n = getLaColmena().removeFirst();

                    }
                    log.log("Eleven ha liberado a " + j + " niños");
                }
            }
        }
    }

    // Prioridad: primero los que vuelven a Hawkins (UpsideDown -> Normal)
    private void moverParDePortales(LinkedBlockingQueue portalNormal,
                                    LinkedBlockingQueue portalUpside) throws InterruptedException {

        if (!portalUpside.isEmpty()) {
            // Niños que vuelven a Hawkins
            moverUnNino(portalUpside);
        } else if (!portalNormal.isEmpty()) {
            // Niños que van al UpsideDown
            moverUnNino(portalNormal);
        }
    }

    private void moverUnNino(LinkedBlockingQueue origen) throws InterruptedException {
        Semaphore sem = getSemaphoreAdecuado(origen);

        if (!sem.tryAcquire()) {
            return; // otro niño está cruzando este portal
        }

        try {
            Object o = origen.poll(1, TimeUnit.MILLISECONDS);
            if (o == null) {
                return;
            }

            // tiempo de cruce ~1 segundo
            Thread.sleep(1000);
        } finally {
            sem.release();
        }
    }


}
