package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class PromiscuousCloudService implements CloudService{
	@Override
	public ApiResponse<String> upload(MultipartFile profileImage) {
		return null;
	}
}
