package com.microservicos.ProductServiceAPI.models;

public class ProductUser {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private UserClient user;

    public ProductUser(Product product, UserClient user) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.user = user;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public Integer getQuantity() { return quantity; }
    public UserClient getUser() { return user; }
}