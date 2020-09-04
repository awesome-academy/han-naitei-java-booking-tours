package com.bookingTour.dao;

import com.bookingTour.entity.User;
import com.bookingTour.model.UserModel;
import org.springframework.data.domain.Page;

public interface UserDAO extends GenericDAO<User, Long> {
    public User findUser(User user);

    public User findUserByUserName(String userName);

    public User findUserByEmail(String email);

    public boolean checkPassword(String password, Long id);

    public Page<UserModel> paginate(UserModel userModel) throws Exception;
}
