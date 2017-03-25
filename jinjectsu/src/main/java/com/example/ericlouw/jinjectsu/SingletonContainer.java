package com.example.ericlouw.jinjectsu;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import exceptions.UnregisteredTypeException;

class SingletonContainer implements ITypeResolver {
    private Map<Class, Object> singletonLookup;

    private Map<Class, Class> singletonTypeMap;

    SingletonContainer() {
        this.singletonLookup = new HashMap<>();

        this.singletonTypeMap = new HashMap<>();
    }

    void register(Class abstractType, Class concreteType)
    {
        this.singletonTypeMap.put(abstractType, concreteType);
    }

    private Object CreateSingleton(Class abstractClass, Jinjectsu jinjectsu) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        if (this.singletonTypeMap.containsKey(abstractClass)) {
            Class concreteClass = this.singletonTypeMap.get(abstractClass);
            return jinjectsu.ConstructorResolve(concreteClass);
        }

        throw new UnregisteredTypeException(String.format("Type %s was not registered as a singleton.", abstractClass.getName()));
    }

    @Override
    public Object resolve(Class abstractType, Jinjectsu jinjectsu) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        boolean singletonExists = this.singletonLookup.containsKey(abstractType);

        if (!singletonExists)
        {
            this.singletonLookup.put(abstractType, this.CreateSingleton(abstractType, jinjectsu));
        }

        return this.singletonLookup.get(abstractType);
    }

    @Override
    public Class getTypeToResolveFor(Class type) {
        return this.singletonTypeMap.get(type);
    }

    @Override
    public Set<Class> getRegisteredTypes() {
        return this.singletonTypeMap.keySet();
    }

    @Override
    public boolean isTypeRegistered(Class registeredType) {
        return this.singletonTypeMap.containsKey(registeredType);
    }
}
