package com.lecture.security.security.service;

import com.lecture.security.domain.entitiy.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AccountContext extends User {
    private final Account account;

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities){
        //여기서 super의 의미를 모르겠다.
        super(account.getUsername(), account.getPassword(), authorities);
        this.account = account;
    }
}
