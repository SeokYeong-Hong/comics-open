package com.comicsopen.web.model;
import javax.persistence.*;import java.time.LocalDateTime;
@Entity @Table(name="posts") public class Post {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,length=80) public String title;
 @Column(nullable=false,length=2000) public String content;
 @ManyToOne(fetch=FetchType.EAGER,optional=false) public Member author;
 @Column(nullable=false) public LocalDateTime createdAt;
 protected Post(){} public Post(String title,String content,Member author){this.title=title;this.content=content;this.author=author;this.createdAt=LocalDateTime.now();}
}
