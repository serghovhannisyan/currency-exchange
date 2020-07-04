package com.serg.currencyexchange.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Currency {

    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private boolean enabled;
}
