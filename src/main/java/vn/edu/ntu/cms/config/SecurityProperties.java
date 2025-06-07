package vn.edu.ntu.cms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private PasswordStrength passwordStrength = new PasswordStrength();
    private AccountLockout accountLockout = new AccountLockout();
    private RememberMe rememberMe = new RememberMe();

    @Data
    public static class PasswordStrength {
        private int minLength = 8;
        private boolean requireUppercase = true;
        private boolean requireLowercase = true;
        private boolean requireDigit = true;
        private boolean requireSpecial = true;
    }

    @Data
    public static class AccountLockout {
        private int maxAttempts = 5;
        private String duration = "15m";
    }

    @Data
    public static class RememberMe {
        private String key;
        private String validityPeriod = "14d";
    }
} 