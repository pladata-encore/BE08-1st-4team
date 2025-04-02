package gym.membership.domain;

public class Membership {
    private int membershipId;
    private int period;
    private int price;

    public Membership(int period, int price) {
        this.period = period;
        this.price = price;
    }


    public Membership(int price, int period, int membershipId) {
        this.price = price;
        this.period = period;
        this.membershipId = membershipId;
    }

    public int getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}