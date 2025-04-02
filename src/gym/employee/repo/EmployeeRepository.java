package gym.employee.repo;

import gym.employee.domain.Employee;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import gym.employee.service.EmployeeService;
import jdbc.DBConnectionManager;
import java.sql.*;
import java.util.Scanner;


public class EmployeeRepository {

    // 직원 조회
    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE employee_active = 'Y'";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(rs.getInt("employee_id"));
                employee.setEmployeeName(rs.getString("employee_name"));
                employee.setPart(rs.getString("part"));

                // "Y" 값을 boolean으로 변환
                employee.setEmployeeActive(true);


                employeeList.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employeeList;
    }

    // 직원 추가
    public boolean addEmployee(Employee employee) {
        if (employee.getEmployeeName() == null || employee.getEmployeeName().isEmpty()) {
            System.out.println("Employee name is missing. Please provide a valid name.");
            return false; // 실패 처리
        }

        String sql = "INSERT INTO employees (employee_id, employee_name, part, employee_active) VALUES (employees_seq.NEXTVAL, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employee.getEmployeeName());
            ps.setString(2, employee.getPart());
            ps.setString(3, "Y"); // Active 상태를 항상 "Y"로 설정

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    // 직원 수정
    public boolean updateEmployee(Employee employee) {
        Scanner scanner = new Scanner(System.in);
        String findSql = "SELECT employee_id, employee_name, part FROM employees WHERE employee_name = ?";
        String updateSql = "UPDATE employees SET employee_name = ?, part = ? WHERE employee_id = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement findPs = conn.prepareStatement(findSql)) {

            // 동명이인 검색
            findPs.setString(1, employee.getEmployeeName());
            ResultSet rs = findPs.executeQuery();

            List<Employee> duplicateEmployees = new ArrayList<>();
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setEmployeeId(rs.getInt("employee_id"));
                emp.setEmployeeName(rs.getString("employee_name"));
                emp.setPart(rs.getString("part"));
                duplicateEmployees.add(emp);
            }

            // 검색 결과 처리
            if (duplicateEmployees.isEmpty()) {
                System.out.println("해당 이름을 가진 직원이 없습니다.");
                return false;
            } else if (duplicateEmployees.size() == 1) {
                System.out.println("유일한 직원이 발견되었습니다.");
                employee.setEmployeeId(duplicateEmployees.get(0).getEmployeeId());
            } else {
                // 동명이인 목록 출력
                System.out.println("동명이인이 발견되었습니다. 아래 목록에서 employeeId를 선택하세요:");
                for (Employee emp : duplicateEmployees) {
                    System.out.println("employeeId: " + emp.getEmployeeId() + ", 이름: " + emp.getEmployeeName() + ", 부서: " + emp.getPart());
                }

                System.out.print("수정할 employeeId를 입력하세요: ");
                while (true) {
                    int selectedId = scanner.nextInt();

                    // 입력된 ID 검증
                    boolean validId = duplicateEmployees.stream().anyMatch(emp -> emp.getEmployeeId() == selectedId);
                    if (validId) {
                        employee.setEmployeeId(selectedId);
                        break;
                    } else {
                        System.out.println("잘못된 employeeId입니다. 다시 입력하세요:");
                    }
                }
            }

            // 직원 정보 업데이트
            try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                updatePs.setString(1, employee.getEmployeeName());
                updatePs.setString(2, employee.getPart());
                updatePs.setInt(3, employee.getEmployeeId());
                return updatePs.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.err.println("SQL 실행 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("예기치 못한 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 직원 비활성화
    public boolean deactivateEmployee(int id) {
        String sql = "UPDATE employees SET employee_active = 'N' WHERE employee_id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Employee> findAll() {
        List<Employee> employeeList = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE employee_active = 'Y'";
        try (Connection conn = DBConnectionManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                employeeList.add(new Employee(
                        rs.getInt("employee_id"),
                        rs.getString("employee_name"),
                        rs.getString("part")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    public void updateEmployee2(Employee employee) {
        String sql = "UPDATE employees SET employee_name = ?, part = ? WHERE employee_id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getEmployeeName());
            pstmt.setString(2, employee.getPart());
            pstmt.setInt(3, employee.getEmployeeId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}