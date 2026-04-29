
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

public class Log {

    private static final Log INSTANCE = new Log();
    private final ReentrantLock lock = new ReentrantLock();
    private PrintWriter writer;
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static java.util.function.Consumer<String> guiCallback;

    private Log() {
        try {
            writer = new PrintWriter(new FileWriter("hawkins.txt", false), true);
        } catch (IOException e) {
            System.err.println("[LOGGER ERROR] No se pudo abrir hawkins.txt: " + e.getMessage());
        }
    }

    public static Log getInstance() { return INSTANCE; }

    public void log(String message) {
        String line = "[" + LocalDateTime.now().format(FMT) + "] " + message;
        lock.lock();
        try {
            if (writer != null) writer.println(line);
            System.out.println(line);
            if (guiCallback != null) guiCallback.accept(line);
        } finally {
            lock.unlock();
        }
    }

    public void close() {
        lock.lock();
        try { if (writer != null) writer.close(); }
        finally { lock.unlock(); }
    }
    public static void setGuiCallback(java.util.function.Consumer<String> cb) { guiCallback = cb; }
}