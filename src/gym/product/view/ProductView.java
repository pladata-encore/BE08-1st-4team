package gym.product.view;

import static common.AppUI.inputInteger;
import static common.AppUI.inputString;

import gym.membership.domain.Membership;
import gym.membership.service.MembershipService;
import gym.order.service.OrderService;
import gym.product.domain.Product;
import gym.product.service.ProductService;

import java.text.DecimalFormat;
import java.util.List;

public class ProductView {

    private static final OrderService orderService = new OrderService();
    private static ProductService productService = new ProductService();

    /**
     * 회원권 조회
     */
    public static List<Product> findProductView() {
        List<Product> ProductOptions = productService.getProductOptions();

        for (int i = 1; i <= ProductOptions.size(); i++) {
            Product product = ProductOptions.get(i - 1);

            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedPrice = formatter.format(product.getPrice());

            System.out.println("### " + i + ". "
                    + product.getProductName() + " - " + formattedPrice + "원");
        }

        return ProductOptions;
    }

    /**
     * 상품 추가
     */
    public static void addProductView() {
        ProductService productService = new ProductService();
        System.out.println("\n====== 상품 추가. ======");
        String productName = inputString("# 상품 이름: ");
        int price = inputInteger("# 상품 가격: ");

        Product product = productService.createProduct(productName, price);
        System.out.printf("\n### [%s] 상품이 등록되었습니다.\n", product.getProductName());
    }

    public static void showProductView() {
        List<Product> productOptions = productService.getProductOptions(); // 상품 목록 가져오기
        if (productOptions.isEmpty()) {
            System.out.println("등록된 상품이 없습니다.");
            return;
        }

        // 상품 목록을 출력하고 선택
        for (int i = 1; i <= productOptions.size(); i++) {
            Product product = productOptions.get(i - 1);
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedPrice = formatter.format(product.getPrice());
            System.out.println("### " + i + ". "
                    + product.getProductName() + " - " + formattedPrice + "원");
        }
    }
}
