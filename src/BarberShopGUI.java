import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.function.Consumer;

public class BarberShopGUI extends JFrame {
    private JPanel panel;
    private static JPanel cortePeloPanel = new JPanel();
    private static JPanel dormirPanel = new JPanel();
    private static JPanel salaEsperaPanel = new JPanel(new GridLayout(0, 1)); // GridLayout con una sola columna

    private static JLabel clienteCortando = new JLabel("");
    private static JLabel barberoCortando = new JLabel("");
    private static JLabel barberoDescansando = new JLabel("");

    private Barberia barberia;

    public BarberShopGUI(Barberia barberia) {

        iniciarHiloClientes();

        this.barberia = barberia;

        setTitle("Barbería");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);

        panel = new JPanel(new GridLayout(1, 3));

        salaEsperaPanel.setBorder(BorderFactory.createTitledBorder("Sala de Espera"));

        panel.add(salaEsperaPanel);

        JPanel izquierdaPanel = new JPanel(new GridLayout(2, 1));

        cortePeloPanel.setBackground(Color.CYAN);
        cortePeloPanel.setBorder(BorderFactory.createTitledBorder("Área de Corte de Pelo"));
        izquierdaPanel.add(cortePeloPanel);

        cortePeloPanel.add(clienteCortando);
        cortePeloPanel.add(barberoCortando);

        dormirPanel.setBackground(Color.YELLOW);
        dormirPanel.setBorder(BorderFactory.createTitledBorder("Zona para Dormir"));
        izquierdaPanel.add(dormirPanel);

        dormirPanel.add(barberoDescansando);

        try {
            File file = new File("./res/barbero.jpg");
            Image img = ImageIO.read(file);
            ImageIcon icon = new ImageIcon(img);
            barberoDescansando.setIcon(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        panel.add(izquierdaPanel);

        getContentPane().add(panel);
        setVisible(true);

    }

    public static void actualizarSalaEspera(Barberia barberia, JPanel salaEsperaPanel) {

        System.out.println("Se actualiza la sala de espera. Numero de clientes esperando: " + barberia.getNumeroPlazasOcupadas());

        salaEsperaPanel.removeAll(); // Limpiar la sala de espera antes de volver a agregar clientes

        barberia.getClientes().stream().forEach(c -> {
            JLabel cliente = new JLabel(c);
            salaEsperaPanel.add(cliente);
        });

        salaEsperaPanel.revalidate();
        salaEsperaPanel.repaint();
    }

    public static void actualizarClienteCortando(String cliente) {
        clienteCortando.setText(cliente);
    }

    public static void actualizarClienteTerminandoCortando() {
       clienteCortando.setText("");
    }

    public static void barberoSeDespierta() {
        try {
            // Cambia la ruta si tu imagen está en otro directorio
            File file = new File("./res/barbero.jpg");
            Image img = ImageIO.read(file);
            ImageIcon icon = new ImageIcon(img);
            barberoCortando.setIcon(icon);
            barberoDescansando.setIcon(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void barberoSeDuerme() {
        try {
            // Cambia la ruta si tu imagen está en otro directorio
            File file = new File("./res/barbero.jpg");
            Image img = ImageIO.read(file);
            ImageIcon icon = new ImageIcon(img);
            barberoDescansando.setIcon(icon);
            barberoCortando.setIcon(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarHiloClientes() {

        Random R = new Random();

        Thread threadClientes = new Thread(() -> {

            String nombres[] = {"Juan", "Pedro", "Marta", "Lara", "Nuria", "Paula", "Maria", "Carlos", "Nacho", "Abel", "Andres", "Pablo", "Alex", "Manuel", "Yerai", "Alejrandro"};

            int n = 0;
            //Max 15 pa q pueda tener nombre
            while (n < 14) {
                try {
                    double s = (R.nextDouble() * 7.0) * 1000;
                    Thread.sleep((long) s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                new Cliente(nombres[n], barberia).start();

                n++;
            }
        });
        threadClientes.start();
    }

    public static void main(String[] args) {

        Barberia barberia = new Barberia(5, salaEsperaPanel, cortePeloPanel, dormirPanel);

        SwingUtilities.invokeLater(() -> new BarberShopGUI(barberia));
    }
}
