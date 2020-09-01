package com.bookingTour.service.imp;

import com.bookingTour.dao.imp.UserDAOImp;
import com.bookingTour.entity.User;
import com.bookingTour.model.UserModel;
import com.bookingTour.model.enu.Role;
import com.bookingTour.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImp implements UserService {
    private static Logger log = LoggerFactory.getLogger(UserServiceImp.class);

    private UserDAOImp userDAOImp;

    public void setUserDAOImp(UserDAOImp userDAOImp) {
        this.userDAOImp = userDAOImp;
    }

    @Override
    public UserModel findUser(Long id) {
        log.info("Checking the user in the database");
        try {
            User user = userDAOImp.find(id);
            return convertBean(user);
        } catch (Exception e) {
            log.error("An error occurred while fetching the user details from the database", e);
            return null;
        }
    }

    @Override
    public UserModel findUserByUserName(String userName) {
        log.info("Fetching the user by username in the database");
        try {
            User user = userDAOImp.findUserByUserName(userName);
            return convertBean(user);
        } catch (Exception e) {
            log.error("An error occurred while fetching the user details by username from the database", e);
            return null;
        }
    }

    @Override
    public UserModel findUserByEmail(String email) {
        log.info("Fetching the user by email in the database");
        try {
            User user = userDAOImp.findUserByEmail(email);
            return convertBean(user);
        } catch (Exception e) {
            log.error("An error occurred while fetching the user details by email from the database", e);
            return null;
        }
    }

    @Override
    public boolean existingUserName(String userName, Long id) {
        log.info("Checking the user by username in the database");
        try {
            return userDAOImp.existingUserName(userName, id);
        } catch (Exception e) {
            log.error("An error occurred while checking the user details by username from the database", e);
            return true;
        }
    }

    @Override
    public boolean existingEmail(String email, Long id) {
        log.info("Checking the user by email in the database");
        try {
            return userDAOImp.existingEmail(email, id);
        } catch (Exception e) {
            log.error("An error occurred while checking the user details by email from the database", e);
            return true;
        }
    }

    @Override
    public boolean checkPassword(String password, Long id) {
        log.info("Checking the password of the user in the database");
        try {
            return userDAOImp.checkPassword(password, id);
        } catch (Exception e) {
            log.error("An error occurred while checking the password of the user from the database", e);
            return true;
        }
    }

    @Transactional
    public UserModel addUser(UserModel userModel) throws Exception {
        log.info("Adding the user in the database");
        try {
            User user = new User();
            user.setId(userModel.getId());
            user.setEmail(userModel.getEmail());
            user.setUserName(userModel.getUserName());
            user.setPassword(userModel.getPassword());
            user.setName(userModel.getName());
            user.setPhoneNumber(userModel.getPhoneNumber());
            user.setRole(Role.USER_ROLE);
            user = userDAOImp.makePersistent(user);
            return convertBean(user);
        } catch (Exception e) {
            log.error("An error occurred while adding the user details to the database", e);
            throw e;
        }
    }

    @Transactional
    public UserModel editUser(UserModel userModel) throws Exception {
        log.info("Updating the user in the database");
        try {
            User user = userDAOImp.find(userModel.getId(), true);
            if (StringUtils.hasText(userModel.getName())) {
                user.setName(userModel.getName());
            }
            if (StringUtils.hasText(userModel.getPhoneNumber())) {
                user.setPhoneNumber(userModel.getPhoneNumber());
            }
            userDAOImp.makePersistent(user);
            return userModel;
        } catch (Exception e) {
            log.error("An error occurred while updating the user details to the database", e);
            throw e;
        }
    }

    @Transactional
    public boolean deleteUser(UserModel userModel) throws Exception {
        log.info("Deleting the user in the database");
        try {
            User user = userDAOImp.find(userModel.getId(), true);
            userDAOImp.makeTransient(user);
            return true;
        } catch (Exception e) {
            log.error("An error occurred while adding the user details to the database", e);
            throw e;
        }
    }

    public List<UserModel> findAll() {
        log.info("Fetching all users in the database");
        List<UserModel> userModelList = new ArrayList<UserModel>();
        try {
            List<User> userList = userDAOImp.findAll();
            for (User user : userList) {
                UserModel userModel = new UserModel();
                BeanUtils.copyProperties(user, userModel);
                userModelList.add(userModel);
            }
        } catch (Exception e) {
            log.error("An error occurred while fetching all users from the database", e);
        }
        return userModelList;
    }

    @Override
    public Page<UserModel> paginate(UserModel userModel) {
        try {
            return userDAOImp.paginate(userModel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private UserModel convertBean(User user) {
        UserModel userModel = null;
        if (user != null) {
            userModel = new UserModel();
            BeanUtils.copyProperties(user, userModel);
        }
        return userModel;
    }
}
