package jssf;

import jssf.dto.ProductDTO;
import jssf.jms.ConsumerMessageListener;
import jssf.service.ProductService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class ProductBean implements Serializable {

    @Inject
    private ProductService productService;

    @Inject
    private ConsumerMessageListener consumerMessageListener;

    @Inject
    @Push
    private PushContext productChanel;

    @PostConstruct
    public void init(){
        productService.addPropertyChangeListener((PropertyChangeEvent e)->{
            System.out.println("Old value " + e.getOldValue());
            System.out.println("New value " + e.getNewValue());
            productChanel.send("updateProducts");
            System.out.println("Pushed");
            System.out.println("New products " + productService.getProducts());
        });
        consumerMessageListener.receivedQueue();
    }

    public List<ProductDTO> getProducts(){
        return productService.getProducts();
    }

    public void update(){
        productChanel.send("updateProducts");
//        productService.getProducts();
    }
}

