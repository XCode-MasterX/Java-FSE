import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
 Scenario: 
    You are tasked with sorting customer orders by their total price on an e-commerce platform. This helps in prioritizing high-value orders.
 Steps:
    1.	Understand Sorting Algorithms:
    	Explain different sorting algorithms (Bubble Sort, Insertion Sort, Quick Sort, Merge Sort).
    2.	Setup:
    	Create a class Order with attributes like orderId, customerName, and totalPrice.
    3.	Implementation:
    	Implement Bubble Sort to sort orders by totalPrice.
    	Implement Quick Sort to sort orders by totalPrice.
    4.	Analysis:
    	Compare the performance (time complexity) of Bubble Sort and Quick Sort.
    	Discuss why Quick Sort is generally preferred over Bubble Sort.
 */

class SortingOrders {
    static Scanner in = new Scanner(System.in);

    public static Order getOrder() {
        System.out.print("Enter the ID of the order: ");
        String orderID = in.nextLine();

        System.out.print("Enter the NAME of the customer: ");
        String customerName = in.nextLine();

        System.out.print("Enter the PRICE of the order: ");
        float totalPrice = in.nextFloat();

        in.nextLine();

        return new Order(orderID, customerName, totalPrice);
    }

    public static void main(String args[]) {
        ArrayList<Order> orders = new ArrayList<>();

        System.out.print("Enter the number of orders: ");
        int num = in.nextInt();
        in.nextLine();
        for(int i = 0; i < num; i++)
            orders.add(getOrder());
        
        System.out.println("1. Bubble Sort thel list of the orders.");
        System.out.println("2. Quick Sort the list of the orders.");
        System.out.print("Enter the choice: ");
        int choice = in.nextInt();
        switch(choice) {
            case 1:
                orders = bubbleSort(orders);
                break;
            case 2:
                quickSort(orders, 0, orders.size() - 1);
                break;
            default:
                System.out.println("The choice is invalid.");
                return;
        }

        Arrays.stream(orders.toArray()).forEach(System.out::println);
    }

    public static ArrayList<Order> bubbleSort(ArrayList<Order> list) {
        for(int i = 0; i < list.size(); i++) {
            for(int j = i + 1; j < list.size(); j++) {
                if(list.get(i).totalPrice > list.get(j).totalPrice)
                {
                    Order temp = list.get(j);
                    list.set(j, list.get(i));
                    list.set(i, temp);
                }
            }
        }

        return list;
    }

    static int partition(ArrayList<Order> arr, int low, int high) {
        Order pivot = arr.get(high);

        int i = low - 1;
        for (int j = low; j <= high - 1; j++) {
            if (arr.get(j).totalPrice < pivot.totalPrice) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);  
        return i + 1;
    }

    static void swap(ArrayList<Order> arr, int i, int j) {
        Order temp = arr.get(j);
        arr.set(j, arr.get(i));
        arr.set(i, temp);
    }

    public static void quickSort(ArrayList<Order> arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
}

class Order {
    String orderID, customerName;
    float totalPrice;

    public Order(String id, String name, float price) {
        orderID = id;
        customerName = name;
        totalPrice = price;
    }

    public String toString() {
        return String.format("{ ID: %s Name: %s Price: %f }", orderID, customerName, totalPrice);
    }
}