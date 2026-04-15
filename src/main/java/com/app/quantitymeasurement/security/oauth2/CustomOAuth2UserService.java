package com.app.quantitymeasurement.security.oauth2;

import java.util.Map;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.enums.AuthProvider;
import com.app.quantitymeasurement.enums.Role;
import com.app.quantitymeasurement.repository.UserRepository;
import com.app.quantitymeasurement.security.UserPrincipal;

import lombok.extern.slf4j.Slf4j;

/**
 * Loads and processes OAuth2 user data (Google/GitHub).
 */
@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processUser(userRequest, oAuth2User);
        } catch (OAuth2AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("OAuth2 processing error: {}", ex.getMessage());
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    // Main logic
    private UserPrincipal processUser(OAuth2UserRequest request, OAuth2User oAuth2User) {

        String provider = request.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthUserInfo info = extractUserInfo(provider, attributes);

        if (!StringUtils.hasText(info.email)) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_request",
                            "Email not found from " + provider, null));
        }

        return userRepository.findByEmail(info.email)
                .map(user -> updateUser(user, info, attributes))
                .orElseGet(() -> registerUser(info, attributes));
    }

    // Extract provider-specific data
    private OAuthUserInfo extractUserInfo(String provider, Map<String, Object> attr) {

        switch (provider) {
            case "google":
                return new OAuthUserInfo(
                        (String) attr.get("email"),
                        (String) attr.get("name"),
                        (String) attr.get("picture"),
                        (String) attr.get("sub"),
                        AuthProvider.GOOGLE
                );

            default:
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("unsupported_provider",
                                "Unsupported provider: " + provider, null));
        }
    }

    // Existing user logic
    private UserPrincipal updateUser(User user, OAuthUserInfo info, Map<String, Object> attr) {

        if (user.getProvider() == AuthProvider.LOCAL) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("account_conflict",
                            "Use email/password login", null));
        }

        if (user.getProvider() != info.provider) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("account_conflict",
                            "Use " + user.getProvider().name().toLowerCase() + " login", null));
        }

        user.setName(info.name);
        user.setImageUrl(info.imageUrl);

        User saved = userRepository.save(user);
        log.info("Updated user: {}", saved.getEmail());

        return UserPrincipal.create(saved, attr);
    }

    // New user registration
    private UserPrincipal registerUser(OAuthUserInfo info, Map<String, Object> attr) {

        User user = User.builder()
                .email(info.email)
                .name(info.name)
                .imageUrl(info.imageUrl)
                .provider(info.provider)
                .providerId(info.providerId)
                .role(Role.USER)
                .password(null)
                .build();

        User saved = userRepository.save(user);
        log.info("Registered new user: {}", saved.getEmail());

        return UserPrincipal.create(saved, attr);
    }

    // DTO for normalized data
    private static class OAuthUserInfo {
        final String email;
        final String name;
        final String imageUrl;
        final String providerId;
        final AuthProvider provider;

        OAuthUserInfo(String email, String name, String imageUrl,
                      String providerId, AuthProvider provider) {
            this.email = email;
            this.name = name;
            this.imageUrl = imageUrl;
            this.providerId = providerId;
            this.provider = provider;
        }
    }
}