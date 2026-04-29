import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * SimulacionGUI.java — Interfaz Swing para "La Batalla de Hawkins"
 *
 * Diseño simple y claro: fondo blanco, paneles bien delimitados,
 * actualización automática cada 500 ms.
 *
 * Requiere que Log.java tenga setGuiCallback() (ya incluido en el Log entregado).
 */
public class SimulacionGUI extends JFrame {

    // ── Dependencias de simulación ────────────────────────────────────────
    private final Log log;
    private final EventosAleatorios eventos;
    private final Lugares lugares;
    private final CrearNinos creadorNinos;
    private final CrearDemogorgons creadorDemogorgons;

    // ── Colores sencillos ─────────────────────────────────────────────────
    private static final Color C_BG        = new Color(245, 245, 248);
    private static final Color C_HAWKINS   = new Color(220, 235, 255);
    private static final Color C_UPSIDE    = new Color(255, 220, 220);
    private static final Color C_PORTALES  = new Color(220, 255, 230);
    private static final Color C_EVENTO    = new Color(255, 243, 180);
    private static final Color C_COLMENA   = new Color(255, 200, 200);
    private static final Color C_HEADER    = new Color(50, 50, 80);
    private static final Color C_EVENT_ON  = new Color(220, 60, 60);
    private static final Color C_EVENT_OFF = new Color(160, 160, 160);
    private static final Font  F_TITLE     = new Font("SansSerif", Font.BOLD, 13);
    private static final Font  F_NORMAL    = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font  F_BIG       = new Font("SansSerif", Font.BOLD, 22);
    private static final Font  F_MONO      = new Font("Monospaced", Font.PLAIN, 11);

    // ── Labels dinámicos ──────────────────────────────────────────────────
    // Resumen global
    private JLabel lblNinosCreados, lblDemogorgonsActivos, lblSangre, lblVictimas;

    // Hawkins
    private JLabel lblCalle, lblSotano, lblRadio;

    // Portales (Normal→UD | UD→Normal)
    private JLabel lblPBN, lblPBUD;   // Bosque
    private JLabel lblPLN, lblPLUD;   // Laboratorio
    private JLabel lblPCN, lblPCUD;   // Centro Comercial
    private JLabel lblPAN, lblPAUD;   // Alcantarillado

    // Upside Down
    private JLabel lblBosqueNinos, lblLabNinos, lblCentroNinos, lblAlcantNinos;
    private JLabel lblBosqueDem,   lblLabDem,   lblCentroDem,   lblAlcantDem;
    private JLabel lblColmena;

    // Eventos
    private JLabel lblEvTormenta, lblEvRed, lblEvEleven, lblEvApagon;
    private JLabel lblEventoActivo;

    // Log
    private JTextArea areaLog;

    // Timer de refresco
    private Timer timerRefresh;

