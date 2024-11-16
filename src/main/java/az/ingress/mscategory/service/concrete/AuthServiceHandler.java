package az.ingress.mscategory.service.concrete;

import az.ingress.mscategory.client.AuthClient;
import az.ingress.mscategory.model.client.AuthResponseDto;
import az.ingress.mscategory.service.abstracts.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceHandler implements AuthService {
    private final AuthClient authClient;
    @Override
    public AuthResponseDto validateToken(String accessToken) {
        return authClient.validateToken(accessToken);
    }
}