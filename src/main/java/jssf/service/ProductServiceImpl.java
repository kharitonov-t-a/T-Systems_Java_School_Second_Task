package jssf.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jssf.dto.ProductDTO;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ProductServiceImpl implements ProductService, Serializable {

    private List<ProductDTO> productDTOList;
    private static ObjectMapper mapper = new ObjectMapper();
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private String httpGetRest(String url) throws IOException {
        URL urlRequest = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlRequest.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
//        String base64ClientCredentials = new String(Base64.getEncoder().encodeToString("a:p".getBytes()));
//        connection.setRequestProperty("Authorization", "Basic " + base64ClientCredentials);
        int code = connection.getResponseCode();
        if(code != 200 ){
            System.out.println(code);
            throw new IOException("Result code is not OK");
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = bufferedReader.readLine();
        System.out.println("Response = " + response);
        connection.disconnect();
        return response;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    private List<ProductDTO> fetchProducts(){
        try {
            String response = httpGetRest("http://localhost:8080/product/rest/getproducts");
            return mapper.readValue(response, new TypeReference<List<ProductDTO>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ProductDTO> getProducts() {
        if(productDTOList == null){
            productDTOList = fetchProducts();
        }
        return productDTOList;
    }

    public void updateProducts() {
        List<ProductDTO> productDTOList = fetchProducts();
        pcs.firePropertyChange("productDTOList", this.productDTOList, productDTOList);
        this.productDTOList = productDTOList;
    }

}
