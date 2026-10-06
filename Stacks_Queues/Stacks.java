import java.util.ArrayList;

public class Stacks{
    public static void main (String [] args){
        Stack booksStack = new Stack();

        System.out.println("1. Apilando Libros:");
        booksStack.push(new Book("Cien Años de Soledad", "978-0307474728", "Gabriel García Márquez", "Editorial Sudamericana"));
        booksStack.push(new Book("Don Quijote de la Mancha", "978-8424116286", "Miguel de Cervantes", "Espasa-Calpe"));
        booksStack.push(new Book("El Principito", "978-0156013987", "Antoine de Saint-Exupéry", "Reynal & Hitchcock"));

        booksStack.printStack();

        System.out.println("\n2. Consultando la Cima");
        System.out.println("Libro en la cima: " + booksStack.peek());

        System.out.println("\n3. Desapilando Libros");
        Book unstackedBook = booksStack.pop();
        System.out.println("Se retiró de la pila: " + unstackedBook);

        booksStack.printStack();

        System.out.println("\n4. Comprobando Tamaño y Estado");
        System.out.println("Cantidad de libros en la pila: " + booksStack.size());
        System.out.println("¿La pila está vacía?: " + booksStack.isEmpty());
    }

    static class Book{
        private String name;
        private String isbn;
        private String author;
        private String editorial;

        public Book(String name, String isbn, String author, String editorial){
            this.name = name;
            this.isbn = isbn;
            this.author = author;
            this.editorial = editorial;
        }
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "'" + name + "' de " + author + " [" + editorial + ", ISBN: " + isbn + "]";
        }
    }

    static class Stack {
        private ArrayList<Book> items;

        public Stack() {
            this.items = new ArrayList<>();
        }

        public void push(Book book) {
            this.items.add(book);
            System.out.println("Apilado con éxito: " + book.getName());
        }

        public Book pop() {
            if (isEmpty()) {
                System.out.println("La pila está vacía. No se puede desapilar.");
                return null;
            }

            int lastIndex = this.items.size() - 1;
            return this.items.remove(lastIndex);
        }

        public Book peek() {
            if (isEmpty()) {
                System.out.println("La pila está vacía.");
                return null;
            }
            return this.items.getLast();
        }

        public boolean isEmpty() {
            return this.items.isEmpty();
        }

        public int size() {
            return this.items.size();
        }

        public void printStack() {
            if (isEmpty()) {
                System.out.println("La pila está vacía.");
                return;
            }

            System.out.println("\nEstado de la pila (Cima -> Base):");
            for (int i = this.items.size() - 1; i >= 0; i--) {
                String tag = (i == this.items.size() - 1) ? " [CIMA] " : "        ";
                System.out.println(tag + "Nivel " + i + ": " + this.items.get(i));
            }
            System.out.println("==================================================");
        }
    }
}