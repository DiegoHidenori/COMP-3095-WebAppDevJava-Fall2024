package ca.gbc.productservice.dto;

import java.math.BigDecimal;

// Records introduced in java 14, represent immutable classes (values can never change), automatically provide getters
public record ProductRequest(String id, String name, String description, BigDecimal price) {

}
