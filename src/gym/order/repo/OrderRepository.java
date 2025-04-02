package gym.order.repo;

import gym.employee.domain.Employee;
import gym.membership.domain.Membership;
import gym.order.domain.Order;
import gym.product.domain.Product;
import gym.user.domain.User;
import jdbc.DBConnectionManager;

import java.io.PipedInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    /**
     * 회원원권 결재
     *
     * @param userId
     * @param membershipId
     * @param employeeId
     * @return
     */
    public boolean insertOrder(int userId, int membershipId, int employeeId) {
        String query =
                "INSERT INTO orders (order_id, user_id, membership_id, order_date, employee_id) " +
                        "VALUES (orders_seq.NEXTVAL, ?, ?, SYSDATE, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, membershipId);
            pstmt.setInt(3, employeeId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 상품 결재
     *
     * @param userId
     * @param membershipId
     * @param employeeId
     * @param productId
     * @return
     */
    public boolean insertOrder(int userId, int membershipId, int employeeId, int productId) {
        String query =
                "INSERT INTO orders (order_id, user_id, membership_id, product_id, order_date, employee_id) " +
                        "VALUES (orders_seq.NEXTVAL, ?, ?, ?, SYSDATE, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, membershipId);
            pstmt.setInt(3, productId);
            pstmt.setInt(4, employeeId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getOrderList() {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM orders o " +
                "JOIN users u " +
                "ON o.user_id = u.user_id " +
                "JOIN memberships m " +
                "ON o.membership_id = m.membership_id " +
                "JOIN employees e " +
                "ON o.employee_id = e.employee_id " +
                "LEFT JOIN products p " +
                "ON o.product_id = p.product_id ";

        try (Connection conn = DBConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                int userId = rs.getInt("user_id");
                int membershipId = rs.getInt("membership_id");
                int productId = rs.getInt("product_id");

                Order order = new Order(
                        orderId,
                        userId,
                        membershipId,
                        productId,
                        LocalDate.now()
                );
                order.setUser(new User(
                        rs.getString("user_name"),
                        rs.getString("phone_number")
                ));
                order.setEmployee(new Employee(
                        0, rs.getString("employee_name"),
                        rs.getString("part")
                ));
                order.setMembership(new Membership(
                        rs.getInt("period"),
                        0 // 추후 수정
                ));
                order.setProduct(new Product(
                        rs.getString("product_name"),
                        0 // 추후 수정
                ));

                orderList.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }
}
