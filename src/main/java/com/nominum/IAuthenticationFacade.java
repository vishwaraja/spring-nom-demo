package com.nominum;

import org.springframework.security.core.Authentication;

/**
 * Created by vpathi on 1/8/17.
 */
public interface IAuthenticationFacade {
    Authentication getAuthentication();
}