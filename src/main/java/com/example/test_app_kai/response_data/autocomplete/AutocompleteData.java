package com.example.test_app_kai.response_data.autocomplete;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AutocompleteData {

    private List<Group> groups;
}
