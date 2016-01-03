package com.pkry.db.model.DTOs;

import java.util.List;

public class AuthDTO {

    String login;

    String password;

    List<AuthSessionDTO> authSessionDTOList;

    AccountDTO accountDTO;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AuthSessionDTO> getAuthSessionDTOList() {
        return authSessionDTOList;
    }

    public void setAuthSessionDTOList(List<AuthSessionDTO> authSessionDTOList) {
        this.authSessionDTOList = authSessionDTOList;
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }
}
