package com.axonivy.market.statefuldatatable.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import com.axonivy.market.statefuldatatable.entity.AbstractEntity;

/**
 * Base dao interface
 *
 */
public interface IAbstractEntityDAO<AE extends AbstractEntity> {

    /**
     * Defines the DAO class
     * 
     * @return
     */
    Class<AE> getType();

    /**
     * save entity in DB
     *
     * @param entity
     * @return
     */
    AE save(AE entity);

    /**
     * Delete method (remove)
     *
     * @param entity
     */
    void delete(AE entity);

    /**
     * Finds all entities
     *
     * @return
     */
    List<AE> getAll();

    /**
     * Finds entity by its id
     *
     * @param ae
     * @return
     */
    AE getById(String id);

    /**
     * Makes query using {@link EntityManager} / {@link CriteriaQuery}
     *
     * @param cf
     * @return
     */
    List<AE> getByCriteriaQuery(CriteriaQuery<AE> cc);

    /**
     * Makes query using {@link EntityManager} / {@link CriteriaQuery}
     *
     * @param cf
     * @return
     */
    List<AE> getByCriteriaQuery(CriteriaQuery<AE> cc, int offset, int resultSize);

    /**
     * Returns single result of {@link #getByCriteriaQuery(CriteriaQuery)} if only
     * one is found by query, null otherwise.
     *
     * @param cc
     * @return
     */
    AE getByCriteriaQuerySingle(CriteriaQuery<AE> cc);

    /**
     * Returns single result - count of items found by query.
     * 
     * @param cc
     * @return
     */
    Long countByCriteriaQuery(CriteriaQuery<Long> cc);
}
