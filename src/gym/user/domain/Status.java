package gym.user.domain;

import java.time.LocalDate;

public class Status {
    private int userId;
    private LocalDate startDate;
    private int remainedMonth;
    private int productCount;

    public Status(int userId, LocalDate startDate, int remainedMonth, int productCount) {
        this.userId = userId;
        this.startDate = startDate;
        this.remainedMonth = remainedMonth;
        this.productCount = productCount;
    }

    public Status(int userId, LocalDate startDate, int remainedMonth) {
        this.userId = userId;
        this.startDate = startDate;
        this.remainedMonth = remainedMonth;
        this.productCount = 0;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getRemainedMonth() {
        return remainedMonth;
    }

    public void setRemainedMonth(int remainedMonth) {
        this.remainedMonth = remainedMonth;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
