package gym.user.service;

import gym.user.domain.Status;
import gym.user.repo.StatusRepository;

public class StatusService {

    private final StatusRepository statusRepository = new StatusRepository();

    /**
     * 회원권 남은 개월수
     */
    public Status findByStatusRemainedMonth(int userId) {
        return statusRepository.findByRemainedMonth(userId);
    }

    /**
     * 상품 남은 횟수
     */
    public void findByStatus() {

    }

    /**
     * 회원상태 추가
     */
    public void saveStatus(boolean boolMonth, int userId, int remainedMonth, int productCount) {
        statusRepository.saveStatus(boolMonth, userId, remainedMonth, productCount);
    }



}
