package com.ericlouw.jinjectsu.jinjectsu;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ericlouw.jinjectsu.jinjectsu.exceptions.TypeAlreadyRegisteredException;
import com.ericlouw.jinjectsu.jinjectsu.exceptions.UnregisteredTypeException;
import com.ericlouw.jinjectsu.jinjectsu.interfaces.IFactoryMethod;

class SingletonContainer implements com.ericlouw.jinjectsu.jinjectsu.interfaces.ITypeResolver {
    private Map<Class, Object> singletonLookup;

    private Map<Class, Class> singletonTypeMap;

    private Map<Class, com.ericlouw.jinjectsu.jinjectsu.interfaces.IFactoryMethod> singletonFactoryMethodMap;

    SingletonContainer() {
        this.singletonLookup = new HashMap<>();

        this.singletonTypeMap = new HashMap<>();

        this.singletonFactoryMethodMap = new HashMap<>();
    }

    void register(Class abstractType, Class concreteType) {
        if (this.singletonFactoryMethodMap.containsKey(abstractType)) {
            throw new TypeAlreadyRegisteredException(String.format("Type %s has already been registered as a singleton to be resolved using a factory method.", abstractType.getName()));
        }

        this.singletonTypeMap.put(abstractType, concreteType);
    }

    void register(Class abstractType, com.ericlouw.jinjectsu.jinjectsu.interfaces.IFactoryMethod factoryMethod) {
        if (this.singletonTypeMap.containsKey(abstractType)) {
            throw new TypeAlreadyRegisteredException(String.format("Type %s has already been registered as a singleton.", abstractType.getName()));
        }

        this.singletonFactoryMethodMap.put(abstractType, factoryMethod);
    }

    private Object CreateSingleton(Class abstractClass, Jinjectsu jinjectsu) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        if (this.singletonTypeMap.containsKey(abstractClass)) {
            Class concreteClass = this.singletonTypeMap.get(abstractClass);
            return jinjectsu.ConstructorResolve(concreteClass);
        }

        if (this.singletonFactoryMethodMap.containsKey(abstractClass)) {
            return this.singletonFactoryMethodMap.get(abstractClass).create();
        }

        throw new UnregisteredTypeException(String.format("Type %s was not registered as a singleton.", abstractClass.getName()));
    }

    @Override
    public Object resolve(Class abstractType, Jinjectsu jinjectsu) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        boolean singletonExists = this.singletonLookup.containsKey(abstractType);

        if (!singletonExists) {
            this.singletonLookup.put(abstractType, this.CreateSingleton(abstractType, jinjectsu));
        }

        return this.singletonLookup.get(abstractType);
    }

    @Override
    public Class getTypeToResolveFor(Class type) {
        if (this.singletonTypeMap.containsKey(type)) {
            return this.singletonTypeMap.get(type);
        }

        IFactoryMethod factoryMethod = this.singletonFactoryMethodMap.get(type);

        return factoryMethod.getClass().getMethods()[0].getReturnType();
    }

    @Override
    public Set<Class> getRegisteredTypes() {
        Set<Class> types = new HashSet<>();
        types.addAll(this.singletonTypeMap.keySet());
        types.addAll(this.singletonFactoryMethodMap.keySet());
        return types;
    }

    @Override
    public boolean isTypeRegistered(Class registeredType) {
        return this.singletonTypeMap.containsKey(registeredType) || this.singletonFactoryMethodMap.containsKey(registeredType);
    }
}
