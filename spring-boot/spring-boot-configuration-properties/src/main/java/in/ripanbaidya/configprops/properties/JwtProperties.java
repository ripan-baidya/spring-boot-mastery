package in.ripanbaidya.configprops.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * We use {@link ConfigurationProperties} on properties class.
 * In production, properties should be externalized using configuration files (such as application.yml or
 * application.properties) rather than hardcoded values to ensure security and maintainability.
 */
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        
        String secret,
        
        long expiration,
        
        String issuer,
        
        String header,
        
        String prefix
) {}
