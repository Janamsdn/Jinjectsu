package com.example.ericlouw.jinjectsu;

public interface ITypeBinder {
    <TConcrete> Jinjectsu instance(TConcrete concrete);
    Jinjectsu lifestyleTransient(Class concreteType);
    Jinjectsu lifestyleSingleton(Class concreteType);
    Jinjectsu lifeStyleScoped(Class concreteType);
}
