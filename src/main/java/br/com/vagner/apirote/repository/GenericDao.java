package br.com.vagner.apirote.repository;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import br.com.vagner.apirote.exception.DAOException;

/**
 * Generic Dao for gereric use purposes
 * @author Vagner
 *
 * @param <E>
 * @param <I>
 */
public abstract class GenericDao<E, I> {

    private static final Logger LOGGER = Logger.getLogger(GenericDao.class);

    private Class<E> entityClass;

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public GenericDao() {
        this.entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public E find(I id) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: find (id):" + id);
            return getEntityManager().find(entityClass, id);

        } catch (EntityNotFoundException nf) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Entity not found " + entityClass + ". Id " + id, nf);
            }
            return null;
        } catch (Exception e) {
            throw new DAOException("Erro ao tentar recuperar "+ entityClass + " [id=" + id + "]", e);
        }
    }

/*    private TypedQuery<E> queryFindAll() throws DAOException {
        try{
            LOGGER.debug("Parametros de entrada, DAO: queryFindAll ():" );
           
            CriteriaQuery<E> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
            return getEntityManager().createQuery(cq);
        }
        catch(Exception e){
            throw new DAOException("Erro ao tentar recuperar lista de "+ entityClass, e);
        }
    }

    private TypedQuery<E> queryFindAllOrderBy(String orderBy, Boolean isAsc) throws DAOException {
        try{
            LOGGER.debug("Parametros de entrada, DAO: queryFindAllOrderBy (orderBy, isAsc):" + orderBy + "," + isAsc);
            
            CriteriaQuery<E> cq = cq.createQuery(entityClass);
            Root<E> c = cq.from(entityClass);
            cq.select(c);
    
            if (!orderBy.isEmpty()) {
                if (isAsc) {
                    cq.orderBy(cb.desc(c.get(orderBy)));
                } else {
                    cq.orderBy(cb.asc(c.get(orderBy)));
                }
            }
            return getEntityManager().createQuery(cq);
           
        }
        catch(Exception e){
            throw new DAOException("Erro ao tentar recuperar lista de "+ entityClass, e);
        }
    }*/

 /*   public List<E> findAll(String orderBy, Boolean isAsc) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: queryFindAllOrderBy (orderBy, isAsc):" + orderBy + "," + isAsc);
            TypedQuery<E> t = queryFindAllOrderBy(orderBy, isAsc);
            return t.getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao tentar recuperar lista de "+ entityClass, e);
        }
    }*/

 /*   public List<E> findAll() throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: findAll ():");
            TypedQuery<E> t = queryFindAll();
            return t.getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao tentar recuperar lista de "+ entityClass, e);
        }
    }*/

/*    public List<E> findAll(Integer startPosition) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: findAll (startPosition):" + startPosition);
            TypedQuery<E> t = queryFindAll();
            t.setFirstResult(startPosition);
            return t.getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao tentar recuperar lista de "+ entityClass, e);
        }
    }*/

