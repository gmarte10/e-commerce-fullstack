package com.giancarlos.repository;

import com.giancarlos.model.CartItem;
import com.giancarlos.model.Product;
import com.giancarlos.model.User;
import com.giancarlos.model.UserRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    // Test naming convention: [class]_[method]_[return]
    @Test
    public void ProductRepository_Save_ReturnSavedProduct() {
        // Arrange
        Product product = Product.builder()
                .name("hoodie")
                .price(BigDecimal.valueOf(50.99))
                .description("warm cozy hoodie")
                .category("clothing")
                .imageURL("hoodie.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();

        // Act
        Product saved = productRepository.save(product);

        // Assert
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0L);
    }

    @Test
    public void ProductRepository_FindByCategory_ReturnMoreThanOneProduct() {
        // Arrange
        Product product = Product.builder()
                .name("hoodie")
                .price(BigDecimal.valueOf(50.99))
                .description("warm cozy hoodie")
                .category("clothing")
                .imageURL("hoodie.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();

        Product product2 = Product.builder()
                .name("laptop")
                .price(BigDecimal.valueOf(1119.50))
                .description("fast powerful laptop")
                .category("technology")
                .imageURL("laptop.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        productRepository.save(product);
        productRepository.save(product2);

        // Act
        List<Product> productList = productRepository.findByCategory("technology");

        // Assert
        Assertions.assertThat(productList).isNotNull();
        Assertions.assertThat(productList.size()).isEqualTo(1);
        Assertions.assertThat(productList.get(0).getName()).isEqualTo("laptop");
    }

    @Test
    public void ProductRepository_FindAll_ReturnMoreThanOneProduct() {
        // Arrange
        Product product = Product.builder()
                .name("hoodie")
                .price(BigDecimal.valueOf(50.99))
                .description("warm cozy hoodie")
                .category("clothing")
                .imageURL("hoodie.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        Product product2 = Product.builder()
                .name("laptop")
                .price(BigDecimal.valueOf(1119.50))
                .description("fast powerful laptop")
                .category("technology")
                .imageURL("laptop.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        productRepository.save(product);
        productRepository.save(product2);

        // Act
        List<Product> productList = productRepository.findAll();

        // Assert
        Assertions.assertThat(productList).isNotNull();
        Assertions.assertThat(productList.size()).isEqualTo(2);
        Assertions.assertThat(productList.get(0).getName()).isEqualTo("hoodie");
    }

    @Test
    public void ProductRepository_FindById_ReturnProduct() {
        // Arrange
        Product product = Product.builder()
                .name("hoodie")
                .price(BigDecimal.valueOf(50.99))
                .description("warm cozy hoodie")
                .category("clothing")
                .imageURL("hoodie.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        productRepository.save(product);

        // Act
        Product found = productRepository.findById(product.getId()).get();

        // Assert
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(product.getId()).isGreaterThan(0L);
        Assertions.assertThat(found.getName()).isEqualTo("hoodie");
    }

    @Test
    public void ProductRepository_FindByPriceLessThanEqual_ReturnMoreThanOneProduct() {
        // Arrange
        Product product = Product.builder()
                .name("hoodie")
                .price(BigDecimal.valueOf(50.99))
                .description("warm cozy hoodie")
                .category("clothing")
                .imageURL("hoodie.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        Product product2 = Product.builder()
                .name("laptop")
                .price(BigDecimal.valueOf(1119.50))
                .description("fast powerful laptop")
                .category("technology")
                .imageURL("laptop.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        productRepository.save(product);
        productRepository.save(product2);

        // Act
        List<Product> productList = productRepository.findByPriceLessThanEqual(BigDecimal.valueOf(100));

        // Assert
        Assertions.assertThat(productList).isNotNull();
        Assertions.assertThat(productList.size()).isEqualTo(1);
        Assertions.assertThat(productList.get(0).getName()).isEqualTo("hoodie");
    }

    @Test
    public void ProductRepository_SearchByNameAndCategory_ReturnMoreThanOneProduct() {
        // Arrange
        Product product = Product.builder()
                .name("hoodie")
                .price(BigDecimal.valueOf(50.99))
                .description("warm cozy hoodie")
                .category("clothing")
                .imageURL("hoodie.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        Product product2 = Product.builder()
                .name("laptop")
                .price(BigDecimal.valueOf(1119.50))
                .description("fast powerful laptop")
                .category("technology")
                .imageURL("laptop.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        productRepository.save(product);
        productRepository.save(product2);

        // Act
        List<Product> search = productRepository.searchByNameAndCategory("hoodie");
        List<Product> search2 = productRepository.searchByNameAndCategory("tech");
        List<Product> search3 = productRepository.searchByNameAndCategory("l");
        List<Product> search4 = productRepository.searchByNameAndCategory("x");
        List<Product> search5 = productRepository.searchByNameAndCategory("o");

        // Assert
        Assertions.assertThat(search).isNotNull();
        Assertions.assertThat(search2).isNotNull();
        Assertions.assertThat(search3).isNotNull();
        Assertions.assertThat(search4).isNotNull();
        Assertions.assertThat(search5).isNotNull();
        Assertions.assertThat(search.size()).isEqualTo(1);
        Assertions.assertThat(search5.size()).isEqualTo(2);
        Assertions.assertThat(search3.size()).isEqualTo(2);
        Assertions.assertThat(search4.size()).isEqualTo(0);
        Assertions.assertThat(search2.get(0).getName()).isEqualTo("laptop");
    }

    @Test
    public void ProductRepository_DeleteById_ReturnNothing() {
        // Arrange
        Product product = Product.builder()
                .name("hoodie")
                .price(BigDecimal.valueOf(50.99))
                .description("warm cozy hoodie")
                .category("clothing")
                .imageURL("hoodie.jpg")
                .cartItems(null)
                .orderItems(null)
                .build();
        productRepository.save(product);

        // Act
        productRepository.deleteById(product.getId());
        Optional<Product> found = productRepository.findById(product.getId());

        // Assert
        Assertions.assertThat(found).isEmpty();
    }
}
