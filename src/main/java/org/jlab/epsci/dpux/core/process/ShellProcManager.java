package org.jlab.epsci.dpux.core.process;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OSProcess;
import oshi.util.FormatUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A class to start and monitor a shell process, capture its metrics, and export them using Prometheus.
 * It also captures the process's stdout and stderr and sends email alerts if thresholds are exceeded.
 *
 * @author Vardan Gyurjyan
 */
public class ShellProcManager {

    private Process process;
    private long pid;
    private ScheduledExecutorService scheduler;
    private BufferedReader stdOutput;
    private BufferedReader stdError;
    private SystemInfo systemInfo;
    private OperatingSystem os;
    private HTTPServer server;

    private long prevCpuTime = 0;
    private long prevSystemTime = 0;

    private static final Gauge cpuUsage = Gauge.build()
            .name("process_cpu_usage_percent")
            .help("CPU usage of the process in percent.")
            .register();

    private static final Gauge memoryUsage = Gauge.build()
            .name("process_memory_usage_bytes")
            .help("Memory usage of the process in bytes.")
            .register();

    private static final Gauge virtualMemoryUsage = Gauge.build()
            .name("process_virtual_memory_usage_bytes")
            .help("Virtual memory usage of the process in bytes.")
            .register();

    private static final Gauge bytesRead = Gauge.build()
            .name("process_bytes_read")
            .help("Bytes read by the process.")
            .register();

    private static final Gauge bytesWritten = Gauge.build()
            .name("process_bytes_written")
            .help("Bytes written by the process.")
            .register();

    private static final Counter processExits = Counter.build()
            .name("process_exit_count")
            .help("Count of process exits.")
            .register();

    private static final Counter stdoutLines = Counter.build()
            .name("process_stdout_lines")
            .help("Number of lines captured from stdout.")
            .register();

    private static final Counter stderrLines = Counter.build()
            .name("process_stderr_lines")
            .help("Number of lines captured from stderr.")
            .register();

    // Define thresholds
    private static final double CPU_USAGE_THRESHOLD = 80.0;  // in percent
    private static final long MEMORY_USAGE_THRESHOLD = 200 * 1024 * 1024;  // 200 MB
    private static final long BYTES_READ_THRESHOLD = 100 * 1024 * 1024;  // 100 MB
    private static final long BYTES_WRITTEN_THRESHOLD = 100 * 1024 * 1024;  // 100 MB

    // Interval for capturing stdio output
    private static final long STDIO_CAPTURE_INTERVAL = 1;  // in seconds

    private final EmailAlert emailAlert = new EmailAlert();

    /**
     * Constructor to initialize the ShellProcessMonitor.
     *
     * @param command The command to start the shell process.
     * @throws IOException If an I/O error occurs.
     */
    public ShellProcManager(String command) throws IOException {
        this.process = Runtime.getRuntime().exec(command);
        this.pid = getProcessId(process);
        this.stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        this.stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.systemInfo = new SystemInfo();
        this.os = systemInfo.getOperatingSystem();
        DefaultExports.initialize();  // Export JVM metrics
        this.server = new HTTPServer(8080);
    }

    /**
     * Retrieves the process ID.
     *
     * @param process The process whose ID is to be retrieved.
     * @return The process ID.
     */
    private long getProcessId(Process process) {
        // This method may be platform dependent. For Unix-like systems:
        String processInfo = process.toString();
        String[] parts = processInfo.split(",");
        String pidPart = parts[0].split("=")[1];
        return Long.parseLong(pidPart);
    }

