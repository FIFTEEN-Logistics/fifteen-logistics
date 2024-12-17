package com.fifteen.eureka.vpo.domain.service;

import com.fifteen.eureka.vpo.domain.model.Product;
import org.springframework.stereotype.Service;

@Service
public class OrderProductService {

    public void updateProduct(Product product, int quantity, boolean orderIsCanceled) {
        product.updateQuantity(quantity, orderIsCanceled);
    }
}
