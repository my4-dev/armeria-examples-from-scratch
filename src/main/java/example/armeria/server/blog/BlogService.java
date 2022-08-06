package example.armeria.server.blog;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BlogService {
  private final Map<Integer, BlogPost> blogPosts = new ConcurrentHashMap<>();

  @Post("/blogs")
  @RequestConverter(BlogPostRequestConverter.class)
  public HttpResponse createBlogPost(BlogPost blogPost) {
    blogPosts.put(blogPost.getId(), blogPost);
    return HttpResponse.ofJson(blogPost);
  }

  @Get("/blogs/:id")
  public HttpResponse getBlogPost(@Param int id) {
    BlogPost blogPost = blogPosts.get(id);
    return HttpResponse.ofJson(blogPost);
  }

  @Put("/blogs/:id")
  public HttpResponse updateBlogPost(@Param int id, @RequestObject BlogPost blogPost) {
    BlogPost oldBlogPost = blogPosts.get(id);
    if (oldBlogPost == null) {
      // Return a Not Found error. See the next section for instruction.
      return HttpResponse.of(HttpStatus.NOT_FOUND);
    }
    BlogPost newBlogPost = new BlogPost(id, blogPost.getTitle(), blogPost.getContent(), oldBlogPost.getCreatedAt(), blogPost.getCreatedAt());
    blogPosts.put(id, newBlogPost);
    return HttpResponse.ofJson(newBlogPost);
  }
}
