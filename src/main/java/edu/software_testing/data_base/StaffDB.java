package edu.software_testing.data_base;

import edu.software_testing.model.Department;
import edu.software_testing.model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class StaffDB {

    private static Session session;

    public static void createSession() {
        session = getSessionFactory().openSession();
    }

    public static ObservableList<Employee> getStaff() {
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = builder.createQuery(Employee.class);
        criteria.select(criteria.from(Employee.class));

        List<Employee> result = session.createQuery(criteria).getResultList();
        ObservableList<Employee> staff = FXCollections.observableList(result);
        session.getTransaction().commit();
        return staff;
    }

    public static ObservableList<Department> getDepartments() {
        session.beginTransaction();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Department> criteria = builder.createQuery(Department.class);
        criteria.select(criteria.from(Department.class));

        List<Department> result = session.createQuery(criteria).getResultList();
        ObservableList<Department> departments = FXCollections.observableList(result);
        session.getTransaction().commit();
        return departments;
    }

    public static void remove(ObservableList<Employee> toRemove) {
        session.beginTransaction();
        for(Employee e: toRemove)
            session.delete(e);
        session.getTransaction().commit();
    }

    public static void add(Employee e) {
        session.beginTransaction();
        session.save(e);
        session.getTransaction().commit();
    }

    public static void addDepartment(Department d) {
        session.beginTransaction();
        session.save(d);
        session.getTransaction().commit();
    }

    public static void removeDepartment(ObservableList<Department> toRemove) {

        session.beginTransaction();
        /*Query query = session.createQuery("delete Employee where department = :dep");

        for(Department d: toRemove) {
            query.setParameter("dep", d);
            query.executeUpdate();
            session.delete(d);
        }*/
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = builder.createQuery(Employee.class);
        Root<Employee> root = criteria.from(Employee.class);

        criteria.where(root.get("department").in(toRemove));
        List<Employee> result = session.createQuery(criteria).getResultList();

        for(Employee e: result)
            session.delete(e);
        session.getTransaction().commit();

        session.beginTransaction();
        for(Department d: toRemove)
            session.delete(d);
        session.getTransaction().commit();
    }

    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy( registry );
            throw new ExceptionInInitializerError("Initial SessionFactory failed" + e);
        }
        return sessionFactory;
    }

    public static Session getSession() {
        return session;
    }

    private static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
