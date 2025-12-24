package com.vibrantcovers.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.model.Price;
import com.stripe.model.checkout.Session;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.vibrantcovers.entity.Configuration;
import com.vibrantcovers.entity.CaseFinish;
import com.vibrantcovers.entity.CaseMaterial;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    @Value("${stripe.secret-key}")
    private String stripeSecretKey;
    
    @Value("${app.server-url}")
    private String serverUrl;
    
    private static final int BASE_PRICE = 1200; // $12.00 in cents
    private static final int POLYCARBONATE_PRICE = 600; // $6.00 in cents
    private static final int TEXTURED_FINISH_PRICE = 400; // $4.00 in cents
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }
    
    public int calculatePrice(Configuration configuration) {
        int price = BASE_PRICE;
        
        if (configuration.getFinish() == CaseFinish.TEXTURED) {
            price += TEXTURED_FINISH_PRICE;
        }
        
        if (configuration.getMaterial() == CaseMaterial.POLYCARBONATE) {
            price += POLYCARBONATE_PRICE;
        }
        
        return price;
    }
    
    public String createCheckoutSession(Configuration configuration, String userId, String orderId) throws StripeException {
        int price = calculatePrice(configuration);
        
        // Create product
        ProductCreateParams productParams = ProductCreateParams.builder()
                .setName("Custom iPhone Case")
                .addImage(configuration.getImageUrl())
                .build();
        Product product = Product.create(productParams);
        
        // Create price for the product
        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setProduct(product.getId())
                .setCurrency("usd")
                .setUnitAmount((long) price)
                .build();
        Price priceObj = Price.create(priceParams);
        
        // Create checkout session
        SessionCreateParams params = SessionCreateParams.builder()
                .setSuccessUrl(serverUrl + "/thank-you?orderId=" + orderId)
                .setCancelUrl(serverUrl + "/configure/preview?id=" + configuration.getId())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setShippingAddressCollection(
                        SessionCreateParams.ShippingAddressCollection.builder()
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.US)
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.VN)
                                .build()
                )
                .putMetadata("userId", userId)
                .putMetadata("orderId", orderId)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(priceObj.getId())
                                .setQuantity(1L)
                                .build()
                )
                .build();
        
        Session session = Session.create(params);
        return session.getUrl();
    }
}

