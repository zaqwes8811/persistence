package com.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    
    private static SessionFactory sessionFactory;
    
    static {
        try {
            System.out.println("Initializing Hibernate 6.x...");
            
            // Create registry with explicit configuration
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();
            
            System.out.println("✓ ServiceRegistry created");
            
            // Create MetadataSources
            MetadataSources sources = new MetadataSources(registry);
            
            // Add annotated classes
            sources.addAnnotatedClass(com.example.entity.User.class);
            sources.addAnnotatedClass(com.example.entity.Product.class);
            
            System.out.println("✓ Entity classes registered");
            
            // Build metadata
            Metadata metadata = sources.getMetadataBuilder().build();
            
            System.out.println("✓ Metadata created");
            
            // Build SessionFactory
            sessionFactory = metadata.getSessionFactoryBuilder().build();
            
            System.out.println("✓ SessionFactory created successfully!");
            
        } catch (Exception e) {
            System.err.println("SessionFactory creation failed: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
                System.out.println("SessionFactory closed");
            } catch (Exception e) {
                System.err.println("Error closing SessionFactory: " + e.getMessage());
            }
        }
    }
}