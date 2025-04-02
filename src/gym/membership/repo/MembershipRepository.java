package gym.membership.repo;

import gym.membership.domain.Membership;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jdbc.DBConnectionManager;

public class MembershipRepository {

    /**
     * 회원권 조회
     */
    public List<Membership> findAll() {
        List<Membership> membershipList = new ArrayList<>();
        String sql = "SELECT * FROM memberships";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                membershipList.add(new Membership(
                        rs.getInt("price"),
                        rs.getInt("period"),
                        rs.getInt("membership_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membershipList;
    }

    /**
     * 회원권 종류 추가
     */
    public void addMembership(Membership membership) {
        String sql = "INSERT INTO memberships VALUES(memberships_seq.NEXTVAL, ?, ?)";

        try (Connection conn = DBConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, membership.getPeriod());
            pstmt.setInt(2, membership.getPrice());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
