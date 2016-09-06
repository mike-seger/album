package com.net128.app.album.persistence;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.FieldEnd;
import org.mongodb.morphia.query.Query;

public class Repository<T> {
	protected Datastore ds=MongoDatastoreFactory.getDatastore();
	
	public T saveOrUpdate(Class<T> clazz, T entity, Object id) {
		if(id!=null) {
			ds.delete(entity);
			ds.save(entity);
			return entity;
		}
		
		List<T> list=new ArrayList<>();
		list.add(entity);
		Iterable<Key<T>> result=ds.save(list);
		T t=ds.getByKey(clazz, result.iterator().next());
		return t;
	}
	
	public T findOneByField(Class<T> clazz, String name, String value) {
		final List<T> entities = findByField(clazz, name, value);
		if(entities.size()<=0) {
			return null;
		}
		T entity=entities.get(0);
		return entity;
	}
	
	public List<T> findByField(Class<T> clazz, String name, String value) {
		return getFieldQ(clazz, name).equal(value).asList();
	}
	
	public List<T> findByFieldOrdered(Class<T> clazz, String name, String value, String order) {
		return getFieldQ(clazz, name).equal(value).order(order).asList();
	}

	public void remove(T entity) {
		if(entity!=null) {
			ds.delete(entity);
		}
	}
	
	public void remove(List<T> entities) {
		if(entities!=null) {
			for(T entity : entities) {
				ds.delete(entity);				
			}
		}
	}
	
	protected FieldEnd<? extends Query<T>> getFieldQ(Class<T> clazz, String name) {
		return ds.createQuery(clazz).field(name);
	}
}
