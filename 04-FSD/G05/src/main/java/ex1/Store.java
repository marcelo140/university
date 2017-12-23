package ex1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store {
    Map<Integer, Book> collection = new HashMap<>();

    Store() {
        collection.put(1, new Book(1, "Lev Tolstoy", "The Death of Ivan Ilyich"));
        collection.put(2, new Book(2, "Haruki Murakami", "Kafka on the Shore"));
        collection.put(3, new Book(3, "John Steinbeck", "East of Eden"));
        collection.put(4, new Book(4, "Daniel Keys", "Flowers for Algernon"));
        collection.put(5, new Book(5, "Markus Zusak", "The Book Thief"));
    }

    public Book getBook(int isbn) {
        return collection.get(isbn);
    }

    public Book search(final String title) {
        return collection.values().stream()
            .filter(book -> book.getTitle().equals(title))
            .findFirst()
            .orElse(null);
    }

    public Cart newCart() {
        return new Cart();
    }

    public class Cart {
        private List<Book> wishes = new ArrayList<>();

        public void add(Book b) {
            wishes.add(b);
        }

        public boolean buy() {
            wishes.clear();
            return true;
        }
    }
}
