package com.viet.bookmanagement.config;

import com.viet.bookmanagement.response.restcountries.CountryInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


//@FeignClient(name="country-service", url="http://localhost:8085")
@FeignClient(name="country-service")
public interface CountryInfoByCurrencyProxy {
	
	@GetMapping("/cs/country/{currency}")
	public List<CountryInfo> retrieveCountryInfoByCurrency(
			@PathVariable String currency);

}
