package com.comicsopen.web.model;
import javax.persistence.*;
@Entity @Table(name="members") public class Member {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,unique=true,length=180) public String email;
 @Column(nullable=false,length=60) public String nickname;
 @Column(nullable=false) public String passwordHash;
 protected Member(){} public Member(String email,String nickname,String hash){this.email=email;this.nickname=nickname;this.passwordHash=hash;}
}
