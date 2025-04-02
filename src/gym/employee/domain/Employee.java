package gym.employee.domain;

public class Employee {
    private int employeeId;
    private String employeeName;
    private String Part;
    private boolean employeeActive;

    public Employee() {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.Part = Part;
        this.employeeActive = employeeActive;
    }

    public Employee(int employeeId, String employeeName, String part) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.Part = part;
        this.employeeActive = true;
    }

    public Employee(int employeeId, String employeeName, String part, boolean employeeActive) {
      if (employeeName == null || employeeName.isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be null or empty.");
        }
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.Part = part;
        this.employeeActive = employeeActive;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPart() {
        return Part;
    }


    public void setPart(String part) {
        this.Part = part;
    }



    public boolean getEmployeeActive() {
        return employeeActive;

    }

    public void setEmployeeActive(boolean employeeActive) {
        this.employeeActive = employeeActive;
    }


    @Override
    public String toString() {
        return "### 직원이름: " + employeeName + ", 담당직무: " + Part;
    }
}





