package dev.arturo.mx.apirest;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.arturo.mx.apirest.controller.ProductController;
import dev.arturo.mx.apirest.dto.ProductDTO;
import dev.arturo.mx.apirest.service.ProductService;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetProducts() throws Exception {
        ProductDTO product1 = new ProductDTO();
        product1.setId(1);
        product1.setTitle("Product 1");

        ProductDTO product2 = new ProductDTO();
        product2.setId(2);
        product2.setTitle("Product 2");

        List<ProductDTO> products = Arrays.asList(product1, product2);

        BDDMockito.given(productService.getAllProducts()).willReturn(products);

        mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Product 1")))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Product 2")));

    }

    @Test
    public void testSaveProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(3);
        productDTO.setTitle("Product 3");

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        int productId = 1;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productDTO.setTitle("Updated Product");

        ProductDTO updatedProductDTO = new ProductDTO();
        updatedProductDTO.setId(productId);
        updatedProductDTO.setTitle("Updated Product");

        BDDMockito.given(productService.updateProduct(Mockito.eq(productId), any(ProductDTO.class)))
                .willReturn(updatedProductDTO);

        mockMvc.perform(put("/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId)))
                .andExpect(jsonPath("$.name", is("Updated Product")));

    }

    @Test
    public void testDeleteProduct() throws Exception {
        int productId = 1;

        mockMvc.perform(delete("/" + productId))
                .andExpect(status().isNoContent());
    }
}
