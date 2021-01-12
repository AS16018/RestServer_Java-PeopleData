/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ues.occ.edu.sv.ingenieria.prn335.serverpeople.entity;

/**
 *
 * @author christian
 */
public class Login {
    
    private String email;
    private String password;
    private String errorEmail;
    private String errorPassword;
    private String exito;
    
    public Login(){
        
    }
    
    public Login(String email, String password, String errorEmail, String errorPassword, String exito){
        this.email = email;
        this.password = password;
        this.errorEmail = errorEmail;
        this.errorPassword = errorPassword;
        this.exito = exito;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getErrorEmail() {
        return errorEmail;
    }

    public void setErrorEmail(String errorEmail) {
        this.errorEmail = errorEmail;
    }

    public String getErrorPassword() {
        return errorPassword;
    }

    public void setErrorPassword(String errorPassword) {
        this.errorPassword = errorPassword;
    }

    public String getExito() {
        return exito;
    }

    public void setExito(String exito) {
        this.exito = exito;
    }
    
    
}
