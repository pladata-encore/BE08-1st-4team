package gym.membership.service;

import gym.membership.domain.Membership;
import gym.membership.repo.MembershipRepository;
import java.util.List;

public class MembershipService {

    private static final MembershipRepository membershipRepository = new MembershipRepository();

    // 회원권 조회
    public static List<Membership> getMembershipOptions() {
        return membershipRepository.findAll();
    }

    // 회원권 종류 추가
    public Membership createMembership(int price, int period) {
        Membership membership = new Membership(price, period);
        membershipRepository.addMembership(membership);

        return membership;
    }
}