    // ─────────────────────────────────────────────────────────────────────
    public SimulacionGUI(Log log, EventosAleatorios eventos, Lugares lugares,
                         CrearNinos creadorNinos, CrearDemogorgons creadorDemogorgons) {
        super("La Batalla de Hawkins — Simulación Concurrente");
        this.log = log;
        this.eventos = eventos;
        this.lugares = lugares;
        this.creadorNinos = creadorNinos;
        this.creadorDemogorgons = creadorDemogorgons;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 760);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_BG);
        setLayout(new BorderLayout(6, 6));

        add(buildHeader(),   BorderLayout.NORTH);
        add(buildMain(),     BorderLayout.CENTER);
        add(buildLogPanel(), BorderLayout.SOUTH);

        // Conectar log a la GUI
        Log.setGuiCallback(msg -> SwingUtilities.invokeLater(() -> appendLog(msg)));

        // Refresco automático
        timerRefresh = new Timer(500, e -> refreshStats());
        timerRefresh.start();
    }

    // ════════════════════════════════════════════════════════════════════
    // CABECERA — resumen global
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildHeader() {
        JPanel p = new JPanel(new GridLayout(1, 4, 8, 0));
        p.setBackground(C_HEADER);
        p.setBorder(new EmptyBorder(10, 14, 10, 14));

        lblNinosCreados      = bigStat("Niños creados", "0",   new Color(100, 180, 255));
        lblDemogorgonsActivos = bigStat("Demogorgons",   "0",   new Color(255, 120, 120));
        lblSangre            = bigStat("Sangre Vecna",  "0",   new Color(255, 220, 80));
        lblVictimas          = bigStat("En Colmena",    "0",   new Color(220, 100, 255));

        p.add(wrapStat("NIÑOS CREADOS",   lblNinosCreados,       new Color(100, 180, 255)));
        p.add(wrapStat("DEMOGORGONS",      lblDemogorgonsActivos, new Color(255, 120, 120)));
        p.add(wrapStat("SANGRE DE VECNA", lblSangre,             new Color(255, 220, 80)));
        p.add(wrapStat("EN COLMENA",      lblVictimas,           new Color(220, 100, 255)));
        return p;
    }

    private JLabel bigStat(String name, String val, Color c) {
        JLabel l = new JLabel(val, SwingConstants.CENTER);
        l.setFont(F_BIG);
        l.setForeground(c);
        return l;
    }

    private JPanel wrapStat(String titulo, JLabel valor, Color c) {
        JPanel p = new JPanel(new BorderLayout(2, 2));
        p.setOpaque(false);
        JLabel t = new JLabel(titulo, SwingConstants.CENTER);
        t.setFont(new Font("SansSerif", Font.BOLD, 10));
        t.setForeground(new Color(180, 180, 200));
        p.add(t, BorderLayout.NORTH);
        p.add(valor, BorderLayout.CENTER);
        return p;
    }

    // ════════════════════════════════════════════════════════════════════
    // PANEL PRINCIPAL — 3 columnas
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildMain() {
        JPanel p = new JPanel(new GridLayout(1, 3, 8, 0));
        p.setBackground(C_BG);
        p.setBorder(new EmptyBorder(6, 10, 6, 10));
        p.add(buildHawkinsCol());
        p.add(buildPortalesCol());
        p.add(buildUpsideDownCol());
        return p;
    }

    // ── Columna izquierda: Hawkins + Eventos ─────────────────────────────
    private JPanel buildHawkinsCol() {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBackground(C_BG);

        // Hawkins
        JPanel pHawkins = section("HAWKINS — MUNDO NORMAL", C_HAWKINS);
        lblCalle  = rowLabel("Calle Principal:");
        lblSotano = rowLabel("Sótano Byers:");
        lblRadio  = rowLabel("Radio WSQK:");
        addRows(pHawkins,
                labelRow("Calle Principal:", lblCalle),
                labelRow("Sótano Byers:",    lblSotano),
                labelRow("Radio WSQK:",      lblRadio));
        col.add(pHawkins);
        col.add(Box.createVerticalStrut(8));

        // Eventos
        JPanel pEv = section("EVENTO GLOBAL ACTIVO", C_EVENTO);
        lblEventoActivo = new JLabel("Sin evento activo", SwingConstants.CENTER);
        lblEventoActivo.setFont(F_TITLE);
        lblEventoActivo.setForeground(C_EVENT_OFF);
        lblEventoActivo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel grid = new JPanel(new GridLayout(2, 2, 4, 4));
        grid.setOpaque(false);
        lblEvTormenta = evLabel("🌩 Tormenta UD");
        lblEvRed      = evLabel("🧠 Red Mental");
        lblEvEleven   = evLabel("✨ Eleven");
        lblEvApagon   = evLabel("🔌 Apagón Lab.");
        grid.add(lblEvTormenta);
        grid.add(lblEvRed);
        grid.add(lblEvEleven);
        grid.add(lblEvApagon);

        pEv.add(lblEventoActivo);
        pEv.add(Box.createVerticalStrut(6));
        pEv.add(grid);
        col.add(pEv);
        col.add(Box.createVerticalGlue());
        return col;
    }

    // ── Columna central: Portales ─────────────────────────────────────────
    private JPanel buildPortalesCol() {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBackground(C_BG);

        JPanel pPort = section("PORTALES  (un niño a la vez)", C_PORTALES);

        // Encabezado de tabla
        JPanel hdr = new JPanel(new GridLayout(1, 3, 4, 0));
        hdr.setOpaque(false);
        hdr.add(headerCell("ZONA"));
        hdr.add(headerCell("→ UD (Normal)"));
        hdr.add(headerCell("← HW (UD)"));
        hdr.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        pPort.add(hdr);
        pPort.add(Box.createVerticalStrut(4));

        lblPBN  = valLabel("0"); lblPBUD  = valLabel("0");
        lblPLN  = valLabel("0"); lblPLUD  = valLabel("0");
        lblPCN  = valLabel("0"); lblPCUD  = valLabel("0");
        lblPAN  = valLabel("0"); lblPAUD  = valLabel("0");

        pPort.add(portalRow("Bosque",          lblPBN,  lblPBUD));
        pPort.add(Box.createVerticalStrut(3));
        pPort.add(portalRow("Laboratorio",     lblPLN,  lblPLUD));
        pPort.add(Box.createVerticalStrut(3));
        pPort.add(portalRow("Centro Comercial",lblPCN,  lblPCUD));
        pPort.add(Box.createVerticalStrut(3));
        pPort.add(portalRow("Alcantarillado",  lblPAN,  lblPAUD));

        col.add(pPort);
        col.add(Box.createVerticalGlue());
        return col;
    }

    // ── Columna derecha: Upside Down ──────────────────────────────────────
    private JPanel buildUpsideDownCol() {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBackground(C_BG);

        JPanel pUD = section("UPSIDE DOWN", C_UPSIDE);

        // Encabezado de tabla
        JPanel hdr = new JPanel(new GridLayout(1, 3, 4, 0));
        hdr.setOpaque(false);
        hdr.add(headerCell("ZONA"));
        hdr.add(headerCell("Niños 🧒"));
        hdr.add(headerCell("Demogorgons 👾"));
        hdr.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        pUD.add(hdr);
        pUD.add(Box.createVerticalStrut(4));

        lblBosqueNinos  = valLabel("0"); lblBosqueDem  = valLabel("0");
        lblLabNinos     = valLabel("0"); lblLabDem     = valLabel("0");
        lblCentroNinos  = valLabel("0"); lblCentroDem  = valLabel("0");
        lblAlcantNinos  = valLabel("0"); lblAlcantDem  = valLabel("0");

        pUD.add(zonaRow("Bosque",           lblBosqueNinos, lblBosqueDem));
        pUD.add(Box.createVerticalStrut(3));
        pUD.add(zonaRow("Laboratorio",      lblLabNinos,    lblLabDem));
        pUD.add(Box.createVerticalStrut(3));
        pUD.add(zonaRow("Centro Comercial", lblCentroNinos, lblCentroDem));
        pUD.add(Box.createVerticalStrut(3));
        pUD.add(zonaRow("Alcantarillado",   lblAlcantNinos, lblAlcantDem));

        // Colmena
        pUD.add(Box.createVerticalStrut(10));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        pUD.add(sep);
        pUD.add(Box.createVerticalStrut(6));

        lblColmena = new JLabel("COLMENA: 0 niños capturados", SwingConstants.CENTER);
        lblColmena.setFont(F_TITLE);
        lblColmena.setForeground(new Color(180, 40, 40));
        lblColmena.setAlignmentX(Component.CENTER_ALIGNMENT);
        pUD.add(lblColmena);

        col.add(pUD);
        col.add(Box.createVerticalGlue());
        return col;
    }

    // ════════════════════════════════════════════════════════════════════
    // PANEL DE LOG
    // ════════════════════════════════════════════════════════════════════
    private JPanel buildLogPanel() {
        JPanel p = new JPanel(new BorderLayout(4, 4));
        p.setBackground(C_BG);
        p.setBorder(new EmptyBorder(0, 10, 8, 10));

        JLabel titulo = new JLabel("REGISTRO DE EVENTOS (hawkins.txt)");
        titulo.setFont(F_TITLE);
        titulo.setForeground(C_HEADER);

        areaLog = new JTextArea(7, 80);
        areaLog.setFont(F_MONO);
        areaLog.setEditable(false);
        areaLog.setBackground(new Color(30, 30, 40));
        areaLog.setForeground(new Color(180, 255, 180));
        areaLog.setCaretColor(Color.WHITE);
        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 100), 1));

        p.add(titulo, BorderLayout.NORTH);
        p.add(scroll,  BorderLayout.CENTER);
        return p;
    }

    // ════════════════════════════════════════════════════════════════════
    // REFRESCO DE DATOS
    // ════════════════════════════════════════════════════════════════════
    private void refreshStats() {
        // Resumen global
        int ninos = creadorNinos.contadorNinos.get();
        int demos  = creadorDemogorgons.contadorDemogorgons.get();
        lblNinosCreados.setText(String.valueOf(ninos));
        lblDemogorgonsActivos.setText(String.valueOf(demos));
        lblSangre.setText(String.valueOf(lugares.sangreAlmacenada.get()));
        int colmena = lugares.getLaColmena().size();
        lblVictimas.setText(String.valueOf(colmena));

        // Hawkins
        lblCalle.setText(String.valueOf(lugares.getCallePrincipal().size()));
        lblSotano.setText(String.valueOf(lugares.getSotanoByers().size()));
        lblRadio.setText(String.valueOf(lugares.getRadioWSQK().size()));

        // Portales
        lblPBN.setText(String.valueOf(lugares.getPortalNormalBosque().size()));
        lblPBUD.setText(String.valueOf(lugares.getPortalUpsideDownBosque().size()));
        lblPLN.setText(String.valueOf(lugares.getPortalNormalLaboratorio().size()));
        lblPLUD.setText(String.valueOf(lugares.getPortalUpsideDownLaboratorio().size()));
        lblPCN.setText(String.valueOf(lugares.getPortalNormalCentroComercial().size()));
        lblPCUD.setText(String.valueOf(lugares.getPortalUpsideDownCentroComercial().size()));
        lblPAN.setText(String.valueOf(lugares.getPortalNormalAlcantarillado().size()));
        lblPAUD.setText(String.valueOf(lugares.getPortalUpsideDownAlcantarillado().size()));

        // Upside Down
        lblBosqueNinos.setText(String.valueOf(lugares.getBosqueUpsideDown().size()));
        lblLabNinos.setText(String.valueOf(lugares.getLaboratorioUpsideDown().size()));
        lblCentroNinos.setText(String.valueOf(lugares.getCentroComercialUpsideDown().size()));
        lblAlcantNinos.setText(String.valueOf(lugares.getAlcantarilladoUpsideDown().size()));
        lblBosqueDem.setText(String.valueOf(lugares.getBosqueUpsideDownDemogorgon().size()));
        lblLabDem.setText(String.valueOf(lugares.getLaboratorioUpsideDownDemogorgon().size()));
        lblCentroDem.setText(String.valueOf(lugares.getCentroComercialUpsideDownDemogorgon().size()));
        lblAlcantDem.setText(String.valueOf(lugares.getAlcantarilladoUpsideDownDemogorgon().size()));

        // Colmena
        lblColmena.setText("COLMENA: " + colmena + " niño(s) capturado(s)");
        lblColmena.setForeground(colmena > 0 ? new Color(180, 40, 40) : new Color(100, 140, 100));

        // Eventos
        boolean tormenta = eventos.getTormentaUpsideDown();
        boolean red      = eventos.getLaRedMental();
        boolean eleven   = eventos.getIntervencionDeEleven();
        boolean apagon   = eventos.getApagonLaboratorio();

        setEvento(lblEvTormenta, tormenta);
        setEvento(lblEvRed,      red);
        setEvento(lblEvEleven,   eleven);
        setEvento(lblEvApagon,   apagon);

        if (tormenta)      { lblEventoActivo.setText("🌩 TORMENTA DEL UPSIDE DOWN"); lblEventoActivo.setForeground(new Color(200, 120, 0)); }
        else if (red)      { lblEventoActivo.setText("🧠 LA RED MENTAL");            lblEventoActivo.setForeground(new Color(100, 0, 180)); }
        else if (eleven)   { lblEventoActivo.setText("✨ INTERVENCIÓN DE ELEVEN");   lblEventoActivo.setForeground(new Color(0, 140, 200)); }
        else if (apagon)   { lblEventoActivo.setText("🔌 APAGÓN DEL LABORATORIO");  lblEventoActivo.setForeground(new Color(180, 40, 40)); }
        else               { lblEventoActivo.setText("Sin evento activo");            lblEventoActivo.setForeground(C_EVENT_OFF); }
    }

    private void setEvento(JLabel lbl, boolean activo) {
        lbl.setForeground(activo ? C_EVENT_ON : C_EVENT_OFF);
        lbl.setFont(activo
                ? new Font("SansSerif", Font.BOLD, 11)
                : new Font("SansSerif", Font.PLAIN, 11));
    }

    private void appendLog(String msg) {
        areaLog.append(msg + "\n");
        // Limitar a 500 líneas
        if (areaLog.getLineCount() > 520) {
            try {
                int end = areaLog.getLineEndOffset(areaLog.getLineCount() - 501);
                areaLog.getDocument().remove(0, end);
            } catch (Exception ignored) {}
        }
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    // ════════════════════════════════════════════════════════════════════
    // CONSTRUCTORES DE COMPONENTES AUXILIARES
    // ════════════════════════════════════════════════════════════════════

    /** Panel con título y borde coloreado, BoxLayout vertical */
    private JPanel section(String titulo, Color bg) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(bg);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        JLabel t = new JLabel(titulo);
        t.setFont(F_TITLE);
        t.setForeground(C_HEADER);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(t);
        p.add(Box.createVerticalStrut(8));
        return p;
    }

    /** Fila "Etiqueta: [valor]" */
    private JPanel labelRow(String etiqueta, JLabel valor) {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(F_NORMAL);
        lbl.setForeground(C_HEADER);
        row.add(lbl,   BorderLayout.WEST);
        row.add(valor, BorderLayout.EAST);
        return row;
    }

    /** Fila de 3 celdas para la tabla de portales */
    private JPanel portalRow(String zona, JLabel normal, JLabel upside) {
        JPanel row = new JPanel(new GridLayout(1, 3, 4, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        JLabel z = new JLabel(zona);
        z.setFont(F_NORMAL);
        z.setForeground(C_HEADER);
        row.add(z);
        row.add(normal);
        row.add(upside);
        return row;
    }

    /** Fila de 3 celdas para la tabla del Upside Down */
    private JPanel zonaRow(String zona, JLabel ninos, JLabel demos) {
        JPanel row = new JPanel(new GridLayout(1, 3, 4, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        JLabel z = new JLabel(zona);
        z.setFont(F_NORMAL);
        z.setForeground(C_HEADER);
        row.add(z);
        row.add(ninos);
        row.add(demos);
        return row;
    }

    /** Celda de cabecera de tabla */
    private JLabel headerCell(String txt) {
        JLabel l = new JLabel(txt, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(new Color(80, 80, 120));
        return l;
    }

    /** Label de valor numérico centrado */
    private JLabel valLabel(String txt) {
        JLabel l = new JLabel(txt, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(new Color(40, 80, 160));
        return l;
    }

    /** Label de zona Hawkins (valor alineado a la derecha) */
    private JLabel rowLabel(String txt) {
        JLabel l = new JLabel(txt, SwingConstants.RIGHT);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(new Color(40, 80, 160));
        return l;
    }

    /** Label de evento (apagado por defecto) */
    private JLabel evLabel(String txt) {
        JLabel l = new JLabel(txt, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.PLAIN, 11));
        l.setForeground(C_EVENT_OFF);
        l.setOpaque(true);
        l.setBackground(new Color(230, 230, 235));
        l.setBorder(new EmptyBorder(4, 2, 4, 2));
        return l;
    }

    private void addRows(JPanel panel, JComponent... rows) {
        for (JComponent r : rows) {
            r.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(r);
            panel.add(Box.createVerticalStrut(4));
        }
    }
}