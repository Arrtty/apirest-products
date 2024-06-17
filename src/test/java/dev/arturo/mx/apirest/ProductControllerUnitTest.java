package dev.arturo.mx.apirest;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.arturo.mx.apirest.controller.ProductController;
import dev.arturo.mx.apirest.dto.ProductDTO;
import dev.arturo.mx.apirest.service.ProductService;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ SpringExtension.class, RestDocumentationExtension.class })
@WebMvcTest(ProductController.class)
public class ProductControllerUnitTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ProductService productService;

        @Autowired
        private ObjectMapper objectMapper;

        private RestDocumentationResultHandler documentationHandler;

        @BeforeEach
        public void setUp(WebApplicationContext webApplicationContext,
                        RestDocumentationContextProvider restDocumentation) {
                this.documentationHandler = document("{method-name}", preprocessRequest(), preprocessResponse());
                this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                .apply(documentationConfiguration(restDocumentation))
                                .alwaysDo(this.documentationHandler)
                                .build();
        }

        @Test
        public void testGetProducts() throws Exception {
                ProductDTO product1 = new ProductDTO();
                product1.setId(1);
                product1.setTitle("Product 1");
                product1.setImageUrl("null");
                product1.setDescription("A product");

                ProductDTO product2 = new ProductDTO();
                product2.setId(2);
                product2.setTitle("Product 2");
                product2.setImageUrl("null");
                product2.setDescription("Another product");

                List<ProductDTO> products = Arrays.asList(product1, product2);

                BDDMockito.given(productService.getAllProducts()).willReturn(products);

                mockMvc.perform(get("/products")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].title", is("Product 1")))
                                .andExpect(jsonPath("$[0].imageUrl", is("null")))
                                .andExpect(jsonPath("$[0].description", is("A product")))

                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].title", is("Product 2")))
                                .andExpect(jsonPath("$[1].imageUrl", is("null")))
                                .andExpect(jsonPath("$[1].description", is("Another product")))

                                .andDo(document("get-products",
                                                responseFields(
                                                                fieldWithPath("[].id"),
                                                                fieldWithPath("[].title"),
                                                                fieldWithPath("[].imageUrl"),
                                                                fieldWithPath("[].description"))));
        }

        @Test
        public void testSaveProduct() throws Exception {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(3);
                productDTO.setTitle("Product 3");

                mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productDTO)))
                                .andExpect(status().isCreated())
                                .andDo(document("save-product",
                                                requestFields(
                                                                fieldWithPath("id").description("The product ID"),
                                                                fieldWithPath("title"),
                                                                fieldWithPath("imageUrl"))));

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
                                .andExpect(jsonPath("$.title", is("Updated Product")))

                                .andDo(document("update-product",
                                                pathParameters(
                                                                parameterWithName("id")
                                                                                .description("The product ID")),
                                                requestFields(
                                                                fieldWithPath("id").description("The product ID"),
                                                                fieldWithPath("title").description("The product name")),
                                                responseFields(
                                                                fieldWithPath("id").description("The product ID"),
                                                                fieldWithPath("title")
                                                                                .description("The product name"))));
        }

        @Test
        public void testDeleteProduct() throws Exception {
                int productId = 1;

                mockMvc.perform(delete("/" + productId))
                                .andExpect(status().isNoContent())
                                .andDo(document("delete-product",
                                                pathParameters(
                                                                parameterWithName("id")
                                                                                .description("The product ID"))));
        }
}
