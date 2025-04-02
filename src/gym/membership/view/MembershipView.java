package gym.membership.view;

import static common.AppUI.inputInteger;

import gym.membership.domain.Membership;
import gym.membership.service.MembershipService;
import java.text.DecimalFormat;
import java.util.List;

public class MembershipView {

    /**
     * 회원권 조회
     */
    public static List<Membership> findMembershipView() {
        List<Membership> membershipOptions = MembershipService.getMembershipOptions();

        for (int i = 1; i <= membershipOptions.size(); i++) {
            Membership membership = membershipOptions.get(i - 1);

            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedPrice = formatter.format(membership.getPrice());

            System.out.println("### " + i + ". "
                    + membership.getPeriod() + "개월 - " + formattedPrice + "원");
        }

        return membershipOptions;
    }

    /**
     * 회원권 종류 추가
     */
    public static void addMembershipView() {
        MembershipService membershipService = new MembershipService();
        System.out.println("\n====== 회원권 종류 추가. ======");
        int period = inputInteger("# 회원권 개월: ");
        int price = inputInteger("# 회원권 가격: ");

        Membership membership = membershipService.createMembership(period, price);
        System.out.printf("\n### [%d]개월 회원권이 등록되었습니다.\n", membership.getPeriod());
    }

}
