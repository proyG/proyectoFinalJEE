package com.entity;

import com.entity.Cliente;
import com.entity.Inmueble;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-05-27T20:59:23")
@StaticMetamodel(Transaccion.class)
public class Transaccion_ { 

    public static volatile SingularAttribute<Transaccion, Integer> idTransaccion;
    public static volatile SingularAttribute<Transaccion, Cliente> cliente;
    public static volatile SingularAttribute<Transaccion, String> tipo;
    public static volatile SingularAttribute<Transaccion, Inmueble> inmueble;

}