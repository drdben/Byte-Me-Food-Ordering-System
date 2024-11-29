# Assignment 4

- - Divyanshi, 2023209

Crazy Canteen is a Java application that simulates a food ordering and management system for a college canteen. It allows customers to browse the menu, place orders, track their order status, and view their order history. Administrators can manage the menu, process orders, and generate daily reports.

## Features

1. **Customer Features**:
   - Browse the menu (full menu, by category, or search by keyword)
   - Add items to the cart and checkout
   - Track the status of pending orders
   - View order history and re-order previous items

2. **Admin Features**:
   - Add, update, and remove menu items
   - View and manage pending orders (by priority for VIP customers)
   - Update order status (received, preparing, out for delivery, completed)
   - Generate daily reports

3. **Other Features**:
   - VIP customer status with priority order handling and monthly subscription
   - Exception handling for invalid inputs
   - Persistent storage of menu, customer, and order data (not implemented in this version)
   
## Features Added in Assignment 4
1. **GUI**
   - Used JFrame, JTable to store Menu and Orders, closing the GUI terminates the app
   - The Orders are preserved across sessions, so terminating the program and restarting it does not affect the previous orders,
   - Data changes through CLI are reflected in GUI through input output stream managements
2. **I/O Stream Management**
   - Save order histories for every user: Used file handling to store the order history of customers ensuring that the order details (such as food items, quantity, and price) are saved. All attributes of customer    class are preserved.
   - Managed users by retrieving their data : When users log in, their data is retrieved and when new users register, their details are added to the file. The system handles both existing
   and new users efficiently.
   - Temporary Cart Storage: Cart is also stored in customers.dat file along with other details in real-time as the user updates the cart.
3. **JUnit Testing**
   - Ordering out-of-stock items: Ensures prevention of customers from ordering out of stock items
   - Invalid login attempts: Tests that admin can not login using wrong password;
   - Cart Operations: Tests the functionality of updating the cart by simulating the following actions:
   ∗ Adding an item to the cart and verifying the total price is updated accurately.
   ∗ Modifying the quantity of an existing item in the cart and confirming that the total price is
   recalculated correctly.
   ∗ Attempting to set an item quantity to a negative value and verifying that the system prevents
   this action.
## Getting Started

To run the Crazy Canteen application, follow these steps:

1. Ensure you have Java 8 or higher installed on your system.
2. Clone the repository to your local machine.
3. Open the project in Intellij
4. Run Miain.java

## Project Structure

The project consists of the following Java classes:

- `Main.java`: The entry point of the application, responsible for initializing the menu, customers, and admin functionality.
- `Account.java`: Implements the basic account functionality, including balance management and transaction history.
- `Admin.java`: Handles the admin-specific features, such as menu management and order processing.
- `Customer.java`: Implements the customer-specific features, such as browsing the menu, placing orders, and viewing order history.
- `FoodItem.java`: Represents a menu item, including its name, price, availability, and review management.
- `Order.java`: Represents a customer order, including the order details, status, and priority (VIP or regular).
- `Review.java`: Represents a customer review for a menu item.
- `WrongFoodTypeException.java`: Custom exception for handling invalid food item types.
- `WrongpwException.java`: Custom exception for handling invalid admin passwords.
