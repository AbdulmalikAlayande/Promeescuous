package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.AddressCreationRequest;
import africa.semicolon.promeescuous.dtos.requests.AddressUpdateRequest;
import africa.semicolon.promeescuous.dtos.responses.AddressCreationResponse;
import africa.semicolon.promeescuous.dtos.responses.AddressUpdateResponse;
import africa.semicolon.promeescuous.dtos.responses.GetAddressResponse;
import africa.semicolon.promeescuous.models.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AddressServiceTest {
	@Autowired
	private AddressService addressService;
	private AddressCreationResponse addressCreationResponse;
	@BeforeEach void startEachTestWith(){
		addressService.deleteAll();
		AddressCreationRequest addressCreationRequest = buildCreationRequest();
		addressCreationResponse = addressService.saveAddress(addressCreationRequest);
	}
	
	@Test void saveNewAddressTest(){
		assertThat(addressCreationResponse).isNotNull();
		assertThat(addressCreationResponse.getId()).isNotNull();
		assertThat(addressCreationResponse.getCountry()).isEqualTo("Nigeria");
	}
	
	@Test void updateAddressTest(){
		AddressUpdateRequest addressUpdateRequest = buildUpdateRequest();
		AddressUpdateResponse updateResponse = addressService.updateAddress(addressUpdateRequest);
		assertThat(updateResponse.getCountry()).isEqualTo(addressUpdateRequest.country());
		assertThat(updateResponse.getState()).isEqualTo(updateResponse.getState());
	}
	
	@Test void getAllAddressesTest(){
	
	}
	
	@Test void getAddressByCountryAndState(){
		List<GetAddressResponse> foundAddress = addressService.getAddressBy(Location.of("Nigeria", "Lagos"));
		assertThat(foundAddress).isNotNull();
		foundAddress.forEach(address->{
			assertThat(address.getCountry()).isEqualTo("Nigeria");
			assertThat(address.getState()).isEqualTo("Lagos");
			assertThat(address.getId()).isNotNull();
		});
	}
	
	private static AddressCreationRequest buildCreationRequest() {
		return AddressCreationRequest.builder()
				       .country("Nigeria")
				       .houseNumber("34B")
				       .state("Lagos")
				       .street("Semicolon Street")
				       .build();
	}
	
	private AddressUpdateRequest buildUpdateRequest() {
		return AddressUpdateRequest.builder()
				       .country("Ghana")
				       .id(200L)
				       .houseNumber("43G")
				       .build();
	}
}