    /**
     * Starts monitoring the process.
     */
    public void startMonitoring() {
        scheduler.scheduleAtFixedRate(this::monitorMetrics, 0, 1, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::captureOutput, 0, STDIO_CAPTURE_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * Monitors the metrics of the process.
     */
    private void monitorMetrics() {
        try {
            if (!process.isAlive()) {
                System.out.println("Process ID " + pid + " has terminated.");
                stopMonitoring();
                return;
            }

            OSProcess osProcess = os.getProcess((int) pid);
            if (osProcess != null) {
                System.out.println("Monitoring Process ID: " + pid);

                double cpuLoad = calculateCpuUsage(osProcess);
                long memUsage = osProcess.getResidentSetSize();
                long virtMemUsage = osProcess.getVirtualSize();
                long readBytes = osProcess.getBytesRead();
                long writtenBytes = osProcess.getBytesWritten();

                cpuUsage.set(cpuLoad);
                memoryUsage.set(memUsage);
                virtualMemoryUsage.set(virtMemUsage);
                bytesRead.set(readBytes);
                bytesWritten.set(writtenBytes);

                // Check thresholds and log alerts
                if (cpuLoad > CPU_USAGE_THRESHOLD) {
                    String alertMsg = "WARNING: CPU usage exceeds threshold: " + cpuLoad + " %";
                    System.err.println(alertMsg);
                    emailAlert.sendAlert("CPU Usage Alert", alertMsg);
                }
                if (memUsage > MEMORY_USAGE_THRESHOLD) {
                    String alertMsg = "WARNING: Memory usage exceeds threshold: " + FormatUtil.formatBytes(memUsage);
                    System.err.println(alertMsg);
                    emailAlert.sendAlert("Memory Usage Alert", alertMsg);
                }
                if (readBytes > BYTES_READ_THRESHOLD) {
                    String alertMsg = "WARNING: Bytes read exceeds threshold: " + FormatUtil.formatBytes(readBytes);
                    System.err.println(alertMsg);
                    emailAlert.sendAlert("IO Read Alert", alertMsg);
                }
                if (writtenBytes > BYTES_WRITTEN_THRESHOLD) {
                    String alertMsg = "WARNING: Bytes written exceeds threshold: " + FormatUtil.formatBytes(writtenBytes);
                    System.err.println(alertMsg);
                    emailAlert.sendAlert("IO Write Alert", alertMsg);
                }
            } else {
                System.out.println("Process ID " + pid + " not found.");
            }
        } catch (Exception e) {
            System.err.println("Error monitoring process: " + e.getMessage());
            stopMonitoring();
        }
    }

    /**
     * Calculates the CPU usage of the process.
     *
     * @param osProcess The OSProcess object representing the process.
     * @return The CPU usage as a percentage.
     */
    private double calculateCpuUsage(OSProcess osProcess) {
        long currentCpuTime = osProcess.getKernelTime() + osProcess.getUserTime();
        long currentSystemTime = System.nanoTime();

        double cpuUsage = 0.0;
        if (prevSystemTime != 0) {
            long cpuTimeDiff = currentCpuTime - prevCpuTime;
            long systemTimeDiff = currentSystemTime - prevSystemTime;
            cpuUsage = (cpuTimeDiff / (double) systemTimeDiff) * 100;
        }

        prevCpuTime = currentCpuTime;
        prevSystemTime = currentSystemTime;

        return cpuUsage;
    }

    /**
     * Captures the stdout and stderr output of the process.
     */
    private void captureOutput() {
        try {
            String line;
            while ((line = stdOutput.readLine()) != null) {
                System.out.println("STDOUT: " + line);
                stdoutLines.inc();
            }
            while ((line = stdError.readLine()) != null) {
                System.err.println("STDERR: " + line);
                stderrLines.inc();
            }
        } catch (IOException e) {
            System.err.println("Error capturing output: " + e.getMessage());
        }
    }

    /**
     * Stops monitoring the process.
     */
    public void stopMonitoring() {
        scheduler.shutdown();
        try {
            scheduler.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            int exitCode = process.waitFor();
            processExits.inc();
            System.out.println("Process exited with code: " + exitCode);
            printStream(stdOutput);
            printStream(stdError);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeStream(stdOutput);
            closeStream(stdError);
            server.stop();
        }
    }

    /**
     * Prints the content of a BufferedReader to the console.
     *
     * @param reader The BufferedReader to be printed.
     * @throws IOException If an I/O error occurs.
     */
    private void printStream(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

    /**
     * Closes a BufferedReader.
     *
     * @param reader The BufferedReader to be closed.
     */
    private void closeStream(BufferedReader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Kills the process.
     */
    public void killProcess() {
        process.destroy();
    }

    public static void main(String[] args) {
        try {
            ShellProcManager monitor = new ShellProcManager("your_command_here");
            monitor.startMonitoring();
            // Simulate some work
            Thread.sleep(10000);
            monitor.stopMonitoring();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
