package com.example;

import com.example.entity.User;
import com.example.entity.Product;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.math.BigDecimal;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("=== Hibernate 6 SQLite Demo ===");
        
        // Test database connection and operations
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("✓ Database connected");
            
            // Create test data
            Transaction tx = session.beginTransaction();
            
            // Check if data exists
            Long userCount = session.createQuery("SELECT COUNT(u) FROM User u", Long.class).uniqueResult();
            
            if (userCount == 0) {
                System.out.println("Creating test data...");
                
                User user = new User("testuser", "test@example.com", "password");
                session.persist(user);
                
                Product product1 = new Product("Test Product 1", new BigDecimal("19.99"));
                product1.setUser(user);
                session.persist(product1);
                
                Product product2 = new Product("Test Product 2", new BigDecimal("29.99"));
                product2.setUser(user);
                session.persist(product2);
                
                System.out.println("✓ Test data created");
            }
            
            tx.commit();
            
            // Query and display data
            List<User> users = session.createQuery("FROM User", User.class).list();
            System.out.println("\nUsers in database:");
            for (User u : users) {
                System.out.println("  - " + u.getUsername() + " (" + u.getEmail() + ")");
                
                List<Product> products = session.createQuery(
                    "FROM Product p WHERE p.user.id = :userId", Product.class)
                    .setParameter("userId", u.getId())
                    .list();
                
                for (Product p : products) {
                    System.out.println("      * " + p.getName() + " - $" + p.getPrice());
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
        
        System.out.println("=== Demo completed ===");
    }
}