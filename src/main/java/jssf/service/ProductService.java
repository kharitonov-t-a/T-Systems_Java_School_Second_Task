package jssf.service;

import jssf.dto.ProductDTO;
import java.beans.PropertyChangeListener;
import java.util.List;

public interface ProductService {
    List<ProductDTO> getProducts();
    void updateProducts();
    void addPropertyChangeListener(PropertyChangeListener listener);
}
