package com.contactmanager.config;


import com.contactmanager.entities.Providers;
import com.contactmanager.entities.User;
import com.contactmanager.helpers.AppConstants;
import com.contactmanager.repositories.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{
    @Autowired
    private UserRepo userRepo;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {

         var oauth2AuthenticationToken= (OAuth2AuthenticationToken)authentication;

         String authorizedClientRegistrationId =  oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
         var oauthUser=(OAuth2User)authentication.getPrincipal();

         System.out.println("=== OAuth2 User Attributes ===");
         oauthUser.getAttributes().forEach((key,value) -> {
             System.out.println(key + ": " + value);
         });

         User user = new User();

         user.setUserId(UUID.randomUUID().toString());
         user.setRoleList(List.of(AppConstants.ROLE_USER));
         user.setEmailVerified(true);
         user.setEnabled(true);
         user.setPassword("dummy");
         user.setAbout("This account is create using google");


         String picture = null;

         if(authorizedClientRegistrationId.equalsIgnoreCase("google"))
         {
             String email = oauthUser.getAttribute("email");
             String name = oauthUser.getAttribute("name");
             picture = oauthUser.getAttribute("picture");
             String sub = oauthUser.getAttribute("sub");

             System.out.println("Google OAuth2 attributes - email: " + email + ", name: " + name + ", sub: " + sub + ", picture: " + picture + ", picture type: " + (picture != null ? picture.getClass().getName() : "null"));

             if (email != null) {
                 user.setEmail(email);
             } else {
                 throw new RuntimeException("Email not found in Google OAuth2 response");
             }
             user.setProfilePic(picture != null ? picture.toString() : "");
             user.setName(name != null ? name : "");
             user.setProviderUserId(sub);
             user.setProvider(Providers.GOOGLE);

         }
         else if (authorizedClientRegistrationId.equalsIgnoreCase("github"))
         {
             String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString()
                     : oauthUser.getAttribute("login").toString() + "@gmail.com";
             picture = oauthUser.getAttribute("avatar_url") != null ? oauthUser.getAttribute("avatar_url").toString() : null;
             String name = oauthUser.getAttribute("login").toString();
             String providerUserId = oauthUser.getName();

             System.out.println("GitHub OAuth2 attributes - email: " + email + ", name: " + name + ", picture: " + picture);

             user.setEmail(email);
             user.setProfilePic(picture != null ? picture : "");
             user.setName(name);
             user.setProviderUserId(providerUserId);
             user.setProvider(Providers.GITHUB);

             user.setAbout("This account is created using github");
         }


        /*

        DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        String email= user.getAttribute("email").toString();
        String name= user.getAttribute("name").toString();
        String picture= user.getAttribute("picture").toString();



        User user1 = new User();
        user1.setEmail(email);
        user1.setName(name);
        user1.setProfilePic(picture);
        user1.setPassword("password");
        user1.setUserId(UUID.randomUUID().toString());
        user1.setProvider(Providers.GOOGLE);


        user1.setEmailVerified(true);
        user1.setProviderUserId(user.getName());
        user1.setRoleList(List.of(AppConstants.ROLE_USER));
        user1.setAbout("This account is created using google");



        */

        User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);

        if(user2 == null)
        {
            userRepo.save(user);
        }
        else
        {
            // Update existing user's profile picture if it's empty or from Google login
            if(authorizedClientRegistrationId.equalsIgnoreCase("google") && picture != null)
            {
                if(user2.getProfilePic() == null || user2.getProfilePic().isEmpty())
                {
                    user2.setProfilePic(picture);
                    userRepo.save(user2);
                }
            }
        }





        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");


    }
}
