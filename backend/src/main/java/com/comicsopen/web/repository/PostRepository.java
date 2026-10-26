package com.comicsopen.web.repository;
import com.comicsopen.web.model.Post;import org.springframework.data.jpa.repository.JpaRepository;import java.util.List;
public interface PostRepository extends JpaRepository<Post,Long>{List<Post> findAllByOrderByCreatedAtDesc();}
