package com.example.test_app_kai.utils;

import com.google.gson.annotations.SerializedName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Component
public class ProxyInfo {
    @SerializedName("country")
    public String country;
    @SerializedName("region")
    public String region;
    @SerializedName("city")
    public String city;

}