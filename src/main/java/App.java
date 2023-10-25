import entity.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Date;
import java.util.List;

@SuppressWarnings({"unchecked", "DataFlowIssue", "ConstantValue", "JpaQlInspection"})
public class App {
    public static void main(String[] args) {

        // Needed for work with DB
        SessionFactory sessionFactory = null;

        // Helps to configure Hibernate
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();

        // Building the SessionFactory
        try {
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(Event.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        // Work with operations on DB
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        // Saving a new event in the DB
        session.save(new Event("Hibernate Learning", new Date()));
        session.getTransaction()
                .commit(); // Saving the changes
        session.close();

        // Process of reading the data
        session = sessionFactory.openSession();
        session.beginTransaction();
        List<Event> result = session.createQuery("from Event").list(); // Getting all the data from EVENT table
        // Iterating through all the data
        for (Event event : result) {
            System.out.println("Event (" + event.getDate() + ") : " + event.getTitle()); // Output of events
        }
        // Finishing the work with DB
        session.getTransaction().commit();
        session.close();

        // NullPointerException safety
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
