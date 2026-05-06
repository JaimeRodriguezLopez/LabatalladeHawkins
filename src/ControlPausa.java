public class ControlPausa {
    private static final ControlPausa INSTANCE = new ControlPausa();
    private boolean pausado = false;

    private ControlPausa() {
    }

    public static ControlPausa getInstance() {
        return INSTANCE;
    }

    public synchronized void pausar() {
        pausado = true;
    }

    public synchronized void reanudar() {
        pausado = false;
        notifyAll();
    }

    public synchronized boolean estaPausado() {
        return pausado;
    }

    public synchronized void esperarSiPausado() throws InterruptedException {
        while (pausado) {
            wait();
        }
    }

    public void dormir(long milisegundos) throws InterruptedException {
        long fin = System.currentTimeMillis() + milisegundos;
        while (true) {
            esperarSiPausado();
            long restante = fin - System.currentTimeMillis();
            if (restante <= 0) {
                return;
            }
            Thread.sleep(Math.min(100, restante));
        }
    }
}
