package com.bookingTour.service;

import com.bookingTour.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.List;

public interface UserService extends UserDetailsService, PersistentTokenRepository {
    public UserModel findUser(Long id);

    public UserModel findUserByUserName(String userName);

    public UserModel findUserByEmail(String email);

    public boolean checkExisted(Long id, String field, String value, String column);

    public boolean checkPassword(String password, Long id);

    public UserModel addUser(UserModel user) throws Exception;

    public UserModel editUser(UserModel userModel) throws Exception;

    public UserModel changePassword(UserModel userModel) throws Exception;

    public boolean deleteUser(UserModel userModel) throws Exception;

    public List<UserModel> findAll();

    public Page<UserModel> paginate(UserModel userModel);
}
