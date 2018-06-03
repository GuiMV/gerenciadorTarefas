package com.supero.gerenciador.tarefas.persistence;

import org.jinq.orm.stream.JinqStream;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.persistence.EntityManager;

public abstract class GenericDao<Entity> implements Serializable {

    protected final Class<Entity> entityClass;

    @SuppressWarnings("unchecked")
    public GenericDao() {
        final Type genericSuperclass = getClass().getGenericSuperclass();
        final ParameterizedType param = ParameterizedType.class.cast(genericSuperclass);
        final Type typeParam = param.getActualTypeArguments()[0];
        entityClass = Class.class.cast(typeParam);
    }

    public GenericDao(Class<Entity> entityClass) {
        this.entityClass = entityClass;
    }

    public Entity get(Object primaryKey) {
        Entity entity = getEntityManager().find(entityClass, primaryKey);
        return entity;
    }

    public List<Entity> getAll() {
        return getEntities().toList();
    }

    public void add(Entity entity) {
        getEntityManager().persist(entity);
    }

    public void addAll(List<Entity> entities) {
        for (Entity entity : entities) {
            add(entity);
        }
    }

    public Entity edit(Entity entity) {
        return getEntityManager().merge(entity);
    }

    public void editAll(List<Entity> entities) {
        for (Entity entity : entities) {
            edit(entity);
        }
    }

    public void delete(Entity entity) {
        getEntityManager().remove(entity);
        getEntityManager().flush();
    }

    public void deleteAll(List<Entity> entities) {
        for (Entity entity : entities) {
            delete(entity);
        }
    }

    public <Entity> JinqStream<Entity> getEntities(Class<Entity> entityClass) {
        return PersistenceUtil.getJinqHibernateStreamProvider().streamAll(getEntityManager(), entityClass);
    }

    public JinqStream<Entity> getEntities() {
        return getEntities(entityClass);
    }

    protected EntityManager getEntityManager() {
        return PersistenceUtil.getEntityManager();
    }
}
