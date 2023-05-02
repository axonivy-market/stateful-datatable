package com.axonivy.demo.statefuldatatable.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import com.axonivy.demo.statefuldatatable.entity.AbstractEntity;

/**
 * Base dao interface
 *
 */
public interface IAbstractEntityDAO<AE extends AbstractEntity> {

    /**
     * Defines the DAO class
     *
     * @return type
     */
    Class<AE> getType();

    /**
     * save entity in DB
     *
     * @param entity
     * @return saved
     */
    AE save(AE entity);

    /**
     * Delete method (remove)
     *
     * @param entity
     */
    void delete(AE entity);

    /**
     * @return all entities
     */
    List<AE> getAll();

    /**
     * Finds entity by its id
     *
     * @param id
     * @return entity
     */
    AE getById(String id);

    /**
     * Makes query using {@link EntityManager} / {@link CriteriaQuery}
     *
     * @param cc
     */
    List<AE> getByCriteriaQuery(CriteriaQuery<AE> cc);

    /**
     * Makes query using {@link EntityManager} / {@link CriteriaQuery}
     */
    List<AE> getByCriteriaQuery(CriteriaQuery<AE> cc, int offset, int resultSize);

    /**
     * Returns single result of {@link #getByCriteriaQuery(CriteriaQuery)} if only
     * one is found by query, null otherwise.
     *
     * @param cc
     */
    AE getByCriteriaQuerySingle(CriteriaQuery<AE> cc);

    /**
     * Returns single result - count of items found by query.
     *
     * @param cc
     */
    Long countByCriteriaQuery(CriteriaQuery<Long> cc);
}
