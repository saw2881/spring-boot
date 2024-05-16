package com.subsaw.ilearn.actuator;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Endpoint(id = "custom")
public class CustomEndpoint {

    private static Bar bar;

    public CustomEndpoint() {
        super();
        bar = new Bar("ONE", "TWO");
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class Bar {
        private String barOne;
        private String barTwo;
    }

    // GET
    @ReadOperation
    public Bar getBar() {
        return bar;
    }
    
    @WriteOperation
    public void postBar(String barOne, String barTwo) {
        CustomEndpoint.bar = new Bar(barOne, barTwo);
    }

    @DeleteOperation
    public void deleteBar() {
        CustomEndpoint.bar = null;
    }
}
