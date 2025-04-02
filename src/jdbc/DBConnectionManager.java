package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
    // 오라클 JDBC 연결 정보
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver"; // 정해져있는 값
    private static final String URL = "jdbc:oracle:thin:@3.35.212.217:1521:xe"; //데이터베이스에 요청을 보내는 주소 -> 접속
    private static final String USER = "team4"; // TODO 작성해주세요
    private static final String PASSWORD = "playdatateam4"; // TODO 작성해주세요

    // 정적 초기화자를 사용하여 드라이버를 로드
    static {
        try {
            Class.forName(DRIVER);
//            System.out.println("JDBC 드라이버 강제 구동 완료!");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC 드라이버 로드 실패.");
            e.printStackTrace();
        }
    }

    // 데이터베이스 접속 객체를 리턴해주는 메서드
    // 데이터베이스 접속 객체 Connection을 리턴, 예외를 던짐
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
