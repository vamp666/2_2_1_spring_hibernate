package hiber.dao;

import hiber.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      Session session = sessionFactory.openSession();
      session.beginTransaction();

      session.save(user);
      session.save(user.getCar());

      session.getTransaction().commit();
      session.close();

   }


   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }


   @Override
   public User getOwner(String model, int series) {
      Session session = sessionFactory.openSession();
      String hql = "SELECT u " +
              "FROM User u " +
              "LEFT JOIN u.car c " +
              "WHERE c.model = :model " +
              "AND c.series = :series";
      org.hibernate.query.Query query = session.createQuery(hql);
      query.setParameter("model", model);
      query.setParameter("series", series);

      User user = (User) query.list().get(0);
      return user;
   }

}
