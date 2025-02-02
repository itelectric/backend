package com.itelectric.backend.v1.service.impl;

import com.itelectric.backend.v1.domain.entity.User;
import com.itelectric.backend.v1.domain.exception.*;
import com.itelectric.backend.v1.service.contract.IKeycloakCLIService;
import com.itelectric.backend.v1.utils.FuncUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.TokenVerifier;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class KeycloakCLIService implements IKeycloakCLIService {
    private Keycloak keycloak;

    @Value("${keycloak.auth-server-url}")
    private String baseUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    @Value("${keycloak.grant-type}")
    private String grantType;
    @Value("${keycloak.scope}")
    private String scope;

    @PostConstruct
    public void init() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(baseUrl)
                .realm(realm)
                .clientId(clientId)
                .username(username)
                .password(password)
                .grantType(grantType)
                .build();
    }

    @Override
    public void createUser(User user) throws ConflictException, BusinessException, UnexpectedException, ForbiddenException {
        //Creating user
        UserRepresentation userToSave = new UserRepresentation();
        userToSave.setUsername(user.getUsername());
        userToSave.setEmail(user.getContact().getEmail());
        userToSave.setFirstName(user.getName());
        userToSave.setLastName(user.getName());
        userToSave.setEnabled(true);
        //Creating credentials
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(user.getPassword());
        userToSave.setCredentials(Collections.singletonList(credential));
        //Adding user to keycloak
        Response response = this.keycloak.realm(this.realm).users().create(userToSave);
        //getting user id
        String location = response.getHeaderString("Location");
        String userId = location.substring(location.lastIndexOf("/") + 1);
        //Associate user to group
        String groupName = user.getType().name();
        GroupsResource groupsResource = this.keycloak.realm(this.realm).groups();
        GroupRepresentation group = groupsResource.groups()
                .stream()
                .filter(g -> g.getName().equals(groupName))
                .findFirst()
                .orElseThrow(() -> new UnexpectedException("Cannot find group " + groupName));
        // adding user tio group
        this.keycloak.realm(this.realm).users().get(userId).joinGroup(group.getId());
        FuncUtils.handlingKeycloakResponse(response);
        this.close();
    }

    @Override
    public String login(String username, String password) throws UnauthorizedException {
        try {
            Keycloak keycloakLogin = KeycloakBuilder.builder()
                    .serverUrl(this.baseUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType(grantType)
                    .username(username)
                    .password(password)
                    .build();

            AccessTokenResponse tokenResponse = keycloakLogin.tokenManager().getAccessToken();
            return tokenResponse.getToken();
        } catch (Exception ex) {
            throw new UnauthorizedException(ex.getMessage());
        }
    }

    public void close() {
        keycloak.close();
    }

    public String getUserInfo(String token,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              FilterChain filterChain) throws UnauthorizedException, ServletException, IOException {
        try {
            Keycloak keycloak = KeycloakBuilder.builder()
                    .serverUrl(this.baseUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .authorization(token)
                    .build();

            String userId = TokenVerifier.create(token, AccessToken.class).getToken().getSubject();
            UserRepresentation user = keycloak.realm(realm).users().get(userId).toRepresentation();
            return user.getUsername();
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
    }
}
