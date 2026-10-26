package com.comicsopen.web.dto;
import javax.validation.constraints.*;
public class Requests {
 public static class Signup {
  @NotBlank @Email public String email;
  @NotBlank @Size(min=2,max=30) public String nickname;
  @NotBlank @Size(min=8,max=72) public String password;
  public Signup(){}
  public String email(){return email;} public String nickname(){return nickname;} public String password(){return password;}
 }
 public static class Login {
  @NotBlank public String email; @NotBlank public String password;
  public Login(){} public String email(){return email;} public String password(){return password;}
 }
 public static class PostInput {
  @NotBlank @Size(max=80) public String title;
  @NotBlank @Size(max=2000) public String content;
  public PostInput(){} public String title(){return title;} public String content(){return content;}
 }
}