/*    public List<E> findAll(Integer startPosition, Integer maxResult) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: findAll (startPosition, maxResult):" + startPosition + "," + maxResult);
            TypedQuery<E> t = queryFindAll();
            t.setFirstResult(startPosition);
            t.setMaxResults(maxResult);
            return t.getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao tentar recuperar lista de "+ entityClass, e);
        }
    }

    public List<E> findAll(String orderBy, Boolean isAsc, Integer startPosition, Integer maxResult) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: findAll (orderBy, isAsc, startPosition, maxResult):" + orderBy + "," + isAsc + "," + startPosition + "," + maxResult);
            TypedQuery<E> t = queryFindAllOrderBy(orderBy, isAsc);
            t.setFirstResult(startPosition);
            t.setMaxResults(maxResult);
            return t.getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao tentar recuperar lista de "+ entityClass, e);
        }
    }
*/
    public E persist(E entity) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: persist (entity):" + entity);
            getEntityManager().persist(entity);
            return entity;
        } catch (ConstraintViolationException ie) {
            String msg = buildMessageInvalidState(ie);
            throw new DAOException("Valores invalidos ao salvar entidade " + entityClass + ". Erros: " + msg, ie);
        } catch (Exception e) {
            throw new DAOException("Erro ao tentar persistir [" + entityClass + "=" + entity + "]", e);
        }
    }

    public Collection<E> persistAll(Collection<E> entities) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: persistAll (entities):" + entities);
            if (entities == null || entities.isEmpty()) {
                return Collections.<E> emptyList();
            }
            List<E> ret = new ArrayList<E>();
            for (E e : entities) {
                getEntityManager().persist(e);
                ret.add(e);
            }
            return ret;
        } catch (Exception e) {
            throw new DAOException("Erro ao tentar persistir lista de "+ entityClass + "[entities="+ entities +"]", e);
        }
    }

    public E merge(E entity) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: merge (entity):" + entity);
            getEntityManager().merge(entity);
            return entity;
        } catch (ConstraintViolationException ie) {
            String msg = buildMessageInvalidState(ie);
            throw new DAOException("Valores invalidos para merge na entidade " + entity + ". Erros: " + msg, ie);
        } catch (Exception e) {
            throw new DAOException("Erro ao tentar merge de [" + entityClass + "=" + entity + "]", e);
        }
    }

    public void mergeAll(Collection<E> entities) throws DAOException {
        if (entities != null && !entities.isEmpty()) {
            for (E e : entities) {
                merge(e);
            }
        }
    }

    public void remove(E entity) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: remove (entity):" + entity);
            getEntityManager().remove(entity);
        } catch (Exception e) {
            throw new DAOException("Erro ao tentar deletar [" + entityClass + "=" + entity + "]", e);
        }
    }

    public Query getNamedQuery(String queryName) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: getNamedQuery (queryName):" + queryName);
            return getEntityManager().createNamedQuery(queryName);
        } catch (Exception e) {
            throw new DAOException("Erro ao obter queryName [queryName=" + queryName + "]", e);
        }
    }

    // methods protected

    protected Query createQuery(String ejbql) throws DAOException {
        try{    
            LOGGER.debug("Parametros de entrada, DAO: createQuery (ejbql):" + ejbql);
            return createQuery(ejbql, null);
        } catch (Exception e) {
            throw new DAOException("Erro ao crair query [ejbql=" + ejbql + "]", e);
        }
    }

    protected Query createQuery(String ejbql, Map<String, Object> parameters) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: createQuery (ejbql, parameters):" + ejbql + "," + parameters);
            Query q = getEntityManager().createQuery(ejbql);
            if (parameters != null && !parameters.isEmpty()) {
                for (Entry<String, Object> entry : parameters.entrySet()) {
                    Object value = entry.getValue();
                    if (value != null) {
                        q.setParameter(entry.getKey(), value);
                    }
                }
            }
            return q;
        } catch (Exception e) {
            throw new DAOException("Erro ao crair query [ejbql=" + ejbql + "]", e);
        }
    }

    @SuppressWarnings("rawtypes")
    protected Query createQuery(String hql, Map<String, Object> param, Integer firstResult, Integer maxResults) throws DAOException {
        try{
            Query q = createQuery(hql);
            if (param != null && !param.isEmpty()) {
                for (Entry<String, Object> entry : param.entrySet()) {
                    Object val = entry.getValue();
                    if (val instanceof String) {
                        q.setParameter(entry.getKey(), (String) val);
                    } else if (val instanceof Integer) {
                        q.setParameter(entry.getKey(), (Integer) val);
                    } else if (val instanceof Long) {
                        q.setParameter(entry.getKey(), (Long) val);
                    } else if (val instanceof Collection) {
                        q.setParameter(entry.getKey(), (Collection) val);
                    } else {
                        q.setParameter(entry.getKey(), val);
                    }
                }
            }
            if (firstResult != null && maxResults != null) {
                q.setFirstResult(firstResult).setMaxResults(maxResults);
            }
            return q;
        } catch (Exception e) {
            throw new DAOException("Erro ao crair query [hql=" + hql + "]", e);
        }

    }

    @SuppressWarnings("unchecked")
    protected List<E> getResultList(Query query) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: getResultList (query):" + query);
            return (List<E>) query.getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao recuperar lista de resultados [query=" + query + "]", e);
        }
    }

    @SuppressWarnings("unchecked")
    protected E getSingleResult(Query query) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: getSingleResult (query):" + query);
            return (E) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new DAOException("Erro ao crair query [query=" + query + "]", e);
        }
    }

    protected Long executeCount(Query query) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: executeCount (query):" + query);
            Number number = (Number) query.getSingleResult();
            return (number == null) ? 0 : number.longValue();
        } catch (Exception e) {
            throw new DAOException("Erro ao crair query [query=" + query + "]", e);
        }
    }

    protected Long executeCount(String hql, Map<String, Object> param) throws DAOException {
        try{
            Query query = createQuery(hql, param);
            return executeCount(query);
        } catch (Exception e) {
            throw new DAOException("Erro ao crair query [hql=" + hql + "]", e);
        }
    }

    protected int executeUpdate(Query query) throws DAOException {
        try {
            LOGGER.debug("Parametros de entrada, DAO: executeUpdate (query):" + query);
            return query.executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao crair query [query=" + query + "]", e);
        }
    }

    protected void setPage(Integer offset, Integer maxResults, Query query) {
        if (offset != null && offset >= 0 && maxResults != null && maxResults > 0) {
            query.setFirstResult(offset).setMaxResults(maxResults);
        }
    }

    private String buildMessageInvalidState(ConstraintViolationException ie) {
        StringBuilder msg = new StringBuilder();
        for (ConstraintViolation<?> v : ie.getConstraintViolations()) {
            msg.append(", ").append("Property: ").append(v.getPropertyPath()).append(" - Cause: ").append(v.getMessage());
        }
        return msg.substring(2, msg.length());
    }

}
