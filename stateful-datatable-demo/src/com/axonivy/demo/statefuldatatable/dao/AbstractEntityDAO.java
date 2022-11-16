package com.axonivy.demo.statefuldatatable.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.demo.statefuldatatable.entity.AbstractEntity;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.process.data.persistence.IIvyEntityManager;

/**
 * Base class for DAO classes for usage with {@link AbstractEntity}
 *
 * @param <AE>
 */
public abstract class AbstractEntityDAO<AE extends AbstractEntity> implements IAbstractEntityDAO<AE> {

    protected static final String PERSISTENCE_UNIT = "statefulDatatable";

    protected IIvyEntityManager getEntityManager() {
        return getIvyEntityManager();
    }

    protected IIvyEntityManager getIvyEntityManager() {
        return Ivy.persistence().get(PERSISTENCE_UNIT);
    }

    protected CriteriaBuilder getCriteriaBuilder() {
        return getEntityManager().createEntityManager().getCriteriaBuilder();
    }

    protected CriteriaQuery<AE> createQuery() {
        return getCriteriaBuilder().createQuery(getType());
    }

    @Override
    public AE save(AE entity) {
        EntityManager em = null;

        try {
            em = getEntityManager().createEntityManager();
            em.getTransaction().begin();

            if (!StringUtils.isEmpty(entity.getId())) {
                entity = em.merge(entity);
            } else {
                em.persist(entity);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            Ivy.log().error("AbstractDAO: error saving data: ", e);
        } finally {
            em.close();
        }

        return entity;
    }

    @Override
    public void delete(AE entity) {
        EntityManager em = null;

        try {
            if (!StringUtils.isEmpty(entity.getId())) {
                em = getEntityManager().createEntityManager();
                em.getTransaction().begin();
                entity = em.find(getType(), entity.getId());
                em.remove(entity);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            Ivy.log().error("AbstractDAO: error deleting data: ", e);
        } finally {
            em.close();
        }
    }

    @Override
    public List<AE> getAll() {
        List<AE> entities = new ArrayList<>();
        try {
            entities = getEntityManager().findAll(getType());
        } catch (Exception e) {
            Ivy.log().error("AbstractDAO:error getting all data: ", e);
        }
        return entities;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cz.eon.md.dao.meta.IAbstractEntityDao#getById(long)
     */
    @Override
    public AE getById(String id) {
        AE entity = null;
        try {
            if (!StringUtils.isEmpty(id)) {
                entity = getEntityManager().find(getType(), id);
            }
        } catch (Exception e) {
            Ivy.log().error("AbstractDAO: error getting data for id: " + id, e);
        }
        return entity;
    }

    @Override
    public List<AE> getByCriteriaQuery(CriteriaQuery<AE> cc) {
        List<AE> resultList = null;
        EntityManager em = null;

        try {
            em = getEntityManager().createEntityManager();
            TypedQuery<AE> typedQuery = em.createQuery(cc);
            resultList = typedQuery.getResultList();
        } catch (Throwable t) {
            resultList = new ArrayList<>();
        } finally {
            em.close();
        }

        return resultList;
    }

    @Override
    public List<AE> getByCriteriaQuery(CriteriaQuery<AE> cc, int offset, int resultSize) {
        List<AE> resultList = null;
        EntityManager em = null;

        try {
            em = getEntityManager().createEntityManager();
            TypedQuery<AE> typedQuery = em.createQuery(cc);
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(resultSize);
            resultList = typedQuery.getResultList();
        } catch (Throwable t) {
            resultList = new ArrayList<>();
        } finally {
            em.clear();
            em.close();
        }

        return resultList;
    }

    @Override
    public AE getByCriteriaQuerySingle(CriteriaQuery<AE> cc) {
        List<AE> beans = getByCriteriaQuery(cc);
        if (beans != null && beans.size() == 1) {
            return beans.get(0);
        }
        return null;
    }

    /**
     * Prepares string value for ignore case query
     * 
     * @param value
     * @return
     */
    protected String prepareForIgnoreCaseQuery(String value) {
        return StringUtils.lowerCase(StringUtils.trimToEmpty(value));
    }

    @Override
    public Long countByCriteriaQuery(CriteriaQuery<Long> cc) {
        Long result = null;
        EntityManager em = null;

        try {
            em = getEntityManager().createEntityManager();
            TypedQuery<Long> typedQuery = em.createQuery(cc);
            List<Long> resultList = typedQuery.getResultList();

            if (resultList != null && resultList.size() == 1) {
                result = resultList.get(0);
            }
        } catch (Throwable t) {
            Ivy.log().error("error in count by cq", t);
            result = null;
        } finally {
            em.close();
        }

        return result;
    }
}
