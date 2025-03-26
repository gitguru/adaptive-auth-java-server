package com.apidynamics.test.server_demo.service;

import com.apidynamics.test.server_demo.entity.ApiProvider;
import com.apidynamics.test.server_demo.model.ApiDynamicsClient;
import com.apidynamics.test.server_demo.repository.ApiProviderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.util.Pair;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class AdaptiveAuthenticationService {

    private final static Logger LOG = LoggerFactory.getLogger(AdaptiveAuthenticationService.class);

    private final ApiProviderRepository apiProviderRepository;
    private final RestTemplate restTemplate;

    @Value("${API_DYNAMICS_PROVIDER_ID}")
    private String apiDynamicsProviderId;

    @Autowired
    public AdaptiveAuthenticationService(RestTemplate restTemplate, ApiProviderRepository apiProviderRepository) {
        this.restTemplate = restTemplate;
        this.apiProviderRepository = apiProviderRepository;
    }

    private String getProviderId() {
        Optional<ApiProvider> apiProvider = apiProviderRepository.findById(1);
        String providerId = null;
        if (apiProvider.isPresent()) {
            providerId = apiProvider.get().getPublicKey();
        } else {
            providerId = apiDynamicsProviderId;
        }
        LOG.debug("ProviderId: {}", providerId);
        return providerId;
    }

    /**
     * Performs a TOTP token generation by calling Apy Dynamics server enforcing API Client to generate it
     * @param clientId - Api Client public key gotten from client request
     * @param transactionId - Current transaction Id
     * @return - Pair with Api Dynamics status and body
     */
    public Pair<HttpStatusCode, Map<String, Object>> generateApiClientTotp(String clientId, String transactionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("X-API-Dynamics-Provider-Id", getProviderId());
        headers.add("X-API-Dynamics-Client-Id", clientId);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/totp/client/generate?tid={transactionId}", HttpMethod.GET, entity, responseType, transactionId);
        return Pair.of(response.getStatusCode(), response.getBody());
    }

    /**
     * Performs a TOTP token validation by calling Apy Dynamics server
     * @param transactionId - Current transaction Id
     * @param totp - Actual TOTP token, could be self or server generated
     * @return - Pair with Api Dynamics status and body
     */
    public Pair<HttpStatusCode, Map<String, Object>> validateApiClientTotp(String transactionId, String totp) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("X-API-Dynamics-Provider-Id", getProviderId());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<>() {};
        Map<String, String> uriVariables = Map.of("transactionId", transactionId, "totp", totp);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/totp/server/validate?tid={transactionId}&totp={totp}", HttpMethod.GET, entity, responseType, uriVariables);
        return Pair.of(response.getStatusCode(), response.getBody());
    }

    /**
     * Perform Adaptive Authentication call to Api Dynamics server
     * @param apiDynamicsClient - Api Client details
     * @return - Pair with Api Dynamics status and body
     */
    public Pair<HttpStatusCode, Map<String, Object>> validateAdaptiveApiClient(ApiDynamicsClient apiDynamicsClient) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-API-Dynamics-Provider-Id", getProviderId());
        HttpEntity<?> entity = new HttpEntity<>(apiDynamicsClient.stringify(), headers);
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("/adaptive/server/validate", HttpMethod.POST, entity, responseType);
        return Pair.of(response.getStatusCode(), response.getBody());
    }
}
