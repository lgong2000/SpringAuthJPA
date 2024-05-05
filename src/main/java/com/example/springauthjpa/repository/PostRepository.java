package com.example.springauthjpa.repository;

import com.example.springauthjpa.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
}
