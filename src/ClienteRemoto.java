import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.rmi.Naming;

public class ClienteRemoto extends JFrame {
    private HawkinsService servicio;
    private final JLabel lblConexion = new JLabel("Conectando...");
    private final JLabel lblPausa = new JLabel("-");
    private final JTextArea areaEstado = new JTextArea();
    private final JButton btnPausarReanudar = new JButton("Pausar");
    private Timer timer;

    public ClienteRemoto() {
        super("Cliente remoto - La Batalla de Hawkins");
        configurarVentana();
        conectar();
        iniciarActualizacionAutomatica();
    }

    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel cabecera = new JPanel(new BorderLayout(8, 8));
        cabecera.setBorder(new EmptyBorder(10, 10, 0, 10));
        lblConexion.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPausa.setHorizontalAlignment(SwingConstants.RIGHT);
        cabecera.add(lblConexion, BorderLayout.WEST);
        cabecera.add(lblPausa, BorderLayout.EAST);

        areaEstado.setEditable(false);
        areaEstado.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaEstado.setMargin(new Insets(10, 10, 10, 10));

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pie.setBorder(new EmptyBorder(0, 10, 10, 10));
        btnPausarReanudar.addActionListener(e -> cambiarPausa());
        pie.add(btnPausarReanudar);

        add(cabecera, BorderLayout.NORTH);
        add(new JScrollPane(areaEstado), BorderLayout.CENTER);
        add(pie, BorderLayout.SOUTH);
    }

    private void conectar() {
        try {
            servicio = (HawkinsService) Naming.lookup("rmi://localhost:1099/HawkinsService");
            lblConexion.setText("Conectado al servidor RMI de Hawkins");
        } catch (Exception e) {
            lblConexion.setText("No se pudo conectar al servidor RMI");
            areaEstado.setText("Comprueba que el programa principal esté ejecutándose.\n\n" + e.getMessage());
            btnPausarReanudar.setEnabled(false);
        }
    }

    private void iniciarActualizacionAutomatica() {
        timer = new Timer(1000, e -> actualizarEstado());
        timer.start();
    }

    private void actualizarEstado() {
        if (servicio == null) {
            return;
        }

        try {
            EstadoHawkins estado = servicio.getEstado();
            lblPausa.setText(estado.isPausado() ? "Estado: PAUSADO" : "Estado: EN EJECUCION");
            btnPausarReanudar.setText(estado.isPausado() ? "Reanudar" : "Pausar");
            areaEstado.setText(formatearEstado(estado));
        } catch (Exception e) {
            lblConexion.setText("Conexion perdida con el servidor RMI");
            areaEstado.setText("Error consultando el estado remoto:\n" + e.getMessage());
        }
    }

    private String formatearEstado(EstadoHawkins e) {
        StringBuilder sb = new StringBuilder();
        sb.append("ULTIMA ACTUALIZACION: ").append(e.getFechaActualizacion()).append("\n\n");

        sb.append("HAWKINS\n");
        sb.append("  Total en Hawkins:       ").append(e.getNinosHawkins()).append("\n");
        sb.append("  Calle Principal:        ").append(e.getNinosCallePrincipal()).append("\n");
        sb.append("  Sotano Byers:           ").append(e.getNinosSotanoByers()).append("\n");
        sb.append("  Radio WSQK:             ").append(e.getNinosRadioWSQK()).append("\n\n");

        sb.append("PORTALES  (Normal -> Upside Down | Upside Down -> Hawkins)\n");
        sb.append(String.format("  Bosque:              %4d | %4d%n", e.getPortalBosqueNormal(), e.getPortalBosqueUpsideDown()));
        sb.append(String.format("  Laboratorio:         %4d | %4d%n", e.getPortalLaboratorioNormal(), e.getPortalLaboratorioUpsideDown()));
        sb.append(String.format("  Centro Comercial:    %4d | %4d%n", e.getPortalCentroNormal(), e.getPortalCentroUpsideDown()));
        sb.append(String.format("  Alcantarillado:      %4d | %4d%n%n", e.getPortalAlcantarilladoNormal(), e.getPortalAlcantarilladoUpsideDown()));

        sb.append("UPSIDE DOWN\n");
        sb.append(String.format("  Bosque:              ninos=%4d  demogorgons=%4d%n", e.getNinosBosque(), e.getDemogorgonsBosque()));
        sb.append(String.format("  Laboratorio:         ninos=%4d  demogorgons=%4d%n", e.getNinosLaboratorio(), e.getDemogorgonsLaboratorio()));
        sb.append(String.format("  Centro Comercial:    ninos=%4d  demogorgons=%4d%n", e.getNinosCentroComercial(), e.getDemogorgonsCentroComercial()));
        sb.append(String.format("  Alcantarillado:      ninos=%4d  demogorgons=%4d%n", e.getNinosAlcantarillado(), e.getDemogorgonsAlcantarillado()));
        sb.append("  Colmena:             ").append(e.getNinosColmena()).append(" capturados\n");
        sb.append("  Demogorgons activos: ").append(e.getDemogorgonsActivos()).append("\n\n");

        sb.append("RECURSOS Y EVENTOS\n");
        sb.append("  Sangre disponible:   ").append(e.getSangreDisponible()).append("\n");
        sb.append("  Evento activo:       ").append(e.getEventoActivo()).append("\n\n");

        sb.append("RANKING TOP 3 DEMOGORGONS\n");
        String[] ranking = e.getRankingDemogorgons();
        if (ranking.length == 0) {
            sb.append("  Sin demogorgons registrados todavia\n");
        } else {
            for (int i = 0; i < ranking.length; i++) {
                sb.append("  ").append(i + 1).append(". ").append(ranking[i]).append("\n");
            }
        }

        return sb.toString();
    }

    private void cambiarPausa() {
        try {
            if (servicio.estaPausado()) {
                servicio.reanudar();
            } else {
                servicio.pausar();
            }
            actualizarEstado();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cambiar el estado de pausa:\n" + e.getMessage(),
                    "Error RMI",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClienteRemoto().setVisible(true));
    }
}
