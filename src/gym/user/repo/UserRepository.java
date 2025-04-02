package gym.user.repo;

import gym.user.domain.User;
import jdbc.DBConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    public void addUser(User user) {
        String sql = "INSERT INTO users VALUES(users_seq.NEXTVAL, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBConnectionManager.getConnection();
            conn.setAutoCommit(false);

            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPhoneNumber());
            pstmt.setDate(3, Date.valueOf(user.getRegistDate()));
            pstmt.setString(4, user.isUserActive() ? "Y" : "N");

            pstmt.executeUpdate();

            userStatus(conn, user);

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void userStatus(Connection conn, User user) throws Exception {
//        String selectSql = "SELECT users_seq.NEXTVAL FROM DUAL ";
//        String insertStatusSql = "INSERT INTO gym.status (user_id, start_date, remained_month, product_count)" +
//                "VALUES(?, ?, ?, ?)INSERT INTO gym.status (user_id, start_date, remained_month, product_count)" +
//                "VALUES(?, ?, ?, ?)";
        String sql = "INSERT INTO status (user_id, start_date, remained_month, product_count) " +
                "VALUES (users_seq.CURRVAL, ?, ?, ?)";

        try(PreparedStatement Pstmt = conn.prepareStatement(sql)){

            Pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            Pstmt.setInt(2, 0);
            Pstmt.setInt(3, 0);

            Pstmt.executeUpdate();
           /* insertPstmt.setInt(1,  userId);
            insertPstmt.setDate(2, Date.valueOf(LocalDate.now()));
            insertPstmt.setInt(3, 0);
            insertPstmt.setInt(4, 0);*/


//            insertPstmt.executeUpdate();
        }
    }



    public void Activation() { //회원 활성화 여부
        String userUpdateSql = "UPDATE users u JOIN status s ON u.user_id = s.user_id " +
                "SET u.user_active = 'N' " +
                "WHERE s.product_count = 0 AND s.remained_month = 0";

        String statusUpdateSql = "UPDATE status s " +
                "JOIN users u ON s.user_id = u.user_id " +
                "SET s.last_updated = CURRENT_TIMESTAMP " +
                "WHERE s.product_count = 0 AND s.remained_month = 0";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement userPstmt = conn.prepareStatement(userUpdateSql);
             PreparedStatement statusPstmt = conn.prepareStatement(statusUpdateSql)) {

            // 유저 테이블
            userPstmt.executeUpdate();

            // 상태 테이블
            statusPstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public List<User> findByUserName(String name, boolean flag) {
        List<User> userList = new ArrayList<>();
        String sql = flag ? "SELECT * FROM users WHERE user_name = ? AND user_active = ?" :
                "SELECT * FROM users WHERE user_name = ? AND user_active IN(?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, "Y");
            if(!flag) {
                pstmt.setString(3, "N");
            }

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

    public void updateUserInfo(User user) {
        String sql = "UPDATE users SET user_name = ?, phone_number = ? WHERE user_id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPhoneNumber());
            pstmt.setInt(3, user.getUserId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserActive(User user, boolean b) {
        String sql = "UPDATE users SET user_active = ? WHERE user_id = ?";
        try(Connection conn = DBConnectionManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, b ? "Y" : "N");
            pstmt.setInt(2, user.getUserId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserActive(int userId, boolean b) {
        String sql = "UPDATE users SET user_active = ? WHERE user_id = ?";
        try(Connection conn = DBConnectionManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, b ? "Y" : "N");
            pstmt.setInt(2, userId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> findAllUser() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DBConnectionManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                userList.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("phone_number"),
                        rs.getDate("regist_date").toLocalDate(),
                        rs.getString("user_active").equals("Y")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }
}
