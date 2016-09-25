package securbank.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Ayush Gupta
 *
 * @param <T>
 * @param <PK>
 */
public class BaseDaoImpl<T, PK extends Serializable> implements BaseDao<T, PK> {
    @PersistenceContext
    protected EntityManager entityManager;
    protected Class<T> entityClass;
    
    public BaseDaoImpl() {
    }
    
    public BaseDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    public BaseDaoImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    /**
     * Returns an element for the given Id.
     * 
     * @param id
     *            The id to query db
     * @return element
     */
    public T findById(PK id) {
        return this.entityManager.find(this.entityClass, id);
    }
    
    /**
     * This method can be used when the updated object need to be returned
     * 
     * @param entity
     *            The entity to update
     * @return The saved entity
     */
    public T update(T entity) {
        return this.entityManager.merge(entity);
    }
    
    /**
     * Creates an entity in DB
     * 
     * @param entity
     *            entity to save to.
     */
    public T save(T entity) {
        this.entityManager.persist(entity);
        return entity;
    }
    
    /**
     * Removes an entity from DB
     * 
     * @param entity
     *            Entity to remove
     */
    public void remove(T entity) {
        this.entityManager.remove(entity);
    }
    
    /**
     * Flushes the entity manager.
     */
    public void flush() {
        this.entityManager.flush();
    }
    
    /**
     * Refreshes the entity
     * 
     * @param entity
     *            The entity to refresh
     */
    public void refresh(T entity) {
        this.entityManager.refresh(entity);
    }
    
    /**
     * Clears the session
     */
    public void clear() {
        this.entityManager.clear();
    }
}
