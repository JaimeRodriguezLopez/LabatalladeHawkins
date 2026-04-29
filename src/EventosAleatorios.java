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

    @Override
    public void run() {
        while (true) {
            try {
                int CooldownEntreEventos = 30000 + (int) (Math.random() * (60000 - 30000 + 1));
                Thread.sleep(CooldownEntreEventos);
                int Evento = (int) (Math.random() * (4));
                int DuracionEvento = 5000 + (int) (Math.random() * (10000 - 5000 + 1));
                switch (Evento) {
                    case 0:
                        setTormentaUpsideDown(true);
                        log.log("***EMPIEZA EL EVENTO TORMENTA DEL UPSIDE DOWN***");
                        Thread.sleep(DuracionEvento);
                        log.log("***TERMINA EL EVENTO TORMENTA DEL UPSIDE DOWN***");
                        setTormentaUpsideDown(false);
                        break;
                    case 1:
                        setLaRedMental(true);
                        log.log("***EMPIEZA EL EVENTO LA RED MENTAL***");
                        Thread.sleep(DuracionEvento);
                        log.log("***TERMINA EL EVENTO LA RED MENTAL***");
                        setLaRedMental(false);
                        break;
                    case 2:
                        setIntervencionDeEleven(true);
                        log.log("***EMPIEZA EL EVENTO INTERVENCION DE ELEVEN***");
                        Thread.sleep(DuracionEvento);
                        log.log("***TERMINA EL EVENTO INTERVENCION DE ELEVEN***");
                        setIntervencionDeEleven(false);
                        break;
                    case 3:
                        setApagonLaboratorio(true);
                        log.log("***EMPIEZA EL EVENTO EL APAGON DEL LABORATORIO***");
                        Thread.sleep(DuracionEvento);
                        log.log("***TERMINA EL EVENTO EL APAGON DEL LABORATORIO***");
                        setApagonLaboratorio(false);
                        break;
                }
            }catch (InterruptedException e){

            }
        }
    }


}
