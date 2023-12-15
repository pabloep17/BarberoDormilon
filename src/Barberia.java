import javax.swing.*;
import java.util.ArrayList;

public class Barberia {

    private int TIEMPO_CORTE = 4000;
    private int n_plazas;

    private boolean barbero_durmiendo = true;

    private ArrayList<String> clientesSalaEspera = new ArrayList<>();

    JPanel salaEsperaPanel, cortePeloPanel, dormirPanel;

    public Barberia(int n_plazas, JPanel salaEsperaPanel, JPanel cortePeloPanel, JPanel dormirPanel) {
        this.n_plazas = n_plazas;
        this.salaEsperaPanel = salaEsperaPanel;
        this.cortePeloPanel = cortePeloPanel;
        this.dormirPanel = dormirPanel;
    }

    public void sentarse() throws InterruptedException {

        //System.out.println(Thread.currentThread().getName() + " esta en la puerta de la barberia");

        //Comprobar si hay plazas
        if (clientesSalaEspera.size() < n_plazas) {
            //Agregar cliente a lista de espera
            agregarClienteSalaEspera(Thread.currentThread().getName());

            System.out.println(Thread.currentThread().getName() + " se ha sentado");

            //Enviar al cliente a cortar
            cortar();

        }else{
            //Mandar al cliente a la mierda porque no hay sitio, que se busque otra barberia
            System.out.println(Thread.currentThread().getName() + " se ha ido porque no hay plazas");
        }

    }

    private synchronized void cortar() throws InterruptedException {
        // Si el barbero esta dumiendo que se despierte
        if (barbero_durmiendo) {
            despetarBarbero();
        }
        BarberShopGUI.actualizarSalaEspera(this, salaEsperaPanel);
        // Borra al cliente de la sala de espera porque ya esta cortando
        borrarClienteSalaEspera(Thread.currentThread().getName());
        // Mostrar cliente que esta cortando
        BarberShopGUI.actualizarClienteCortando(Thread.currentThread().getName());

        //System.out.println(Thread.currentThread().getName() + " esta cortando");

        //Duracion del corte
        Thread.sleep(TIEMPO_CORTE);

        //Quitar al cliente que ya acabo de cortar
        BarberShopGUI.actualizarClienteTerminandoCortando();

        //System.out.println(Thread.currentThread().getName() + " ha dejado de cortar y se va");

        if (clientesSalaEspera.size() == 0) {
            dormirBarbero();
        }
    }

    private void despetarBarbero() {
        BarberShopGUI.barberoSeDespierta();
        barbero_durmiendo = false;
        //System.out.println(Thread.currentThread().getName() + " ha despertado al barbero");
    }

    private void dormirBarbero() {
        BarberShopGUI.barberoSeDuerme();
        barbero_durmiendo = true;
        //System.out.println("El barbero se duerme");
    }

    public int getNumeroPlazasOcupadas() {
        return clientesSalaEspera.size();
    }

    public ArrayList<String> getClientes() {
        return clientesSalaEspera;
    }

    private void borrarClienteSalaEspera(String name) {
        clientesSalaEspera.remove(name);
        BarberShopGUI.actualizarSalaEspera(this, salaEsperaPanel);
    }

    private void agregarClienteSalaEspera(String name) {
        clientesSalaEspera.add(name);
        BarberShopGUI.actualizarSalaEspera(this, salaEsperaPanel);
    }

}
