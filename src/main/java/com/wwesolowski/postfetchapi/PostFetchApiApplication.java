package com.wwesolowski.postfetchapi;

import com.wwesolowski.postfetchapi.model.Post;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PostFetchApiApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {

        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .configure("hibernate_cfg.xml")
                        .build();

        SessionFactory sessionFactory = null;

        try {
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            System.exit(1);
        }
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try {
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            session.close();
        }

        sessionFactory.close();
        SpringApplication.run(PostFetchApiApplication.class, args);
    }

}
