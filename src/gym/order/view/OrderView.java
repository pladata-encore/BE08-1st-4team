package gym.order.view;

import gym.membership.domain.Membership;
import gym.order.service.OrderService;
import gym.product.domain.Product;
import gym.employee.service.EmployeeService;
import gym.membership.view.MembershipView;
import gym.product.view.ProductView;
import gym.user.domain.User;
import gym.user.repo.UserRepository;
import gym.user.service.UserService;
import gym.order.domain.Order;
import gym.user.domain.Status;
import gym.user.service.StatusService;

import java.util.List;
import java.util.Scanner;

public class OrderView {
    private static final OrderService orderService = new OrderService();
    private static final UserService userService = new UserService();
    private static final EmployeeService employeeService = new EmployeeService();
    private static final StatusService statusService = new StatusService();
    private static final Scanner sc = new Scanner(System.in);

    private static final UserRepository userRepository = new UserRepository();

    /**
     * 회원권 결제 화면
     * 1. 회원 이름을 입력받아 회원 정보 확인.
     * 2. 회원권을 선택하여 결제.
     * 3. 선택된 회원권에 따라 회원 상태를 업데이트.
     */
    public static void purchaseMembershipView() {
        System.out.println("\n### 회원권을 등록할 회원을 입력해주세요.");

        String userName = getUserName(); // 회원 이름 입력 받기
        if (userName == null) return; // 입력값이 없으면 종료

        int userId = getUserId(userName); // 회원 ID 확인
        if (userId == -1) return; // 회원 ID가 없으면 종료

        int employeeId = getRandomEmployeeId(); // 랜덤 직원 배정
        if (employeeId == -1) {
            System.out.println("# 직원 정보가 없습니다.");
            return;
        }

        List<Membership> memberships = MembershipView.findMembershipView(); // 등록된 회원권 목록 가져오기
        if (memberships.isEmpty()) {
            System.out.println("# 등록된 회원권이 없습니다.");
            return;
        }

        int selectedMembershipNum = getUserSelection(memberships.size(), "구매할 회원권 번호를 입력하세요: ");
        if (selectedMembershipNum == -1) return; // 잘못된 선택 시 종료

        orderService.purchaseMembership(userId, memberships.get(selectedMembershipNum - 1).getMembershipId(), employeeId); // 회원권 결제 처리

        updateUserStatus(userId, memberships.get(selectedMembershipNum - 1)); // 회원 상태 업데이트

    }

    /**
     * 상품 결제 화면
     * 1. 상품 목록을 출력하고, 구매할 상품을 선택.
     * 2. 추가로 회원권을 선택하여 결제.
     * 3. 선택된 상품과 회원권을 결제하고 회원 상태를 업데이트.
     */
    public static void purchaseProductView() {
        System.out.println("\n### 상품을 등록할 회원을 입력해주세요.");

        String userName = getUserName(); // 회원 이름 입력 받기
        if (userName == null) return; // 입력값이 없으면 종료

        int userId = getUserId(userName); // 회원 ID 확인
        if (userId == -1) return; // 회원 ID가 없으면 종료

        int employeeId = getRandomEmployeeId(); // 랜덤 직원 배정
        if (employeeId == -1) {
            System.out.println("# 직원 정보가 없습니다.");
            return;
        }

        List<Product> products = ProductView.findProductView(); // 상품 목록 가져오기
        if (products.isEmpty()) {
            System.out.println("등록된 상품이 없습니다.");
            return;
        }

        int selectedProductNum = getUserSelection(products.size(), "구매할 상품 번호를 입력하세요: ");
        if (selectedProductNum == -1) return; // 잘못된 선택 시 종료

        // 회원권 목록 출력 및 선택
        List<Membership> memberships = MembershipView.findMembershipView();
        if (memberships.isEmpty()) {
            System.out.println("등록된 회원권이 없습니다.");
            return;
        }

        int selectedMembershipNum = getUserSelection(memberships.size(), "추가할 회원권 번호를 입력하세요: ");
        if (selectedMembershipNum == -1) return; // 잘못된 선택 시 종료

        // 상품 & 회원권 등록
        orderService.purchaseProduct(userId, memberships.get(selectedMembershipNum - 1).getMembershipId(), employeeId, products.get(selectedProductNum - 1).getProductId());

        // 회원 상태 업데이트
        updateUserStatus(userId, memberships.get(selectedMembershipNum - 1), products.get(selectedProductNum - 1));
    }

    /**
     * 회원 이름 입력받기
     * 이름이 비어있을 경우 null을 반환하여 메소드 종료.
     */
    private static String getUserName() {
        System.out.print("# 회원 이름을 입력하세요: ");
        String userName = sc.nextLine();
        if (userName.isEmpty()) {
            System.out.println("# 이름을 입력해주세요.");
            return null;
        }
        return userName;
    }

