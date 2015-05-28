package com.entity;

import com.entity.Transaccion;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-05-27T19:40:29")
@StaticMetamodel(Inmueble.class)
public class Inmueble_ { 

    public static volatile SingularAttribute<Inmueble, Integer> idInmueble;
    public static volatile SingularAttribute<Inmueble, String> tamaÃo;
    public static volatile SingularAttribute<Inmueble, String> barrio;
    public static volatile SingularAttribute<Inmueble, String> tipo;
    public static volatile SingularAttribute<Inmueble, String> precio;
    public static volatile SingularAttribute<Inmueble, String> img;
    public static volatile SingularAttribute<Inmueble, String> direccion;
    public static volatile SingularAttribute<Inmueble, String> telefono;
    public static volatile CollectionAttribute<Inmueble, Transaccion> transaccionCollection;

}