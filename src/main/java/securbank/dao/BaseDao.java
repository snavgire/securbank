package securbank.dao;

import java.io.Serializable;

/**
 * @author Ayush Gupta
 *
 * @param <T>     
 * @param <ID>
 */
public interface BaseDao<T, PK extends Serializable> {
    public T findById(PK id);
    public T save(T entity);
    public void remove(T entity);
    public T update(T entity);
    public void flush();
    public void refresh(T entity);
    /**
     * Clears the session
     */
    public void clear();
}
