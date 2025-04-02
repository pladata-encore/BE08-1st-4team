package gym.access.domain;

import java.time.LocalDate;

public class Access {
    private int accessId;
    private int userId;
    private LocalDate accessDate;

    public Access(int accessId, int userId, LocalDate accessDate) {
        this.accessId = accessId;
        this.userId = userId;

        this.accessDate = accessDate;
    }

    public Access(int accessId, int userId) {
        this.accessId = accessId;
        this.userId = userId;
        this.accessDate = LocalDate.now();
    }


    public int getAccessId() {
        return accessId;
    }

    public void setAccessId(int accessId) {
        this.accessId = accessId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(LocalDate accessDate) {
        this.accessDate = accessDate;
    }
}
