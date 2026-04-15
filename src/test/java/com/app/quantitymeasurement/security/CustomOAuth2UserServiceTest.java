package com.app.quantitymeasurement.security;

import com.app.quantitymeasurement.entity.User;
import com.app.quantitymeasurement.enums.*;
import com.app.quantitymeasurement.repository.UserRepository;
import com.app.quantitymeasurement.security.oauth2.CustomOAuth2UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.user.*;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

// Tests for CustomOAuth2UserService (Google + GitHub)
@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private TestableOAuth2UserService service;

    @BeforeEach
    void setUp() {
        service = new TestableOAuth2UserService(userRepository);
    }

    // ===================== GOOGLE =====================

    @Test
    void testGoogle_NewUser() {
        Map<String, Object> attrs = googleAttrs("alice@gmail.com", "Alice", null, "sub123");

        when(userRepository.findByEmail("alice@gmail.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class)))
            .thenReturn(savedUser("alice@gmail.com", AuthProvider.GOOGLE, "sub123"));

        OAuth2User result = service.process(userRequest("google"), oAuth2User(attrs, "sub"));

        assertInstanceOf(UserPrincipal.class, result);
        assertEquals("alice@gmail.com", ((UserPrincipal) result).getEmail());
    }

    @Test
    void testGoogle_ExistingUser_Update() {
        Map<String, Object> attrs = googleAttrs("bob@gmail.com", "Bob New", "pic", "sub");

        User existing = savedUser("bob@gmail.com", AuthProvider.GOOGLE, "sub");

        when(userRepository.findByEmail("bob@gmail.com")).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        service.process(userRequest("google"), oAuth2User(attrs, "sub"));

        verify(userRepository).save(argThat(u -> "Bob New".equals(u.getName())));
    }

    @Test
    void testGoogle_LocalConflict() {
        when(userRepository.findByEmail("carol@example.com"))
            .thenReturn(Optional.of(localUser("carol@example.com")));

        assertThrows(OAuth2AuthenticationException.class,
            () -> service.process(userRequest("google"),
                oAuth2User(googleAttrs("carol@example.com", "Carol", null, "sub"), "sub")));
    }

    // ===================== GITHUB =====================


    @Test
    void testGithub_LoginFallback() {
        Map<String, Object> attrs = githubAttrs(1, "login", null, "mail@mail.com", null);

        when(userRepository.findByEmail("mail@mail.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        service.process(userRequest("github"), oAuth2User(attrs, "id"));

        verify(userRepository).save(argThat(u -> "login".equals(u.getName())));
    }

    @Test
    void testGithub_NullEmail() {
        assertThrows(OAuth2AuthenticationException.class,
            () -> service.process(userRequest("github"),
                oAuth2User(githubAttrs(1, "login", "Name", null, null), "id")));
    }

    @Test
    void testGithub_ProviderConflict() {
        when(userRepository.findByEmail("x@mail.com"))
            .thenReturn(Optional.of(savedUser("x@mail.com", AuthProvider.GOOGLE, "id")));

        assertThrows(OAuth2AuthenticationException.class,
            () -> service.process(userRequest("github"),
                oAuth2User(githubAttrs(1, "login", "Name", "x@mail.com", null), "id")));
    }

    // ===================== OTHER =====================

    @Test
    void testUnsupportedProvider() {
        assertThrows(OAuth2AuthenticationException.class,
            () -> service.process(userRequest("facebook"),
                oAuth2User(Map.of("email","x"), "id")));
    }

    // ===================== HELPERS =====================

    static class TestableOAuth2UserService extends CustomOAuth2UserService {
        TestableOAuth2UserService(UserRepository repo) { super(repo); }

        OAuth2User process(OAuth2UserRequest req, OAuth2User user) {
            try {
                var m = CustomOAuth2UserService.class
                    .getDeclaredMethod("processOAuth2User", OAuth2UserRequest.class, OAuth2User.class);
                m.setAccessible(true);
                return (OAuth2User) m.invoke(this, req, user);
            } catch (Exception e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }

    private OAuth2UserRequest userRequest(String id) {
        return new OAuth2UserRequest(
            ClientRegistration.withRegistrationId(id)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId("id").clientSecret("secret")
                .redirectUri("url")
                .authorizationUri("url")
                .tokenUri("url")
                .userInfoUri("url")
                .userNameAttributeName("sub")
                .build(),
            new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "t",
                Instant.now(), Instant.now().plusSeconds(60))
        );
    }

    private OAuth2User oAuth2User(Map<String,Object> attrs, String key) {
        return new DefaultOAuth2User(Set.of(() -> "ROLE_USER"), attrs, key);
    }

    private User savedUser(String email, AuthProvider p, String id) {
        return User.builder().id(1L).email(email).provider(p).providerId(id).role(Role.USER).build();
    }

    private User localUser(String email) {
        return User.builder().email(email).provider(AuthProvider.LOCAL).password("x").role(Role.USER).build();
    }

    private Map<String,Object> googleAttrs(String email,String name,String pic,String sub){
        return Map.of("email",email,"name",name,"picture",pic,"sub",sub);
    }

    private Map<String,Object> githubAttrs(int id,String login,String name,String email,String avatar){
        Map<String,Object> m=new HashMap<>();
        m.put("id",id); m.put("login",login); m.put("name",name);
        m.put("email",email); m.put("avatar_url",avatar);
        return m;
    }
}