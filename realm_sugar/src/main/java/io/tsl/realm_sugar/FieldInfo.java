package io.tsl.realm_sugar;

import java.util.HashMap;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by Manu on 10/1/17.
 */

class FieldInfo {
    private boolean isPrimaryKey;
    private boolean isIgnored;
    private String name;
    private String serializedName;
    private String type;
    private boolean isStatic;

    private static HashMap<String, String> primitivesAndWrapperClasses;

    FieldInfo(Element element) throws InvalidElementException {

        this.isPrimaryKey = false;
        this.name = element.getSimpleName().toString();
        this.type = element.asType().toString();

        String serializedName = this.name;

        // search for serializer name annotation
        List<? extends AnnotationMirror> annotations = element.getAnnotationMirrors();
        for (AnnotationMirror annotation: annotations) {
            String annotationName = ((TypeElement) annotation.getAnnotationType().asElement()).getQualifiedName().toString();
            if (annotationName.equals("com.google.gson.annotations.SerializedName")) {
                AnnotationValue value = getAnnotationValue(annotation, "value");
                if (value != null) {
                    serializedName = (String) value.getValue();
                }
            }

            else if (annotationName.equals("io.realm.annotations.PrimaryKey")) {
                this.isPrimaryKey = true;
            }

            else if (annotationName.equals("io.realm.annotations.Ignore")) {
                this.isIgnored = true;
            }
        }

        this.serializedName = serializedName;

        if (!this.isIgnored()) {
            if (element.getModifiers().contains(Modifier.PRIVATE)) {
                throw new InvalidElementException("RealmSugar: fields in @RealmClasses classes can't be private", element);
            }
        }

        isStatic = element.getModifiers().contains(Modifier.STATIC);
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isIgnored() {
        return isIgnored;
    }

    String getSerializedName() {
        return serializedName;
    }

    String getName() {
        return name;
    }

    String getType() {
        return type;
    }

    String getWrapperClassType() {
        String wrapperClassType = getPrimitivesAndWrapperClasses().get(getType());
        return wrapperClassType == null ? getType() : wrapperClassType;
    }

    private static HashMap<String, String> getPrimitivesAndWrapperClasses() {
        if (primitivesAndWrapperClasses == null) {
            primitivesAndWrapperClasses = new HashMap<>();
            primitivesAndWrapperClasses.put("byte", "java.lang.Byte");
            primitivesAndWrapperClasses.put("short", "java.lang.Short");
            primitivesAndWrapperClasses.put("int", "java.lang.Integer");
            primitivesAndWrapperClasses.put("long", "java.lang.Long");
            primitivesAndWrapperClasses.put("float", "java.lang.Float");
            primitivesAndWrapperClasses.put("double", "java.lang.Double");
            primitivesAndWrapperClasses.put("boolean", "java.lang.Boolean");
            primitivesAndWrapperClasses.put("char", "java.lang.Character");
        }
        return primitivesAndWrapperClasses;
    }

    // Helpers
    private static AnnotationValue getAnnotationValue(AnnotationMirror annotation, String fieldName) {
        for (ExecutableElement executable : annotation.getElementValues().keySet()) {
            if (fieldName.equals(executable.getSimpleName().toString())) {
                return annotation.getElementValues().get(executable);
            }
        }
        return null;
    }
}
