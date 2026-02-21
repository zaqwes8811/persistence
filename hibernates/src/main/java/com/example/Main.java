package com.example;

import com.example.entity.User;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create and save a user
        createUser();
        
        // Read all users
        readAllUsers();
        
        // Update a user
        updateUser(1L);
        
        // Delete a user
        deleteUser(1L);
        
        // Shutdown Hibernate
        HibernateUtil.shutdown();
    }
    
    private static void createUser() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            User user = new User("john_doe", "john@example.com");
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setAge(30);
            
            session.save(user);
            transaction.commit();
            
            System.out.println("User created: " + user);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    private static void readAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User", User.class);
            List<User> users = query.list();
            
            System.out.println("\nAll Users:");
            users.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void updateUser(Long userId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            User user = session.get(User.class, userId);
            if (user != null) {
                user.setAge(31);
                session.update(user);
                System.out.println("User updated: " + user);
            }
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    
    private static void deleteUser(Long userId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            User user = session.get(User.class, userId);
            if (user != null) {
                session.delete(user);
                System.out.println("User deleted with ID: " + userId);
            }
            
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}