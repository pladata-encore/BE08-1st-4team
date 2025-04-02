package gym.access.repo;

import gym.access.domain.Access;
import gym.user.domain.Status;
import gym.user.domain.User;
import jdbc.DBConnectionManager;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessRepository {
    public List<User> searchUserByPhoneNumber(String phoneBackNum) {
        List<User> userList = new ArrayList<>();

        String sql = "SELECT * FROM users WHERE phone_number LIKE ? AND user_active = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + phoneBackNum);
            pstmt.setString(2, "Y");

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                userList.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("phone_number"),
                        rs.getDate("regist_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    public Status checkUserStatus(User user) {
        Status status = null;
        String sql = "SELECT * FROM status s JOIN users u ON s.user_id = u.user_id WHERE s.user_id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, user.getUserId());

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                status = new Status(
                        rs.getInt("user_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getInt("remained_month"),
                        rs.getInt("product_count")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }

    public void addAccessData(User user) {
        String sql = "INSERT INTO accesses VALUES (accesses_seq.NEXTVAL, ?, ?) ";
        try (Connection conn = DBConnectionManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getUserId());
            pstmt.setDate(2, Date.valueOf(LocalDate.now()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Access, User> searchAccessByDate(String yearStr, String monthStr) {
        Map<Access, User> map = new HashMap<>();
        String sql = "SELECT * FROM accesses a JOIN users u ON a.user_id = u.user_id " +
                "WHERE a.access_date BETWEEN TO_DATE(?, 'YYYYMMDD') AND TO_DATE(?, 'YYYYMMDD') - 1 ";
        int year = Integer.parseInt(yearStr);
        int month = Integer.parseInt(monthStr);

        String afterYear = "";
        String afterMonth = "";

        if(month == 12) {
            afterYear = Integer.toString(++year);
            afterMonth = "01";
        } else {
            afterYear = yearStr;
            if(month < 10) {
                monthStr = "0" + month;
                afterMonth = month < 9 ? "0" + (++month) : "10";
            }
        }

        String beforeDate = yearStr + monthStr + "01";
        String afterDate = afterYear + afterMonth + "01";


        try (Connection conn = DBConnectionManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, beforeDate);
            pstmt.setString(2, afterDate);

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                map.put(new Access(
                        rs.getInt("access_id"),
                        rs.getInt("user_id"),
                        rs.getDate("access_date").toLocalDate()
                ), new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("phone_number"),
                        rs.getDate("regist_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return map;
    }

    public void updateUserProductCountStatus(User user) {
        String sql = "UPDATE status SET product_count = product_count - 1 WHERE user_id = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getUserId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserMembershipCountStatus(User user, int period) {
        String sql = "UPDATE status SET remained_month = ? WHERE user_id = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getUserId());
            pstmt.setInt(2, period);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
