package com.imaginepartners.imagineworkflow.security.authentication;

import com.imaginepartners.imagineworkflow.services.BusinessService;
import com.imaginepartners.imagineworkflow.util.ConfigConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetailsContextMapper extends InetOrgPersonContextMapper implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private BusinessService businessService;

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        List<GrantedAuthority> grantedAutorities = new ArrayList<GrantedAuthority>();

        String grpDefaultPrefix = businessService.getConfigValue(ConfigConstants.GROUP_ID_DEFAULT_PREFIX);
        for (GrantedAuthority ga : authorities) {
            if (StringUtils.isNotBlank(ga.getAuthority())) {
                if (ga.getAuthority().startsWith(grpDefaultPrefix)) {
                    grantedAutorities.add(ga);
                } else {
                    grantedAutorities.add(new SimpleGrantedAuthority(grpDefaultPrefix + ga.getAuthority()));
                }
            }
        }
        UserDetails user = super.mapUserFromContext(ctx, username, grantedAutorities);
        return user;
    }
}
