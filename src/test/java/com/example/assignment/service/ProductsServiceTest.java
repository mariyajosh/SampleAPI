package com.example.assignment.service;


import com.example.assignment.model.Product;
import com.example.assignment.model.QueryParams;
import com.example.assignment.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ProductsServiceTest {

    @MockBean
    private ProductRepository productRepository;
    @Autowired
    private ProductsService productsService;

    Product product = new Product(0, "RENT", "RENT", "", 24, 0, 0, 0.0, 0.0, null, "RENT", "SARADA PHARMACY");
    List<Product> productList = new ArrayList<>();

    @Test
    public void ShouldParseGivenDataAndCallSaveAllMethodToSaveData() {

        productList.add(product);
        Mockito.when(productRepository.saveAll(productList)).thenReturn(productList);
        productsService.addProducts(convertCsvToMultipartFile());
        Mockito.verify(productRepository, Mockito.times(1)).saveAll(productList);
    }

    @Test
    public void ShouldCallRepositoryMethodToGetDataWithParams() {
        QueryParams queryParams = QueryParams.WrapParams("Atlassian", "software", true, null, null);
        Page<Product> page = new PageImpl<>(productList);
        Mockito.when(productRepository.getProducts("Atlassian", "software", true, null, null)).thenReturn(page);
        productsService.getProducts(queryParams);
        Mockito.verify(productRepository, Mockito.times(1)).getProducts("Atlassian", "software", true, null, null);
    }

    @Test
    public void ShouldCallRepositoryMethodToGetDataWithEmptyParams() {
        QueryParams queryParams = QueryParams.WrapParams(null, null, null, null, null);
        Page<Product> page = new PageImpl<>(productList);
        Mockito.when(productRepository.getProducts(null, null, null, null, null)).thenReturn(page);
        productsService.getProducts(queryParams);
        Mockito.verify(productRepository, Mockito.times(1)).getProducts(null, null, null, null, null);
    }

    private MultipartFile convertCsvToMultipartFile() {
        try {
            MultipartFile multipartFile = new MockMultipartFile("testData.csv", new FileInputStream(new File("src/test/resources/testData.csv")));
            return multipartFile;
        } catch (IOException ioException) {
            System.out.println(ioException.getLocalizedMessage());
        }
        return null;
    }
}
