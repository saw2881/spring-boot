package com.subsaw.ilearn.actuator;

import org.springframework.boot.actuate.info.Info.Builder;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

import org.springframework.boot.actuate.info.InfoContributor;

@Component
public class CustomInfoContributor implements InfoContributor{

    @Override
    public void contribute(Builder builder) {
        builder.withDetails(
            Map.ofEntries(
                Map.entry("info", new Foo("one", "two"))
            )
        ).build();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class Foo {
        private String fooOne;
        private String fooTwo;
    }

}
