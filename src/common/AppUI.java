package common;

import gym.access.service.AccessService;
import gym.employee.service.EmployeeService;
import gym.membership.domain.Membership;
import gym.membership.view.MembershipView;
import gym.order.service.OrderService;
import gym.order.view.OrderView;

import gym.user.service.UserService;
import gym.product.view.ProductView;
import gym.user.view.UserView;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class AppUI {
    private static final Scanner sc = new Scanner(System.in);

    private static final AccessService accessService = new AccessService();
    private static final UserService userService = new UserService();
    private static final EmployeeService employeeService = new EmployeeService();
    private static final OrderService orderService = new OrderService();


    public static String inputString(String message) {
        System.out.print(message);
        return sc.nextLine();
    }

    public static int inputInteger(String message) {
        System.out.print(message);
        int num = 0;
        try {
            num = sc.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("# 올바른 정수 입력값이 아닙니다!");
        } finally {
            sc.nextLine(); // try에서도(엔터 입력값), catch에서도(쓰레기 문자열 수거) nextLine()이 동작해야 함.
        }
        return num;
    }

    public static void makeLine() {
        System.out.println("===============================================");
    }

    // 화면 출력 예시
    public static void startScreen() {
        System.out.println("\n========= 헬스장 시스템 =========");
        System.out.println("### 1. 회원 메뉴");
        System.out.println("### 2. 관리자 메뉴");
        System.out.println("### 3. 프로그램 종료");
        makeLine();

        int selectNum = inputInteger(">>> ");
        switch (selectNum) {
            case 1:
                userMenuScreen();
                break;
            case 2:
                System.out.println("# 비밀번호를 입력하세요");
                String input = inputString(">>> ");
                if(input.equals("admin")) {
                    managerMenuScreen();
                } else {
                    System.out.println("# 잘못된 비밀번호입니다.");
                }
                break;
            case 3:
                System.exit(0);
                return;
            default:
                wrongNumber();
                break;
        }
    }

    public static void wrongNumber() {
        System.out.println("# 잘못된 입력값입니다. 이전으로 돌아갑니다.");
    }

    public static void managerMenuScreen() {

        while (true) {
            System.out.println("\n========= 관리자 메뉴 =========");
            System.out.println("### 1. 회원 정보");
            System.out.println("### 2. 상품 정보");
            System.out.println("### 3. 회원권 정보");
            System.out.println("### 4. 출입 기록 정보");
            System.out.println("### 5. 직원 정보");
            System.out.println("### 6. 결제 내역 정보");
            System.out.println("### 7. 이전 화면으로 가기");
            makeLine();

            int selectNum = inputInteger(">>> ");
            switch(selectNum) {
                case 1:
                    userService.showAllUsers();
                    break;
                case 2:
                    productMenuScreen();
                    break;
                case 3:
                    membershipMenuScreen();
                    break;
                case 4:
                    accessService.searchAccessInfoByMonth();
                    break;
                case 5:
                    employeeMenuScreen();
                    break;
                case 6:
                    orderService.showAllOrderInfo();
                    break;
                case 7:
                    return;
                default:
                    wrongNumber();
                    break;
            }
        }
    }

    public static void employeeMenuScreen() {
        System.out.println("\n========= 직원 메뉴 =========");
        System.out.println("### 1. 직원 조회");
        System.out.println("### 2. 직원 정보 추가");
        System.out.println("### 3. 직원 정보 수정");
        System.out.println("### 4. 직원 정보 삭제");
        System.out.println("### 5. 이전 화면으로 가기");
        makeLine();

        int selectNum = inputInteger(">>> ");
        switch(selectNum) {
            case 1:
                employeeService.getAllEmployees();
                break;
            case 2:
                employeeService.addEmployee();
                break;
            case 3:
                employeeService.updateEmployee();
                break;
            case 4:
                employeeService.deleteEmployee();
                break;
            case 5:
                return;
            default:
                wrongNumber();
                break;
        }
    }

    public static void productMenuScreen() {
        System.out.println("\n========= 상품 메뉴 =========");
        System.out.println("### 1. 상품 조회");
        System.out.println("### 2. 상품 추가");
        System.out.println("### 3. 이전 화면으로 가기");
        makeLine();

        int selectNum = inputInteger(">>> ");
        switch(selectNum) {
            case 1:
                ProductView.showProductView();
                break;
            case 2:
                ProductView.addProductView();
                break;
            case 3:
                return;
            default:
                wrongNumber();
                break;
        }
    }

    public static void membershipMenuScreen() {
        System.out.println("\n========= 회원권 메뉴 =========");
        System.out.println("### 1. 회원권 조회");
        System.out.println("### 2. 회원권 추가");
        System.out.println("### 3. 이전 화면으로 가기");
        makeLine();

        int selectNum = inputInteger(">>> ");
        switch(selectNum) {
            case 1:
                List<Membership> memberships = MembershipView.findMembershipView();
                if (memberships.isEmpty()) System.out.println("# 등록된 회원권이 없습니다.");
                break;
            case 2:
                MembershipView.addMembershipView();
                break;
            case 3:
                return;
            default:
                wrongNumber();
                break;
        }
    }

    public static void userMenuScreen() {
        while (true) {
            System.out.println("\n========= 회원 메뉴 =========");
            System.out.println("### 1. 회원 등록");
            System.out.println("### 2. 회원권 및 상품 결제");
            System.out.println("### 3. 출입 관리");
            System.out.println("### 4. 이전 화면으로 가기");
            makeLine();

            int selectNum = inputInteger(">>> ");
            switch(selectNum) {
                case 1:
                    UserView.addUserView();
                    break;
                case 2:
                    userOrderMenuScreen();
                    break;
                case 3:
                    accessService.accessUserService();
                    break;
                case 4:
                    return;
                default:
                    wrongNumber();
                    break;
            }
        }
    }

    public static void userOrderMenuScreen() {
        System.out.println("\n========= 결제 메뉴 =========");
        System.out.println("### 1. 회원권 구매");
        System.out.println("### 2. 상품 구매");
        System.out.println("### 3. 이전으로 돌아가기");

        int selectNum = inputInteger(">>> ");
        switch(selectNum) {
            case 1:
                OrderView.purchaseMembershipView();
                break;
            case 2:
                OrderView.purchaseProductView();
                break;
            case 3:
                return;
            default:
                wrongNumber();
                break;
        }
    }
}
