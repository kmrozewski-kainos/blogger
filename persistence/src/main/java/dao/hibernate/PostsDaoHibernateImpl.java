package dao.hibernate;

import dao.PostsDao;
import dao.hibernate.entities.PostEntity;
import domain.Post;
import domain.User;
import io.dropwizard.hibernate.AbstractDAO;
import mapper.PostMapper;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.util.List;

import static com.google.common.collect.Lists.transform;

public class PostsDaoHibernateImpl extends AbstractDAO<PostEntity> implements PostsDao {

	private final static String SELECT_ALL = "SELECT p FROM PostEntity p";

	private PostMapper postMapper;

	@Inject
	public PostsDaoHibernateImpl(SessionFactory sessionFactory, PostMapper postMapper) {
		super(sessionFactory);
		this.postMapper = postMapper;
	}

	@Override
	public List<Post> getPosts() {
		Query query = currentSession().createQuery(SELECT_ALL);
		List<PostEntity> postEntities = list(query);
		throwExceptionIfNull(postEntities);

		return transform(postEntities, postMapper::mapPostEntityToPost);
	}

	@Override
	public Post getPostById(Long postId) {
		Criteria criteria = currentSession().createCriteria(PostEntity.class);
		criteria.add(Restrictions.idEq(postId));

		return postMapper.mapPostEntityToPost(uniqueResult(criteria));
	}

	@Override
	public List<Post> getPostsByAuthor(User user) {
		Criteria criteria = currentSession().createCriteria(PostEntity.class);
		criteria.add(Restrictions.eq("user.id", user.getId()));
		List<PostEntity> postEntities = list(criteria);
		throwExceptionIfNull(postEntities);

		return transform(postEntities, postMapper::mapPostEntityToPost);
	}

	private void throwExceptionIfNull(List<PostEntity> entities) {
		if (entities == null) {
			throw new WebApplicationException("no posts found");
		}
	}
}
