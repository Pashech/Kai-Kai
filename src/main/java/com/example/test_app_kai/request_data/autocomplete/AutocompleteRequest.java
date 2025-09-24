package com.example.test_app_kai.request_data.autocomplete;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AutocompleteRequest {

    private String clientInterface;
    private ProviderData data;
    private List<String> fields;
}
