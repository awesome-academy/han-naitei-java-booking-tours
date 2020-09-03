package com.bookingTour.dao.imp;

import com.bookingTour.dao.UserDAO;
import com.bookingTour.entity.User;
import com.bookingTour.model.UserModel;
import com.bookingTour.service.imp.UserServiceImp;
import com.bookingTour.util.CommonUtil;
import com.bookingTour.util.SearchQueryTemplate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImp extends GenericDAOImp<User, Long> implements UserDAO {
    private static Logger log = LoggerFactory.getLogger(UserServiceImp.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDAOImp() {
        super(User.class);
    }

    public User findUser(User user) {
        log.info("Finding the user in the database");
        List<User> userList = (List<User>) getHibernateTemplate().findByExample(user);
        if (!CommonUtil.isEmpty(userList)) {
            return userList.get(0);
        }
        return null;
    }

    public User findUserByUserName(String userName) {
        log.info("Finding the user by username in the database");
        return getHibernateTemplate().execute(new HibernateCallback<User>() {
            public User doInHibernate(Session session) throws HibernateException {
                Query<User> query = session.createQuery("FROM User u WHERE u.userName = :user_name", User.class);
                query.setParameter("user_name", userName);
                return query.uniqueResult();
            }
        });
    }

    public User findUserByEmail(String email) {
        log.info("Finding the user by email in the database");
        return getHibernateTemplate().execute(new HibernateCallback<User>() {
            public User doInHibernate(Session session) throws HibernateException {
                Query<User> query = session.createQuery("FROM User u WHERE u.email = :email", User.class);
                query.setParameter("email", email);
                return query.uniqueResult();
            }
        });
    }

    public boolean existingUserName(String userName, Long id) {
        log.info("Finding the user by username in the database");
        return getHibernateTemplate().execute(new HibernateCallback<Long>() {
            public Long doInHibernate(Session session) throws HibernateException {
                String sql = "SELECT COUNT(*) FROM User WHERE user_name = :user_name";
                if (id != null) {
                    sql += " AND id <> :id";
                }
                Query<Long> query = session.createQuery(sql, Long.class);
                query.setParameter("user_name", userName);
                if (id != null) {
                    query.setParameter("id", id);
                }
                return query.uniqueResult();
            }
        }) > 0;
    }

    public boolean existingEmail(String email, Long id) {
        log.info("Finding the user by email in the database");
        return getHibernateTemplate().execute(new HibernateCallback<Long>() {
            public Long doInHibernate(Session session) throws HibernateException {
                String sql = "SELECT COUNT(*) FROM User WHERE email = :email";
                if (id != null) {
                    sql += " AND id <> :id";
                }
                Query<Long> query = session.createQuery(sql, Long.class);
                query.setParameter("email", email);
                if (id != null) {
                    query.setParameter("id", id);
                }
                return query.uniqueResult();
            }
        }) > 0;
    }

    public boolean checkPassword(String password, Long id) {
        log.info("Checking the password of the user in the database");
        String encodePassword = getHibernateTemplate().execute(new HibernateCallback<User>() {
            public User doInHibernate(Session session) throws HibernateException {
                Query<User> query = session.createQuery("FROM User u WHERE u.id = :id", User.class);
                query.setParameter("id", id);
                return query.uniqueResult();
            }
        }).getPassword();
        return passwordEncoder.matches(password, encodePassword);
    }

    @Override
    public Page<UserModel> paginate(UserModel userModel) throws Exception {
        log.info("Paging the users in the database");
        SearchQueryTemplate searchQueryTemplate = new SearchQueryTemplate();
        searchQueryTemplate.setSql("FROM User");
        searchQueryTemplate.setCountSql("SELECT COUNT(*) FROM User");
        searchQueryTemplate.setPageable(userModel.getPageable());
        Page<User> users = find(searchQueryTemplate);
        return users.map(user -> {
            UserModel model = new UserModel();
            BeanUtils.copyProperties(user, model);
            return model;
        });
    }
}
