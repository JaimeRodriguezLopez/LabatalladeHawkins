import java.util.concurrent.atomic.AtomicInteger;

public class CrearNinos extends Thread{
    public int NinosTotales=1500;
    public AtomicInteger contadorNinos=new AtomicInteger(0);
    public Lugares lugares;
    public EventosAleatorios eventos;
    public Log log;
    public CrearNinos(Lugares lugares,EventosAleatorios eventos,Log log) {
        this.lugares = lugares;
        this.eventos = eventos;
        this.log = log;
    }

    @Override
    public void run(){
        for (int i = 0; i < NinosTotales; i++) {
            //Logear que empiezan a crearse niños
            try {
                ControlPausa.getInstance().esperarSiPausado();
                String idNino = nextNinoId();
                Nino nino = new Nino(idNino,lugares,this.eventos,this.log);
                nino.start();
                log.log("El niño "+idNino+" se une a la batalla");
                int CooldownCreacionNinos = 500 + (int)(Math.random() * (2000 - 500 + 1));//Creacion del Cooldown
                ControlPausa.getInstance().dormir(CooldownCreacionNinos);
                //Logear que se ha creado un niño y su id.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

        }
        //Logear que todos los niños han sido creados
    }
    public String nextNinoId() {
        return String.format("N%04d", contadorNinos.getAndIncrement());
        //Devuelve un string con el formato deseado
    }

}