    /**
     * 회원 ID 확인
     * 이름으로 회원을 검색하고, 결과에 따라 ID를 반환.
     * 만약 동일 이름의 회원이 여러 명 있으면 목록에서 선택하도록 유도.
     */
    private static int getUserId(String userName) {
        List<User> userList = userService.showUserByName(userName, false); // 이름으로 회원 검색
        if (userList.isEmpty()) {
            System.out.println("# 해당 회원이 존재하지 않습니다.");
            return -1; // 회원이 없으면 -1 반환
        }

        if (userList.size() == 1) {
            return userList.get(0).getUserId(); // 회원이 한 명이면 바로 반환
        }

        return selectUserFromList(userList); // 동일 이름 회원이 여러 명일 경우 선택 유도
    }

    /**
     * 동일한 이름의 회원이 여러 명 있을 때 목록에서 선택
     */
    private static int selectUserFromList(List<User> userList) {
        System.out.println("\n# 동일한 이름의 회원이 여러 명 있습니다. 선택해주세요.");
        for (int i = 0; i < userList.size(); i++) {
            System.out.printf("### %d, %s, %s, %s\n", (i + 1), userList.get(i).getUserName(),
                    userList.get(i).getPhoneNumber(), userList.get(i).getRegistDate());
        }

        int selectNum = getUserSelection(userList.size(), "# 회원 번호를 선택하세요: ");
        return userList.get(selectNum - 1).getUserId();
    }

    /**
     * 사용자 선택 번호 입력 및 검증
     */
    private static int getUserSelection(int max, String prompt) {
        System.out.print(prompt);
        String input = sc.nextLine();

        try {
            int selection = Integer.parseInt(input);
            if (selection < 1 || selection > max) {
                System.out.println("# 잘못된 번호입니다. 1부터 " + max + "까지의 번호를 입력해주세요.");
                return -1;
            }
            return selection; // TODO
        } catch (NumberFormatException e) {
            System.out.println("# 숫자만 입력해주세요.");
            return -1;
        }
    }

    /**
     * 랜덤 직원 배정
     * 직원 정보를 받아와 랜덤으로 직원 ID를 반환.
     */
    private static int getRandomEmployeeId() {
        return employeeService.getRandomEmployeeId();
    }

    /**
     * 회원 상태 업데이트
     * 선택된 회원권에 따라 상태를 업데이트.
     */
    private static void updateUserStatus(int userId, Membership selectedMembership) {
        Status status = statusService.findByStatusRemainedMonth(userId); // 현재 상태 가져오기
        boolean remainedMonth = status != null;
        int sMonth = selectedMembership.getPeriod() * 30; // 선택된 회원권의 기간을 월로 변환
        int currentMonth = remainedMonth ? status.getRemainedMonth() : 0; // 남은 월
        int finalMonth = currentMonth + sMonth; // 총 기간 계산
        statusService.saveStatus(remainedMonth, userId, finalMonth, 0); // 상태 저장
    }

    /**
     * 상품 결제 후 회원 상태 업데이트
     * 선택된 상품과 회원권을 결제한 후 상태를 업데이트.
     */
    private static void updateUserStatus(int userId, Membership selectedMembership, Product selectedProduct) {
        Status status = statusService.findByStatusRemainedMonth(userId); // 현재 상태 가져오기
        boolean remainedMonth = status != null;
        int sMonth = selectedMembership.getPeriod() * 30; // 선택된 회원권의 기간
        int currentMonth = remainedMonth ? status.getRemainedMonth() : 0; // 현재 상태에서 남은 월
        int currentCount = remainedMonth ? status.getProductCount() : 0; // 현재 상품 수
        int finalMonth = currentMonth + sMonth; // 총 기간 계산
        int finalCount = currentCount + Integer.parseInt(selectedProduct.getProductName().replaceAll("[^0-9]", "")); // 상품 수 계산
        statusService.saveStatus(remainedMonth, userId, finalMonth, finalCount); // 상태 저장
    }

    /**
     * 결제 정보 출력
     */
    public static void showOrderInfo(List<Order> orderList) {
        System.out.println("========== 결제 목록 ==========");
        for (Order order : orderList) {
            if (order.getProduct() == null) {
                System.out.printf("결제 ID | %d, 회원 정보 | %s(%s), 결제 상품 | %s, 담당 직원 | %s\n",
                        order.getOrderId(), order.getUser().getUserName(), order.getUser().getPhoneNumber(),
                        order.getMembership().getPeriod() + "개월", order.getEmployee().getEmployeeName());
            } else {
                System.out.printf("결제 ID | %d, 회원 정보 | %s(%s), 결제 상품 | %s, %s, 담당 직원 | %s\n",
                        order.getOrderId(), order.getUser().getUserName(), order.getUser().getPhoneNumber(),
                        order.getProduct().getProductName(), order.getMembership().getPeriod() + "개월",
                        order.getEmployee().getEmployeeName());
            }
        }
    }
}
