package uk.co.transferx.app.tokenmanager;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.HttpsURLConnection;

import io.reactivex.Single;
import uk.co.transferx.app.api.SignUpApi;
import uk.co.transferx.app.pojo.TokenEntity;

/**
 * Created by sergey on 08/01/2018.
 */
@Singleton
public class TokenManager {

    private TokenEntity cashedToken;
    private final TokenRepository tokenRepository;
    private final SignUpApi signUpApi;

    @Inject
    public TokenManager(final TokenRepository tokenRepository, final SignUpApi signUpApi) {
        this.tokenRepository = tokenRepository;
        this.signUpApi = signUpApi;
    }

    public Single<TokenEntity> getToken() {
        if (cashedToken == null) {
            cashedToken = tokenRepository.getToken();
        }
        if (!isExpired(cashedToken.getCreated() + cashedToken.getExpiresIn())) {
            return Single.just(cashedToken);
        }
        return signUpApi.refreshToken(cashedToken.getRefreshToken())
                .flatMap(resp -> {
                    if (resp.code() == HttpsURLConnection.HTTP_OK) {
                        cashedToken = resp.body();
                        tokenRepository.saveToken(cashedToken);
                        return Single.just(cashedToken);
                    }
                    return Single.error(new Throwable("Error responce" + resp.code()));
                });
    }

    private boolean isExpired(long expirationTime) {
        return expirationTime <= System.currentTimeMillis();
    }

    public boolean shouldSaveGenesis() {
        return tokenRepository.getToken().getRefreshToken() == null || tokenRepository.getToken().getRefreshToken().isEmpty();
    }

    public void saveToken(final TokenEntity tokenEntity) {
        tokenRepository.saveToken(tokenEntity);
        cashedToken = tokenEntity;
    }
}
