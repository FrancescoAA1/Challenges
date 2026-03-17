import java.util.Scanner;
import java.util.PriorityQueue;
import java.io.File;

public class stockprices {

    static class Order {
        int price;
        int quantity;

        Order(int p, int q) {
            price = p;
            quantity = q;
        }
    }

    public static void main(String[] args) {
        try {

            File f = new File("stockprices.txt");
            Scanner s = new Scanner(f);

            int count = s.nextInt();
            s.nextLine();

            String ask, bid, stock;

            for (int i = 0; i < count; i++) {
                PriorityQueue<Order> buyOrders = new PriorityQueue<Order>((a, b) -> b.price - a.price);
                PriorityQueue<Order> sellOrders = new PriorityQueue<Order>((a, b) -> a.price - b.price);

                int lastDealPrice = -1;

                int nOrders = s.nextInt();
                s.nextLine();

                for (int j = 0; j < nOrders; j++) {
                    String[] line = s.nextLine().split(" ");
                    if (line[0].equals("buy")) {
                        Order order = new Order(Integer.parseInt(line[4]), Integer.parseInt(line[1]));
                        while (!sellOrders.isEmpty() && sellOrders.peek().price <= order.price && order.quantity > 0) {
                            Order sellOrder = sellOrders.poll();
                            int traded = Math.min(sellOrder.quantity, order.quantity);

                            sellOrder.quantity -= traded;
                            order.quantity -= traded;

                            lastDealPrice = Math.min(order.price, sellOrder.price);

                            if (sellOrder.quantity > 0) {
                                sellOrders.add(sellOrder);
                            }
                        }

                        if (order.quantity > 0) {
                            buyOrders.add(order);
                        }
                    } else {
                        Order order = new Order(Integer.parseInt(line[4]), Integer.parseInt(line[1]));
                        while (!buyOrders.isEmpty() && buyOrders.peek().price >= order.price && order.quantity > 0) {
                            Order buyOrder = buyOrders.poll();
                            int traded = Math.min(buyOrder.quantity, order.quantity);

                            order.quantity -= traded;
                            buyOrder.quantity -= traded;

                            lastDealPrice = Math.min(order.price, buyOrder.price);

                            if (buyOrder.quantity > 0) {
                                buyOrders.add(buyOrder);
                            }
                        }

                        if (order.quantity > 0) {
                            sellOrders.add(order);
                        }
                    }

                    ask = sellOrders.isEmpty() ? "-" : Integer.toString(sellOrders.peek().price);
                    bid = buyOrders.isEmpty() ? "-" : Integer.toString(buyOrders.peek().price);
                    stock = lastDealPrice == -1 ? "-" : Integer.toString(lastDealPrice);

                    System.out.println(ask + " " + bid + " " + stock);

                }
            }
        } catch (Exception e) {

        }
    }

}