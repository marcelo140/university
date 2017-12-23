package ex4;

import ex4.stubs.RemoteCart;
import ex4.stubs.RemoteStore;

public class Client {
    public static void main(String[] args) throws Exception {
        RemoteStore store = new RemoteStore();

        int bookIsbn = store.search("East of Eden");
        System.out.println("Book found");

        RemoteCart cart = store.newCart();
        System.out.println("Cart created");

        cart.add(bookIsbn);
        System.out.println("Book added to cart");

        boolean success = cart.buy();
        System.out.println("The purchase was a success: " + success);
    }
}
