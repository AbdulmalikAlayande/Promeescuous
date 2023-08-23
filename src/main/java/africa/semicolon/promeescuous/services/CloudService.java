package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.responses.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CloudService {
	
	
	ApiResponse<String> upload(MultipartFile profileImage);
	
}
