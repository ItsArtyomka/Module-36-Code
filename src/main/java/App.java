import entity.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public class App {
    public static void main(String[] args) {
        SessionFactory sessionFactory = null;

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(Event.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        assert sessionFactory != null;
        Session session = sessionFactory.openSession();
        session.beginTransaction();


        session.save(new Event("New Event!", new Date()));
        session.getTransaction()
                .commit();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Event> query = criteriaBuilder.createQuery(Event.class);
        Root<Event> eventRoot = query.from(Event.class);
        query.select(eventRoot);

        // Querying the data
        Query<Event> eventQuery = session.createQuery(query);
        List<Event> events = eventQuery.getResultList();

        // Output
        System.out.println("Event (" + events.get(0).getTitle() + ") : " + events.get(0).getDate());

        // Closing the session
        session.getTransaction();
        session.close();
    }
}
