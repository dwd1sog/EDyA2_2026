import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Queues {
    public static void main(String [] args) throws InterruptedException {
        Queue atmLine = new Queue();

        System.out.println("Simulación de cajero automático\n");

        atmLine.enqueue(new Person("Carlos Gómez", 150.0));
        Thread.sleep(1000);

        atmLine.enqueue(new Person("Ana Martínez", 300.0));
        Thread.sleep(1000);

        atmLine.enqueue(new Person("Lucía Fernández", 50.0));

        atmLine.print();

        System.out.println("Siguiente en atender (Peek): " + atmLine.peek().getName());

        System.out.println("\nAtendiendo en cajero:");
        Person atendida1 = atmLine.dequeue();
        System.out.println("Atendido/a: " + atendida1.getName() + " (Retiró $" + atendida1.getWithdrawalAmount() + ")");

        Person atendida2 = atmLine.dequeue();
        System.out.println("Atendido/a: " + atendida2.getName() + " (Retiró $" + atendida2.getWithdrawalAmount() + ")");

        // Estado final de la fila
        atmLine.print();
    }

    static class Person {
        private String name;
        private double withdrawalAmount;
        private LocalDateTime arrivalDate;

        public Person(String name, double withdrawalAmount) {
            this.name = name;
            this.withdrawalAmount = withdrawalAmount;
            this.arrivalDate = LocalDateTime.now();
        }

        public String getName() {
            return name;
        }

        public double getWithdrawalAmount() {
            return withdrawalAmount;
        }

        public LocalDateTime getArrivalDate() {
            return arrivalDate;
        }

        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            return "Cliente: " + name + " va a retirar la cantidad: $" + withdrawalAmount + " a la hora de llegada: " + arrivalDate.format(formatter);
        }
    }

    static class Queue{
        private List<Person> items;

        public Queue() {
            this.items = new ArrayList<>();
        }

        public void enqueue(Person person) {
            items.add(person);
            System.out.println("-> " + person.getName() + " ha ingresado a la fila del cajero.");
        }

        public Person dequeue() {
            if (isEmpty()) {
                System.out.println("La fila está vacía. No hay nadie para atender.");
                return null;
            }
            return items.removeFirst();
        }

        public Person peek() {
            if (isEmpty()) {
                return null;
            }
            return items.getFirst();
        }

        public boolean isEmpty() {
            return items.isEmpty();
        }

        public int size() {
            return items.size();
        }

        public void print() {
            if (isEmpty()) {
                System.out.println("La fila del cajero está vacía.");
                return;
            }

            System.out.println("\nEstado en la fila del cajero:");
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i + 1) + ". " + items.get(i));
            }
            System.out.println("--------------------------------------\n");
        }
    }

}
