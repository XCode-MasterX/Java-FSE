package InventoryManagementSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/*
    Scenario: 
        You are developing an inventory management system for a warehouse. Efficient data storage and retrieval are crucial.

    Steps:
        1.	Understand the Problem:
        	Explain why data structures and algorithms are essential in handling large inventories.
        	Discuss the types of data structures suitable for this problem.

        2.	Setup:
        	Create a new project for the inventory management system.

        3.	Implementation:
        	Define a class Product with attributes like productId, productName, quantity, and price.
        	Choose an appropriate data structure to store the products (e.g., ArrayList, HashMap).
        	Implement methods to add, update, and delete products from the inventory.

        4.	Analysis:
    	    Analyze the time complexity of each operation (add, update, delete) in your chosen data structure.
    	    Discuss how you can optimize these operations.

*/

public class InventoryManagementSystem {
    static Scanner in = new Scanner(System.in);
    static final String INVENTORY_FILE_NAME = "inventory.dat";

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
        System.out.println("1. Load previous session");
        System.out.println("2. Save current session");
        System.out.println("3. Add");
        System.out.println("4. Update");
        System.out.println("5. Delete");
        System.out.println("6. Show Inventory");
        System.out.println("7. Exit");

        return inputValidator(1, 7);
    }

    public static void main(String args[]) {
        HashMap<String, Product> warehouse = new HashMap<>();
        boolean saved = false;

        menuLoop:
        while(true) {
            int choice = getChoice();

            switch(choice) {
                case 1:
                    warehouse = loadInventory();
                    break;
                case 2:
                    saved = true;
                    saveInventory(warehouse);
                    break;
                case 3:
                    saved = false;
                    warehouse = addProduct(warehouse);
                    break;
                case 4:
                    saved = false;
                    warehouse = updateProduct(warehouse);
                    break;
                case 5:
                    saved = false;
                    warehouse = deleteProduct(warehouse);
                    break;
                case 6:
                    Arrays.stream(warehouse.entrySet().toArray()).forEach(System.out::println);
                    
                    break;
                case 7:
                    break menuLoop;
                default:
                    System.out.println("How did this even happen??");
                    break;
            }
        }

        if(!saved)
        {
            System.out.println("Would you like to save the changes made? [Y/N]");
            char c = Character.toUpperCase(in.nextLine().charAt(0));
            
            while(c != 'Y' && c != 'N') {
                System.out.println("Not recognised... Try again.");
                c = Character.toUpperCase(in.nextLine().charAt(0));
            }

            if(c == 'Y')
                saveInventory(warehouse);
        }

        System.out.println("Closing the application..."); 
        in.close();
    }

    public static HashMap<String, Product> addProduct(HashMap<String, Product> warehouse) {
        //flushScanner();

        System.out.print("Enter the ID for the product: ");
        String id = in.nextLine().trim();
        id = id.isEmpty() ? in.nextLine() : id;


        if(warehouse.containsKey(id)) {
            System.out.printf("There's a product that has the ID (%s). Do you want to overwrite? [Y/N]\n", id);
            
            if(Character.toUpperCase(in.next().charAt(0)) == 'N')
                return warehouse;
        }

        System.out.println("Enter the NAME of the product: ");
        String name = in.nextLine();
        
        System.out.println("Enter the QUANTITY of the product: ");
        int quantity = in.nextInt();

        System.out.println("Enter the PRICE of the product: ");
        float price = in.nextFloat();

        warehouse.put(id, new Product(id, name, quantity, price));

        return warehouse;
    }

    public static HashMap<String, Product> updateProduct(HashMap<String, Product> warehouse) {
        System.out.println("Enter the product's ID that you want to update: ");
        String prodID = in.nextLine().trim();
        prodID = prodID.isEmpty() ? in.nextLine() : prodID;
        
        if(!warehouse.containsKey(prodID)) {
            System.out.println("There's no such product in the inventory...");
            return warehouse;
        }

        System.out.println("Enter the attribute you would like to update: ");
        System.out.println("1. Product Name");
        System.out.println("2. Quantity");
        System.out.println("3. Price");

        int choice = inputValidator(1, 3);

        switch(choice) {
            case 1:
                String newName = in.nextLine();
                warehouse.get(prodID).setName(newName);
                break;
            case 2:
                warehouse.get(prodID).setQuantity(in.nextInt());
                break;
            case 3:
                warehouse.get(prodID).setPrice(in.nextFloat());
                break;
            default:
                System.out.println("Wait... How did this happen?? Mission ABORT.");
        }

        return warehouse;
    }

    public static HashMap<String, Product> deleteProduct(HashMap<String, Product> warehouse) {
        System.out.println("Enter the product's ID that you want to delete: ");
        String prodID = in.nextLine().trim();
        prodID = prodID.isEmpty() ? in.nextLine() : prodID;
        
        System.out.println("Looking for product...");
        
        if(warehouse.containsKey(prodID)) {
            warehouse.remove(prodID);
            System.out.println("Item removed from inventory...");
        }
        else
            System.out.println("Item not found in warehouse listing...");

        return warehouse;
    }

    public static void saveInventory(HashMap<String, Product> warehouse) {
        File f = new File(INVENTORY_FILE_NAME);
        
        try {
            if(!f.exists()) f.createNewFile();
            
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(warehouse);
            oos.flush();
            oos.close();

            System.out.println("Inventory has been saved to the file system.");
        }
        catch(IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static HashMap<String, Product> loadInventory() {
        File f = new File(INVENTORY_FILE_NAME);
        if(!f.exists()) { 
            System.out.println("No previous session was found...\n");
            return new HashMap<String, Product>();
        }

        HashMap<String, Product> warehouse = null;

        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object obj = ois.readObject();

            if(obj instanceof HashMap)
                warehouse = (HashMap<String, Product>) obj;

            ois.close();
        }
        catch(IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return warehouse;
    }
}