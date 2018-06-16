package org.yipuran.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.google.inject.BindingAnnotation;
/**
 * SqlSessionコントロール.
 *
 * IBatisDao の中で SqlSession に付与するためのアノテーション
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@BindingAnnotation
@interface ControleSession{
}
