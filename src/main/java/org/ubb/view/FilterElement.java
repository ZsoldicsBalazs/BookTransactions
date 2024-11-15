package org.ubb.view;

public interface FilterElement<T> {

    boolean evaluate(T entity);

}
