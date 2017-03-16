package io.tsl.realm_sugar;

import java.util.ArrayList;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * Created by Manu on 10/1/17.
 */

class ClassInfo {

    private String qualifiedClassName;
    private String className;
    private FieldInfo primaryKey;
    private ArrayList<FieldInfo> fields;

    ClassInfo(TypeElement element) throws InvalidElementException {
        qualifiedClassName = element.getQualifiedName().toString();
        className = element.getSimpleName().toString();

        fields = new ArrayList<>();
        for (Element fieldElement: element.getEnclosedElements()) {
            if (fieldElement.getKind() != ElementKind.FIELD) {
                continue;
            }

            FieldInfo info = new FieldInfo(fieldElement);
            fields.add(info);
            if (info.isPrimaryKey()) {
                primaryKey = info;
            }
        }
    }

    boolean hasPrimaryKey() {
        return primaryKey != null;
    }

    String getQualifiedClassName() {
        return qualifiedClassName;
    }

    String getClassName() {
        return className;
    }

    String getPackage() {
        return qualifiedClassName.substring(0, qualifiedClassName.lastIndexOf("."));
    }

    FieldInfo getPrimaryKey() {
        return primaryKey;
    }

    ArrayList<FieldInfo> getFields() {
        return fields;
    }

    String getProxyClassName() {
        return getClassName() + "RealmProxy";
    }
}
