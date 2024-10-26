package az.ingress.mscategory.client;

import az.ingress.mscategory.client.decoder.CustomErrorDecoder;
import az.ingress.mscategory.model.client.AuthResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-auth", url = "${client.urls.ms-auth}"
        ,path = "/internal"
        ,configuration = CustomErrorDecoder.class)
public interface AuthClient {
    @PostMapping("/validate-token")
    AuthResponseDto validateToken(@RequestHeader("Authorization") String accessToken);
}
