package gym.product.service;

import gym.product.domain.Product;
import gym.product.repo.ProductRepository;

import java.util.List;


public class ProductService {

    private static final ProductRepository productRepository = new ProductRepository();

    // 상품 조회
    public List<Product> getProductOptions() {
        return productRepository.findAll();
    }

    /**
     * 상품 추가
     */
    public Product createProduct(String productName, int price) {
        Product product = new Product(productName, price);
        productRepository.addProduct(product);

        return product;
    }

}
