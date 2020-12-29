package com.cab404.jsonm.impl;

/**
* Sorry for no comments!
* Created at 1:15 on 15.02.15
*
* @author cab404
*/
interface JSONAddressNode {
    /**
     * Retrieves an object this node pointing at.
     *
     * @param jsonObject from what to retrieve an object.
     */
    public Object move(Object jsonObject);

    /**
     * Sets value of something this node pointing at
     *
     * @param target where to set a value.
     * @param value what to set
     */
    public void set(Object target, Object value);
}
