//package com.example.TMS.Securing;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Collection;
//
//public class AuthSuccessHandler implements AuthenticationSuccessHandler {
//    SimpleUrlAuthenticationSuccessHandler viewerSuccessHandler =
//            new SimpleUrlAuthenticationSuccessHandler("/product");
//    SimpleUrlAuthenticationSuccessHandler adminSuccessHandler =
//            new SimpleUrlAuthenticationSuccessHandler("/employee");
//    SimpleUrlAuthenticationSuccessHandler purchaseMngrSuccessHandler =
//            new SimpleUrlAuthenticationSuccessHandler("/plan");
//    SimpleUrlAuthenticationSuccessHandler warehouseSuccessHandler =
//            new SimpleUrlAuthenticationSuccessHandler("/warehouse");
//    SimpleUrlAuthenticationSuccessHandler cashierSuccessHandler =
//            new SimpleUrlAuthenticationSuccessHandler("/product");
//    SimpleUrlAuthenticationSuccessHandler deliveryMngrSuccessHandler =
//            new SimpleUrlAuthenticationSuccessHandler("/route");
//    SimpleUrlAuthenticationSuccessHandler courierSuccessHandler =
//            new SimpleUrlAuthenticationSuccessHandler("/route");
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
//        throws IOException, ServletException{
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        for (final GrantedAuthority grantedAuthority : authorities){
//            String authorityName = grantedAuthority.getAuthority();
//            if(authorityName.equals("Администратор")){
//                this.adminSuccessHandler.onAuthenticationSuccess(request, response, authentication);
//                return;
//            }
//            if(authorityName.equals("Закупщик")){
//                this.purchaseMngrSuccessHandler.onAuthenticationSuccess(request, response, authentication);
//                return;
//            }
//            if(authorityName.equals("Кладовщик")){
//                this.warehouseSuccessHandler.onAuthenticationSuccess(request, response, authentication);
//                return;
//            }
//            if(authorityName.equals("Кассир")){
//                this.cashierSuccessHandler.onAuthenticationSuccess(request, response, authentication);
//                return;
//            }
//            if(authorityName.equals("Логист")){
//                this.deliveryMngrSuccessHandler.onAuthenticationSuccess(request, response, authentication);
//                return;
//            }
//            if(authorityName.equals("Курьер")){
//                this.courierSuccessHandler.onAuthenticationSuccess(request, response, authentication);
//                return;
//            }
//        }
//        this.viewerSuccessHandler.onAuthenticationSuccess(request, response, authentication);
//    }
//}
