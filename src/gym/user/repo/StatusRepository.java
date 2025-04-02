package gym.user.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import gym.user.domain.User;
import jdbc.DBConnectionManager;
import gym.user.domain.Status;

public class StatusRepository {
    UserRepository userRepository = new UserRepository();

    public Status findByRemainedMonth(int userId) {
        String sql = "SELECT * FROM status WHERE user_id = ?";

        try (Connection conn = DBConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Status(
                        rs.getInt("user_id"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getInt("remained_month"),
                        rs.getInt("product_count")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveStatus(boolean boolMonth, int userId, int remainedMonth, int productCount) {
        String sql = boolMonth
                ? "UPDATE status SET remained_month = ?, product_count = ? WHERE user_id = ?"
                : "INSERT INTO status (user_id, start_date, product_count, remained_month) VALUES (?, SYSDATE, ?, ?)";



        try (Connection conn = DBConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, boolMonth ? remainedMonth : userId);
            pstmt.setInt(2, productCount);
            pstmt.setInt(3, boolMonth ? userId : remainedMonth);

            pstmt.executeUpdate();

            userRepository.updateUserActive(userId, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
