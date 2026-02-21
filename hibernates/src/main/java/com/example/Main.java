package com.example;

import com.example.entity.User;
import com.example.entity.Product;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("Starting Hibernate SQLite Demo...");
        
        // Test CRUD operations
        createUsersAndProducts();
        readUsers();
        readProducts();
        updateUser();
        deleteProduct();
        
        // Shutdown Hibernate
        HibernateUtil.shutdown();
    }
    
    private static void createUsersAndProducts() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            
            // Create users
            User user1 = new User("john_doe", "john@example.com", "password123");
            user1.setFullName("John Doe");
            user1.setBirthDate(new Date(90, 5, 15)); // June 15, 1990
            
            User user2 = new User("jane_smith", "jane@example.com", "secure456");
            user2.setFullName("Jane Smith");
            user2.setBirthDate(new Date(92, 8, 23)); // September 23, 1992
            
            // Create products for user1
            Product product1 = new Product("Laptop", new BigDecimal("999.99"), 1);
            product1.setDescription("High-performance laptop with 16GB RAM");
            
            Product product2 = new Product("Mouse", new BigDecimal("29.99"), 2);
            product2.setDescription("Wireless optical mouse");
            
            // Associate products with user1
            user1.addProduct(product1);
            user1.addProduct(product2);
            
            // Create product for user2
            Product product3 = new Product("Keyboard", new BigDecimal("79.99"), 1);
            product3.setDescription("Mechanical gaming keyboard");
            user2.addProduct(product3);
            
            // Save users (cascades to products)
            session.save(user1);
            session.save(user2);
            
            transaction.commit();
            System.out.println("Users and products created successfully!");
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    private static void readUsers() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            // HQL query to get all users
            Query<User> query = session.createQuery("FROM User", User.class);
            List<User> users = query.list();
            
            System.out.println("\n=== All Users ===");
            for (User user : users) {
                System.out.println(user);
                System.out.println("  Products count: " + user.getProducts().size());
            }
            
            // Named query example (if you want to add @NamedQuery to User entity)
            // Query<User> query2 = session.createNamedQuery("User.findByUsername", User.class);
            // query2.setParameter("username", "john_doe");
            // User user = query2.uniqueResult();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    private static void readProducts() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            // Get all products with their owners
            Query<Product> query = session.createQuery(
                "FROM Product p LEFT JOIN FETCH p.user", Product.class);
            List<Product> products = query.list();
            
            System.out.println("\n=== All Products ===");
            for (Product product : products) {
                System.out.println(product);
                if (product.getUser() != null) {
                    System.out.println("  Owned by: " + product.getUser().getUsername());
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    private static void updateUser() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            
            // Find user by username
            Query<User> query = session.createQuery(
                "FROM User WHERE username = :username", User.class);
            query.setParameter("username", "john_doe");
            User user = query.uniqueResult();
            
            if (user != null) {
                user.setFullName("John Updated Doe");
                session.update(user);
                System.out.println("\nUser updated successfully!");
            }
            
            transaction.commit();
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    private static void deleteProduct() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            
            // Delete a product
            Query<Product> query = session.createQuery(
                "FROM Product WHERE name = :name", Product.class);
            query.setParameter("name", "Mouse");
            Product product = query.uniqueResult();
            
            if (product != null) {
                session.delete(product);
                System.out.println("\nProduct deleted successfully!");
            }
            
            transaction.commit();
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}