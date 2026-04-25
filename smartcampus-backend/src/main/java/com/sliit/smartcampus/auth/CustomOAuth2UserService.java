package com.sliit.smartcampus.auth;

import com.sliit.smartcampus.user.Role;
import com.sliit.smartcampus.user.User;
import com.sliit.smartcampus.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Get the user attributes from Google
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

        // Debug: Print all attributes to see what's available
        System.out.println("Google User Attributes: " + attributes.keySet());

        // Extract user information
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");
        
        // Ensure we have an email
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from Google");
        }

        // Create or update user in database
        User user = userRepository.findByEmail(email)
                .map(existing -> {
                    existing.setName(name);
                    existing.setPictureUrl(picture);
                    return userRepository.save(existing);
                })
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(email)
                                .name(name)
                                .pictureUrl(picture)
                                .role(Role.USER)
                                .provider("GOOGLE")
                                .build()
                ));

        // Create a new attributes map that includes 'sub' (required by Spring Security)
        Map<String, Object> modifiedAttributes = new HashMap<>(attributes);
        if (!modifiedAttributes.containsKey("sub")) {
            // Use the 'id' as the unique identifier if 'sub' is not present
            modifiedAttributes.put("sub", attributes.get("id"));
        }

        // Return UserPrincipal with the user and attributes
        return UserPrincipal.create(user, modifiedAttributes);
    }
}