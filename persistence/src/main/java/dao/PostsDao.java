package dao;

import domain.Post;
import domain.User;

import java.util.List;

public interface PostsDao {
	List<Post> getPosts();
	Post getPostById(Long postId);
	List<Post> getPostsByAuthor(User user);
//	void addPost(Post post);
//	void deletePost(Long postId);
//	void deleteAllPostsByAuthor(User user);
}
