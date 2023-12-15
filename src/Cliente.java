public class Cliente extends Thread {

    Barberia barberia;

    public Cliente(String nombre, Barberia barberia) {
        super(nombre);
        this.barberia = barberia;
    }

    @Override
    public void run() {

        try {
            barberia.sentarse();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
