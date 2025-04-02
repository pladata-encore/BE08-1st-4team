package gym.user.service;

import gym.user.domain.User;
import gym.user.repo.UserRepository;
import gym.user.view.UserView;

import java.util.List;

import static common.AppUI.*;

public class UserService {
    private final UserRepository userRepository = new UserRepository();


    public User join(String name, String phone) {
        User newUser = new User(name, phone);
        userRepository.addUser(newUser);
        return newUser;
    }



    public void updateUserInfo(String name) {
        List<User> userList = userRepository.findByUserName(name, true);
        if (userList.size() == 1) { // 동명이인 없음
            User user = userList.get(0);
            UserView.updateUserInfoByUser(user);
        } else if (!userList.isEmpty()) { // 동명이인 존재
            System.out.println("# 일치하는 사용자 번호를 선택해주세요");
            for (int i = 0; i < userList.size(); i++) {
                User user = userList.get(i);
                System.out.printf("# %d. %s(%s) %s\n", user.getUserId(), user.getUserName(), user.getPhoneNumber(), user.getRegistDate().toString());
            }
            int selectNum = inputInteger(">>> ");

            for (int i = 0; i < userList.size(); i++) {
                if(selectNum == userList.get(i).getUserId()) {
                    UserView.updateUserInfoByUser(userList.get(i));
                    break;
                }
            }
        } else { // 검색된 이름이 없음
            System.out.println("# 검색된 이름이 없습니다.");
        }
    }

    public void updateUserInfoByUser(User user, int selectNum) {
        switch (selectNum) {
            case 1:
                System.out.println("# 변경하고자 하는 이름을 작성해주세요");
                String newName = inputString(">>> ");
                user.setUserName(newName);
                break;
            case 2:
                System.out.println("# 변경하고자 하는 번호를 작성해주세요");
                String newPhone = inputString(">>> ");
                user.setPhoneNumber(newPhone);
                break;
            case 3:
                makeLine();
                return;
            default:
                System.out.println("# 잘못된 입력값입니다. 이전으로 돌아갑니다.");
                return;
        }

        userRepository.updateUserInfo(user);
    }

    public void showAllUsers() {
        List<User> userList = userRepository.findAllUser();
        UserView.showAllUsersView(userList);
    }

    /**
     * 회원 이름 조회
     */
    public List<User> showUserByName(String userName, boolean flag) {
        return userRepository.findByUserName(userName, flag);
    }

}
