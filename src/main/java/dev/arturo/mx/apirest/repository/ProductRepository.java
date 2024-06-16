package dev.arturo.mx.apirest.repository;

import dev.arturo.mx.apirest.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, Integer> {

}
