package gym.access.service;

import common.AppUI;
import gym.access.domain.Access;
import gym.access.repo.AccessRepository;
import gym.access.view.AccessView;
import gym.user.domain.Status;
import gym.user.domain.User;
import gym.user.repo.UserRepository;
import gym.user.view.UserView;


import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

public class AccessService {
    AccessRepository accessRepository = new AccessRepository();
    UserRepository userRepository = new UserRepository();

    public void accessUserService() {
        String phoneBackNum = AccessView.accessUserView();
        if(phoneBackNum.isEmpty()) return;

        List<User> userList = accessRepository.searchUserByPhoneNumber(phoneBackNum);
        User user = null;

        if (userList.size() == 1) {
            user = userList.get(0);
        } else if (!userList.isEmpty()) {
            user = AccessView.findUserForUserList(userList);
        } else {
            AccessView.cannotFindUser();
        }

        if(user != null) {
            Status status = accessRepository.checkUserStatus(user);
            if(status == null){
                System.out.print("# 회원권/상품 이 확인되지 않습니다. 결재후 이용해주세요.");
                AppUI.userOrderMenuScreen();
            }
            int period = status.getRemainedMonth() - Period.between(status.getStartDate(), LocalDate.now()).getDays();
            accessRepository.updateUserMembershipCountStatus(user, period);
            status.setRemainedMonth(period);
            if(status.getRemainedMonth() > 0) {
                // 출입 승인
                if (status.getProductCount() > 0) {
                    if(AccessView.selectAccessMode()) { // 상품 선택했다면 상품 카운트 - 1
                       accessRepository.updateUserProductCountStatus(user);
                       status.setProductCount(status.getProductCount() - 1);
                    }
                }

                accessRepository.addAccessData(user);
                AccessView.accessSuccessful(user, status);
            } else {
                if(AccessView.requestMembershipExtend()) {
                    // TODO 주문하는 곳으로 이동
                    AppUI.userOrderMenuScreen();
                } else {
                    userRepository.updateUserActive(user, false);
                    UserView.accessUserFail();
                }
            }
        }
    }

    public void searchAccessInfoByMonth() {
        String[] dateInfo = AccessView.searchAccessView().split("-");
        Map<Access, User> userMap = accessRepository.searchAccessByDate(dateInfo[0], dateInfo[1]);
        AccessView.showAccessByDate(userMap);
    }

}
