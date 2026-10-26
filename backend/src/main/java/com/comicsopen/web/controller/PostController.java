package com.comicsopen.web.controller;
import com.comicsopen.web.dto.Requests;import com.comicsopen.web.model.*;import com.comicsopen.web.repository.*;import javax.servlet.http.HttpSession;import javax.validation.Valid;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.time.LocalDateTime;import java.util.*;
@RestController @RequestMapping("/api/posts") public class PostController {
 private final PostRepository posts;private final MemberRepository members;
 public PostController(PostRepository posts,MemberRepository members){this.posts=posts;this.members=members;}
 public static class PostView {
  public final Long id; public final String title,content,author; public final Long authorId; public final LocalDateTime createdAt;
  public PostView(Long id,String title,String content,String author,Long authorId,LocalDateTime createdAt){this.id=id;this.title=title;this.content=content;this.author=author;this.authorId=authorId;this.createdAt=createdAt;}
 }
 private PostView view(Post p){return new PostView(p.id,p.title,p.content,p.author.nickname,p.author.id,p.createdAt);}
 private Long current(HttpSession s){Object id=s.getAttribute("memberId");return id instanceof Long?(Long)id:null;}
 @GetMapping public List<PostView> all(){return posts.findAllByOrderByCreatedAtDesc().stream().map(this::view).collect(java.util.stream.Collectors.toList());}
 @PostMapping public ResponseEntity<?> create(@Valid @RequestBody Requests.PostInput input,HttpSession s){Long id=current(s);if(id==null)return ResponseEntity.status(401).body(Map.of("error","로그인이 필요합니다."));Member m=members.findById(id).orElse(null);if(m==null)return ResponseEntity.status(401).body(Map.of("error","계정을 찾을 수 없습니다."));return ResponseEntity.status(201).body(view(posts.save(new Post(input.title().trim(),input.content().trim(),m))));}
 @PutMapping("/{id}") public ResponseEntity<?> edit(@PathVariable Long id,@Valid @RequestBody Requests.PostInput input,HttpSession s){Post p=posts.findById(id).orElse(null);if(p==null)return ResponseEntity.notFound().build();if(current(s)==null)return ResponseEntity.status(401).body(Map.of("error","로그인이 필요합니다."));if(!p.author.id.equals(current(s)))return ResponseEntity.status(403).body(Map.of("error","작성자만 수정할 수 있습니다."));p.title=input.title().trim();p.content=input.content().trim();return ResponseEntity.ok(view(posts.save(p)));}
 @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable Long id,HttpSession s){Post p=posts.findById(id).orElse(null);if(p==null)return ResponseEntity.notFound().build();if(current(s)==null)return ResponseEntity.status(401).body(Map.of("error","로그인이 필요합니다."));if(!p.author.id.equals(current(s)))return ResponseEntity.status(403).body(Map.of("error","작성자만 삭제할 수 있습니다."));posts.delete(p);return ResponseEntity.noContent().build();}
}
