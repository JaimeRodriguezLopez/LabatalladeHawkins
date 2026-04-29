import javax.swing.*;
import java.util.concurrent.Semaphore;

public class main {
    public static void main(String[] args) throws InterruptedException {
        Log log = Log.getInstance();
        log.log("***Empieza la batalla de hawkings.***");
        EventosAleatorios eventos = new EventosAleatorios(log);
        eventos.start();
        Lugares lugares = new Lugares(eventos,log);
        lugares.start();//Pongo a funcionar los portales
        Semaphore semaforo = new Semaphore(1);
        Demogorgon demogorgonAlpha = new Demogorgon(lugares,eventos,log,semaforo);
        demogorgonAlpha.start();
        log.log("El demogorgon Alpha D0000 ha sido creado");
        CrearNinos creadorDeNinos = new CrearNinos(lugares,eventos,log);
        creadorDeNinos.start();
        CrearDemogorgons creadorDeDemogorgons = new CrearDemogorgons(lugares,eventos,log,semaforo);
        creadorDeDemogorgons.start();
        SwingUtilities.invokeLater(() -> new SimulacionGUI(log, eventos,lugares,creadorDeNinos,creadorDeDemogorgons).setVisible(true));



        }

}