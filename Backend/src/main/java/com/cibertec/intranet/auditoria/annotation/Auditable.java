package com.cibertec.intranet.auditoria.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Auditable {
    String accion();
    String tabla();
}