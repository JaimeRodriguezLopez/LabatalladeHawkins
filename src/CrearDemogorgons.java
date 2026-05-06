import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class CrearDemogorgons extends Thread {
    private Lugares lugares;
    public AtomicInteger contadorDemogorgons = new AtomicInteger(1);
    public EventosAleatorios eventos;
    public Log log;
    Semaphore semaforo = new Semaphore(1);


    public CrearDemogorgons(Lugares lugares,EventosAleatorios eventos, Log log, Semaphore semaforo) {
        this.lugares = lugares;
        this.eventos = eventos;
        this.log = log;
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ControlPausa.getInstance().esperarSiPausado();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            if(lugares.acumuladorVictimas.get()>=8){
                String demogorgonId = nextDemogorgonId();
                Demogorgon demogorgon = new Demogorgon(demogorgonId,this.lugares, this.eventos, this.log, this.semaforo);
                lugares.acumuladorVictimas.addAndGet(-8);
                log.log("El demogorgon"+demogorgonId+" se une a la batalla" );
                demogorgon.start();
            }
            try {
                ControlPausa.getInstance().dormir(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }




    public String nextDemogorgonId() {
        return String.format("D%04d", contadorDemogorgons.getAndIncrement());
        //Devuelve un string con el formato deseado
    }

}
