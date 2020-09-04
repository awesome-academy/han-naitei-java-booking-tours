package com.bookingTour.model;

import com.bookingTour.validator.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "{user.validation.password.notmatch}")
})
@UniqueField.List({
        @UniqueField(field = "email", column = "email", table = "User", message = "{user.validation.email.exist}"),
        @UniqueField(field = "userName", column = "user_name", table = "User", message = "{user.validation.userName.exist}")
})
@CorrectPassword(name = "oldPassword", message = "{user.validation.oldPassword.incorrect}")
public class UserModel extends BaseModel {

    private Long id;
    @NotEmpty(message = "{user.validation.email.required}")
    @Email(message = "{pattern.email}")
    private String email;
    @NotEmpty(message = "{user.validation.userName.required}")
    @Size(max = 64, message = "{user.validation.userName.max}")
    private String userName;
    @NullOrNotBlank(message = "{user.validation.password.required}")
    @Size(max = 32, min = 8, message = "{user.validation.password.length}")
    private String password;
    @NullOrNotBlank(message = "{user.validation.confirmPassword.required}")
    private String confirmPassword;
    private String oldPassword;
    @NullOrNotBlank(message = "{user.validation.name.required}")
    private String name;
    private String avatar;
    @NullOrNotBlank(message = "{user.validation.phoneNumber.required}")
    @PhoneNumber(message = "{user.validation.phoneNumber.length}")
    private String phoneNumber;
    private Integer role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
