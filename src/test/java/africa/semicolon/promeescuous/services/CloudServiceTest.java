package africa.semicolon.promeescuous.services;

import africa.semicolon.promeescuous.dtos.responses.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CloudServiceTest {
	
	@Autowired
	private CloudService cloudService;
	
	@Test
	public void uploadImageTest(){
		String TEST_IMAGE_LOCATION = "C:\\Users\\USER\\Downloads\\DJANGO 3\\part 1 Uploading Files\\puppy.jpeg";
		Path path = Path.of(TEST_IMAGE_LOCATION);
		try(InputStream inputStream = Files.newInputStream(path)) {
			MultipartFile file = new MockMultipartFile("testImage", inputStream);
			ApiResponse<String> response = cloudService.upload(file);
			assertNotNull(response);
			assertThat(response).isNotNull();
		}catch (IOException exception){
			throw new RuntimeException(":(");
		}
	}
}