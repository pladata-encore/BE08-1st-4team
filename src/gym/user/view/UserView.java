package gym.user.view;

import common.AppUI;
import gym.user.domain.User;
import gym.user.service.UserService;

import java.util.List;

import static common.AppUI.*;

public class UserView {

    public static void addUserView() {
        UserService userService = new UserService();
        System.out.println("\n====== 회원 등록을 진행합니다. ======");
        String name = inputString("# 회원명: ");
        String phone = inputString("# 전화번호: ");
        boolean active = true;
        User newUser = userService.join(name, phone);
        System.out.printf("\n### [%s]님의 회원 가입이 완료되었습니다.\n", newUser.getUserName());
        AppUI.userOrderMenuScreen();
    }

    // 회원 수정 - 기능 없음
    public static void updateUserInfo() {
        UserService userService = new UserService();

        System.out.println("# 유저 이름을 입력해주세요");
        String name = inputString(">>> ");

        userService.updateUserInfo(name);
    }

    public static void updateUserInfoByUser(User user) {
        System.out.println("# 변경하고자 하는 정보를 선택해주세요.");
        System.out.println("# 1. 회원 이름");
        System.out.println("# 2. 핸드폰 번호");
        System.out.println("# 3. 이전으로 돌아가기");
        makeLine();
        int selectNum = inputInteger(">>> ");

        UserService userService = new UserService();
        userService.updateUserInfoByUser(user, selectNum);

        System.out.println("# 성공적으로 저장되었습니다.");
    }

    public static void accessUserFail() {
        System.out.println("# 출입에 실패하였습니다.");
    }

    public static void showAllUsersView(List<User> userList) {
        System.out.println("\n====== 회원 전체 조회 ======");
        for (User user : userList) {
            System.out.printf("# %d. %s(%s) 등록 일자 : %s, 활성화 여부: %s\n",
                    user.getUserId(), user.getUserName(), user.getPhoneNumber(), user.getRegistDate().toString(), user.isUserActive() ? "Y" : "N");
        }
        System.out.println();
    }
}
