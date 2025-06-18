import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.BiPredicate;
import java.util.Comparator;

/*
 Scenario: 
    You are working on the search functionality of an e-commerce platform. The search needs to be optimized for fast performance.
 Steps:
    1.	Understand Asymptotic Notation:
    	Explain Big O notation and how it helps in analyzing algorithms.
    	Describe the best, average, and worst-case scenarios for search operations.
    2.	Setup:
    	Create a class Product with attributes for searching, such as productId, productName, and category.
    3.	Implementation:
    	Implement linear search and binary search algorithms.
    	Store products in an array for linear search and a sorted array for binary search.
    4.	Analysis:
    	Match the time complexity of linear and binary search algorithms.
    	Discuss which algorithm is more suitable for your platform and why.
 */

class ECommerce {
    static Scanner in = new Scanner(System.in);

    public static int inputValidator(int minVal, int maxVal) {
        System.out.print("\n\nEnter your choice: ");
        int choice = in.nextInt();
        
        while(choice < minVal || choice > maxVal) { 
            System.out.println("Choice is invalid. Try again."); 
            choice = in.nextInt();
        }
        in.nextLine();

        return choice;
    }

    public static int getChoice() {
        System.out.println("-".repeat(40));
        System.out.println("Choose an option and enter the corresponding number:");
        System.out.println("1. Linear Search by ID");
        System.out.println("2. Linear Search by Name");
        System.out.println("3. Linear Search by Category");
        System.out.println("4. Binary Search by ID");
        System.out.println("5. Binary Search by Name");
        System.out.println("6. Binary Search by Category");

        return inputValidator(1, 7);
    }

    public static void main(String args[]) {        
        System.out.println("Enter the filename to load the products from: ");
        String fileName = in.nextLine();
        ArrayList<Product> prodList = getFromFile(fileName);
        BiPredicate<Product, String> idMatch = (x, y) -> x.productID.equals(y);
        BiPredicate<Product, String> nameMatch = (x, y) -> x.productName.equals(y);
        BiPredicate<Product, String> catMatch = (x, y) -> x.category.equals(y);
        Comparator<Product> idCompare = (x, y) -> x.productID.compareTo(y.productID);
        Comparator<Product> nameCompare = (x, y) -> x.productName.compareTo(y.productName);
        Comparator<Product> catCompare = (x, y) -> x.category.compareTo(y.category);
        
        Arrays.stream(prodList.toArray()).forEach(System.out::println);
        
        int choice = getChoice(), index;
        switch(choice) {
            case 1:
                System.out.println("Enter the target ID: ");
                index = linearSearch(prodList, in.nextLine(), idMatch);
                System.out.println(index == -1 ? "No item was found with that id..." : String.format("Item was found at index: %d", index));
                break;

            case 2:
                System.out.println("Enter the target Name: ");
                index = linearSearch(prodList, in.nextLine(), nameMatch);
                System.out.println(index == -1 ? "No item was found with that name..." : String.format("Item was found at index: %d", index));
                break;

            case 3:
                System.out.println("Enter the target Category: ");
                index = linearSearch(prodList, in.nextLine(), catMatch);
                System.out.println(index == -1 ? "No item was found with that category..." : String.format("Item was found at index: %d", index));
                break;

            case 4:
                System.out.println("Enter the target ID: ");
                prodList.sort(idCompare);
                index = binarySearch(prodList, new Product(in.nextLine(), "", ""), idCompare);
                System.out.println(index == -1 ? "No item was found with that id..." : String.format("Item was found at index: %d", index));
                break;
                
            case 5:
                System.out.println("Enter the target Name: ");
                prodList.sort(nameCompare);
                index = binarySearch(prodList, new Product("", in.nextLine(), ""), nameCompare);
                System.out.println(index == -1 ? "No item was found with that name..." : String.format("Item was found at index: %d", index));
                break;

            case 6:
                System.out.println("Enter the target Category: ");
                prodList.sort(catCompare);
                index = binarySearch(prodList, new Product("", "", in.nextLine()), catCompare);
                System.out.println(index == -1 ? "No item was found with that category..." : String.format("Item was found at index: %d", index));
                break;

            default:
                System.out.println("How did this happen??");
                break;
        }

        in.close();
    }

    public static int linearSearch(ArrayList<Product> list, String target, BiPredicate<Product, String> condition) {
        for(int i = 0; i < list.size(); i++)
            if(condition.test(list.get(i), target))
                return i;

        return -1;
    }

    public static int binarySearch(ArrayList<Product> list, Product targetProduct, Comparator<Product> compare) {
        Arrays.stream(list.toArray()).forEach(System.out::println);

        int s = 0, e = list.size() - 1;

        while(s <= e) {
            int m = (s + e) >> 1;
            Product curr = list.get(m);
            int res = compare.compare(targetProduct, curr);

            if(res == 0) return m;
            else if(res < 0) e = m - 1;
            else if(res > 0) s = m + 1;
        }
        return -1;
    }

    public static ArrayList<Product> getFromFile(String fileName) {
        File f = new File(fileName);

        if(!f.exists())  return null;
    

        ArrayList<Product> list = new ArrayList<>();
        try {
            Scanner reader = new Scanner(new FileInputStream(f));

            while(reader.hasNextLine()) {
                String line[] = reader.nextLine().split(", ");
                list.add(new Product(line[0], line[1], line[2]));
            }

            reader.close();
        }
        catch(IOException e) { e.printStackTrace(); }

        return list;
    }
}

class Product {
    String productID, productName, category;

    public Product(String pid, String name, String cat) {
        productID = pid;
        productName = name;
        category = cat;
    }

    public String toString() {
        return String.format("{Name: %s, ID: %s, Category: %s}", productName, productID, category);
    }
}