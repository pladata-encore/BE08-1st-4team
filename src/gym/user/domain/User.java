package gym.user.domain;

import java.time.LocalDate;

public class User {
    private int userId;
    private String userName;
    private String phoneNumber;
    private LocalDate registDate;
    private boolean userActive;




    public User(String userName, String phoneNumber) {
        this.userId = 0;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.registDate = LocalDate.now();
        this.userActive = true;
    }

    public User(int userId, String userName, String phoneNumber, LocalDate registDate) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.registDate = registDate;
    }

    public User(int userId, String userName, String phoneNumber, LocalDate registDate, boolean userActive) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.registDate = registDate;
        this.userActive = userActive;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getRegistDate() {
        return registDate;
    }

    public void setRegistDate(LocalDate registDate) {
        this.registDate = registDate;
    }

    public boolean isUserActive() {
        return userActive;
    }

    public void setUserActive(boolean userActive) {
        this.userActive = userActive;
    }
}
