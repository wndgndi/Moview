package com.personal.movie.repository;

import com.personal.movie.domain.Post;
import com.personal.movie.domain.constants.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitleContainingIgnoreCase(String keyword);

    List<Post> findByContentContainingIgnoreCase(String keyword);

    List<Post> findByMember_MemberName(String memberName);

    List<Post> findByCategory(Category category);
}
