public class EventosAleatorios extends Thread {
 /*   .
• INTERVENCIÓN DE ELEVEN:
o Eleven usa sus poderes para liberar tantos niños capturados en la COLMENA como
unidades de sangre de Vecna hayan sido recolectadas en ese tiempo, y regresan a
la CALLE PRINCIPAL de Hawkins.
o Durante este evento, todos los demogorgons quedan paralizados (no atacan ni se
mueven) durante la duración del evento.
"""*/

    private volatile boolean TormentaUpsideDown=false;
    private volatile boolean LaRedMental=false;
    private volatile boolean IntervencionDeEleven=false;
    private volatile boolean ApagonLaboratorio=false;
    private Log log;
    private volatile String eventoActivo = "Sin evento activo";
    private volatile long finEventoMillis = 0;

    public EventosAleatorios(Log log) {
        this.log = log;
    }
    public boolean getLaRedMental() {
        return LaRedMental;
    }

    public void setLaRedMental(boolean laRedMental) {
        LaRedMental = laRedMental;
    }

    public boolean getIntervencionDeEleven() {
        return IntervencionDeEleven;
    }

    public void setIntervencionDeEleven(boolean intervencionDeEleven) {
        IntervencionDeEleven = intervencionDeEleven;
    }

    public boolean getApagonLaboratorio() {
        return ApagonLaboratorio;
    }

    public void setApagonLaboratorio(boolean apagonLaboratorio) {
        ApagonLaboratorio = apagonLaboratorio;
    }

    public void  setTormentaUpsideDown(boolean tormentaUpsideDown) {
        TormentaUpsideDown = tormentaUpsideDown;
    }
    public boolean getTormentaUpsideDown() {
        return TormentaUpsideDown;
    }

    public synchronized void iniciarEventoRemoto(String nombre, int duracionMs) {
        eventoActivo = nombre;
        finEventoMillis = System.currentTimeMillis() + duracionMs;
    }

    public synchronized void finalizarEventoRemoto() {
        eventoActivo = "Sin evento activo";
        finEventoMillis = 0;
    }

    public synchronized String getDescripcionEventoActual() {
        if (finEventoMillis <= 0 || System.currentTimeMillis() >= finEventoMillis) {
            return "Sin evento activo";
        }
        long segundos = Math.max(0, (finEventoMillis - System.currentTimeMillis()) / 1000);
        return eventoActivo + " (quedan aprox. " + segundos + " s)";
    }

    @Override
    public void run() {
        while (true) {
            try {
                ControlPausa.getInstance().esperarSiPausado();
                int CooldownEntreEventos = 30000 + (int) (Math.random() * (60000 - 30000 + 1));
                ControlPausa.getInstance().dormir(CooldownEntreEventos);
                int Evento = (int) (Math.random() * (4));
                int DuracionEvento = 5000 + (int) (Math.random() * (10000 - 5000 + 1));
                switch (Evento) {
                    case 0:
                        setTormentaUpsideDown(true);
                        iniciarEventoRemoto("TORMENTA DEL UPSIDE DOWN", DuracionEvento);
                        log.log("***EMPIEZA EL EVENTO TORMENTA DEL UPSIDE DOWN***");
                        ControlPausa.getInstance().dormir(DuracionEvento);
                        log.log("***TERMINA EL EVENTO TORMENTA DEL UPSIDE DOWN***");
                        setTormentaUpsideDown(false);
                        finalizarEventoRemoto();
                        break;
                    case 1:
                        setLaRedMental(true);
                        iniciarEventoRemoto("LA RED MENTAL", DuracionEvento);
                        log.log("***EMPIEZA EL EVENTO LA RED MENTAL***");
                        ControlPausa.getInstance().dormir(DuracionEvento);
                        log.log("***TERMINA EL EVENTO LA RED MENTAL***");
                        setLaRedMental(false);
                        finalizarEventoRemoto();
                        break;
                    case 2:
                        setIntervencionDeEleven(true);
                        iniciarEventoRemoto("INTERVENCION DE ELEVEN", DuracionEvento);
                        log.log("***EMPIEZA EL EVENTO INTERVENCION DE ELEVEN***");
                        ControlPausa.getInstance().dormir(DuracionEvento);
                        log.log("***TERMINA EL EVENTO INTERVENCION DE ELEVEN***");
                        setIntervencionDeEleven(false);
                        finalizarEventoRemoto();
                        break;
                    case 3:
                        setApagonLaboratorio(true);
                        iniciarEventoRemoto("APAGON DEL LABORATORIO", DuracionEvento);
                        log.log("***EMPIEZA EL EVENTO EL APAGON DEL LABORATORIO***");
                        ControlPausa.getInstance().dormir(DuracionEvento);
                        log.log("***TERMINA EL EVENTO EL APAGON DEL LABORATORIO***");
                        setApagonLaboratorio(false);
                        finalizarEventoRemoto();
                        break;
                }
            }catch (InterruptedException e){

            }
        }
    }


}
