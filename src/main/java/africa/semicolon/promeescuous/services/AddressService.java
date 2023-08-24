package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.requests.AddressCreationRequest;
import africa.semicolon.promeescuous.dtos.requests.AddressUpdateRequest;
import africa.semicolon.promeescuous.dtos.responses.AddressCreationResponse;
import africa.semicolon.promeescuous.dtos.responses.AddressUpdateResponse;
import africa.semicolon.promeescuous.dtos.responses.GetAddressResponse;
import africa.semicolon.promeescuous.models.Location;

import java.util.List;

public interface AddressService {
	
	AddressCreationResponse saveAddress(AddressCreationRequest addressCreationRequest);
	AddressUpdateResponse updateAddress(AddressUpdateRequest addressUpdateRequest);
	List<GetAddressResponse> getAllAddresses();
	GetAddressResponse getAddressById(Long id);
	List<GetAddressResponse> getAddressBy(Location location);
	void deleteAll();
}
