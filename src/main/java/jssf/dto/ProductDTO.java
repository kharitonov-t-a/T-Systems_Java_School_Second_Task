package jssf.dto;

import java.io.Serializable;
import java.math.BigDecimal;


public class ProductDTO implements Serializable {

    private Integer id;
    private String name;
    private String characterCode;
    private BigDecimal price;
    private Integer stockQuantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacterCode() {
        return characterCode;
    }

    public void setCharacterCode(String characterCode) {
        this.characterCode = characterCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }


    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", characterCode='" + characterCode + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}