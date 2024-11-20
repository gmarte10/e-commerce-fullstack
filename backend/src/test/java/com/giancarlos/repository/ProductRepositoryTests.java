package com.giancarlos.repository;

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
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testProductRepositoryNotNull() {
        Assertions.assertThat(productRepository).isNotNull();
    }

    @Test
    public void ProductRepository_SaveAll_ReturnSavedProduct() {
        // Arrange
        Product product = Product.builder()
                .name("Ghost")
                .price(BigDecimal.valueOf(1000))
                .category("pet")
                .description("An albino direwolf that can aid you in battle")
                .imageURL("ghost.jpg")
                .build();
        // Act
        Product savedProduct = productRepository.save(product);
        // Assert
        Assertions.assertThat(savedProduct).isNotNull();
        Assertions.assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void ProductRepository_GetAll_ReturnGreaterThanOneProduct() {
        Product product = Product.builder()
                .name("Ghost")
                .price(BigDecimal.valueOf(100))
                .category("pet")
                .description("An albino direwolf that can aid you in battle")
                .imageURL("ghost.jpg")
                .build();

        Product product2= Product.builder()
                .name("ice")
                .price(BigDecimal.valueOf(5000))
                .category("weapon")
                .description(" Ice is a Valyrian steel greatsword, a possession of House Stark. This ancestral weapon is a two-handed, wide sword")
                .imageURL("ice.jpg")
                .build();

        productRepository.save(product);
        productRepository.save(product2);

        List<Product> productList = productRepository.findAll();

        Assertions.assertThat(productList).isNotNull();
        Assertions.assertThat(productList.size()).isEqualTo(2);
    }

    @Test
    public void ProductRepository_FindById_ReturnSavedProduct() {
        // Arrange
        Product product = Product.builder()
                .name("Ghost")
                .price(BigDecimal.valueOf(100))
                .category("pet")
                .description("An albino direwolf that can aid you in battle")
                .imageURL("ghost.jpg")
                .build();
        // Act
        productRepository.save(product);
        Product foundProduct = productRepository.findById(product.getId()).get();
        // Assert
        Assertions.assertThat(foundProduct).isNotNull();
        Assertions.assertThat(foundProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void ProductRepository_UpdateProduct_ReturnProduct() {
        // Arrange
        Product product = Product.builder()
                .name("ice")
                .price(BigDecimal.valueOf(5000))
                .category("weapon")
                .description(" Ice is a Valyrian steel greatsword, a possession of House Stark. This ancestral weapon is a two-handed, wide sword")
                .imageURL("ice.jpg")
                .build();
        // Act
        productRepository.save(product);
        Product foundProduct = productRepository.findById(product.getId()).get();
        foundProduct.setName("OathKeeper");
        foundProduct.setDescription("Oathkeeper is a Valyrian steel blade that was made from the reforged Ice, alongside Widow's Wail");
        foundProduct.setPrice(BigDecimal.valueOf(1000));

        // Assert
        Assertions.assertThat(foundProduct.getName()).isNotNull();
        Assertions.assertThat(foundProduct.getPrice()).isNotNull();
        Assertions.assertThat(foundProduct.getDescription()).isNotNull();
    }

    @Test
    public void ProductRepository_ProductDeleteById_ReturnProductIsEmpty() {
        Product product = Product.builder()
                .name("Ghost")
                .price(BigDecimal.valueOf(100))
                .category("pet")
                .description("An albino direwolf that can aid you in battle")
                .imageURL("ghost.jpg")
                .build();
        productRepository.save(product);

        productRepository.deleteById(product.getId());

        Optional<Product> productReturn = productRepository.findById(product.getId());

        Assertions.assertThat(productReturn).isEmpty();
    }
}
