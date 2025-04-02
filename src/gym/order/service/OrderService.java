package gym.order.service;

import gym.order.domain.Order;
import gym.order.repo.OrderRepository;
import gym.order.view.OrderView;
import gym.product.domain.Product;
import gym.product.service.ProductService;

import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository = new OrderRepository();

    public void purchaseMembership(int userId, int membershipId, int employeeId) {
        boolean result = orderRepository.insertOrder(userId, membershipId, employeeId);

        if (result) {
            System.out.println("# 회원권이 성공적으로 구매되었습니다.");
        } else {
            System.out.println("# 회원권 구매에 실패했습니다. 다시 시도해주세요.");
        }
    }

    public void purchaseProduct(int userId, int membershipId, int employeeId, int productId) {
        boolean result = orderRepository.insertOrder(userId, membershipId, employeeId, productId);

        if (result) {
            System.out.println("# 상품이 성공적으로 구매되었습니다.");
        } else {
            System.out.println("# 상품 구매에 실패했습니다. 다시 시도해주세요.");
        }
    }

    public void showAllOrderInfo() {
        List<Order> orderList = orderRepository.getOrderList();
        OrderView.showOrderInfo(orderList);
    }

}
