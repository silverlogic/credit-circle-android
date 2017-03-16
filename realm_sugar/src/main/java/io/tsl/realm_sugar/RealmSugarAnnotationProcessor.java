package io.tsl.realm_sugar;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes({"io.realm.annotations.RealmClass"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RealmSugarAnnotationProcessor extends AbstractProcessor {

    final private static String PACKAGE_NAME = "io.tsl.realm_sugar";

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        TypeElement annotation = null;

        for (TypeElement element: set) {
            if (element.getQualifiedName().toString().equals("io.realm.annotations.RealmClass")) {
                annotation = element;
                break;
            }
        }

        if (annotation == null) {
            return true;
        }

        ArrayList<ClassInfo> classInfoArrayList = new ArrayList<>();

        for (Element element: roundEnvironment.getElementsAnnotatedWith(annotation)) {
            if (!(element instanceof TypeElement)) {
                continue;
            }

            TypeElement typeElement = (TypeElement) element;

            // Discard Realm proxy classes
            String className = typeElement.getQualifiedName().toString();
            if (className.contains("RealmProxy")) {
                continue;
            }

            try {
                ClassInfo classInfo = new ClassInfo(typeElement);
                classInfoArrayList.add(classInfo);

                // Create the merger class if has primary key
                if (classInfo.hasPrimaryKey()) {
                    writeMergerClass(classInfo);
                    writeFinderClass(classInfo);
                    writeKeysClass(classInfo);
                }
            }

            catch (InvalidElementException exception) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, exception.getMessage(), exception.getElement());
            }
        }

        writeMainMerger(classInfoArrayList);

        return true;
    }

    // Merger
    private void writeMainMerger(ArrayList<ClassInfo> classInfoArrayList) {

        // Class which will handle all the mergers
        String className = "Merger";

        // Imports
        ArrayList<String> imports = new ArrayList<>();
        imports.add("com.google.gson.JsonObject");
        imports.add("io.realm.Realm");

        StringBuilder builder = getClassHeader(className, PACKAGE_NAME, imports)
                .append("\tpublic static void merge(Object object, JsonObject json) {\n");

        for (ClassInfo info: classInfoArrayList) {
            if (!info.hasPrimaryKey()) {
                continue;
            }
            builder.append("\t\tif (object instanceof " + info.getQualifiedClassName() + "){\n");
            builder.append("\t\t\t" + getMergerClassQualifiedName(info) + ".merge((" + info.getQualifiedClassName() + ") object, json);\n");
            builder.append("\t\t\treturn;\n\t\t}\n\n");
        }

        builder.append("\t}\n")
                .append("}\n");

        writeFile(className, PACKAGE_NAME, builder.toString());
    }

    private void writeMergerClass(ClassInfo element) {

        String className = getMergerClassName(element);

        // Imports
        ArrayList<String> imports = new ArrayList<>();
        imports.add("com.google.gson.JsonObject");
        imports.add("io.realm.Realm");

        // Class header
        StringBuilder builder = getClassHeader(className, element.getPackage(), imports);

        // Method to overwrite the values from the json
        builder.append("\tpublic static void merge(" + element.getClassName() + " object, JsonObject json) {\n");

        // Get existing object
        builder.append("\t\t" + element.getClassName() + " oldObject = getExistingObject(object);\n");
        builder.append("\t\tif (oldObject == null){\n\t\t\treturn;\n\t\t}\n\n");

        // Set al fields
        for (FieldInfo field: element.getFields()) {

            if (field.isIgnored()) {
                continue;
            }

            // if existing in the json, set with the value
            builder.append("\t\tif (!json.has(\"" + field.getSerializedName() + "\")){\n");
            builder.append("\t\t\tobject." + field.getName() + " = oldObject." + field.getName() + ";\n");
            builder.append("\t\t}\n\n");
        }

        // Close method
        builder.append("\t}\n\n");

        // Method to get the existing object
        FieldInfo primaryKey = element.getPrimaryKey();
        builder.append("\tpublic static " + element.getClassName() + " getExistingObject(" + element.getClassName() + " object){\n");

        builder.append("\t\tRealm realm = Realm.getDefaultInstance();\n");
        builder.append("\t\t" + primaryKey.getType() + " primaryKeyValue = object." + primaryKey.getName() + ";\n");
        builder.append("\t\t" + element.getClassName() + " oldObject = realm.where(" + element.getClassName() + ".class).equalTo(\"" + primaryKey.getName() + "\", primaryKeyValue).findFirst();\n");
        builder.append("\t\tif (oldObject != null) {\n");
        builder.append("\t\t\toldObject = realm.copyFromRealm(oldObject);\n");
        builder.append("\t\t}\n");
        builder.append("\t\trealm.close();\n");
        builder.append("\t\treturn oldObject;\n");

        builder.append("\t}\n");

        // Close class
        builder.append("}\n");

        writeFile(className, element.getPackage(), builder.toString());
    }

    private String getMergerClassName(ClassInfo info) {
        String elementName = info.getClassName();
        return elementName + "$Merger";
    }

    private String getMergerClassQualifiedName(ClassInfo info) {
        return info.getPackage() + "." + getMergerClassName(info);
    }

    // Finder
    private void writeFinderClass(ClassInfo element) {
        String className = getFinderClassName(element);
        FieldInfo primaryKey = element.getPrimaryKey();

        // Imports
        ArrayList<String> imports = new ArrayList<>();
        imports.add("io.realm.Realm");
        imports.add("java.util.List");
        imports.add("java.util.ArrayList");

        // Class header
        StringBuilder builder = getClassHeader(className, element.getPackage(), imports);

        // Method to get the existing object
        builder.append("\tpublic static " + element.getClassName() + " find(" + primaryKey.getType() + " primaryKeyValue){\n");
        builder.append("\t\tRealm realm = Realm.getDefaultInstance();\n");
        builder.append("\t\t" + element.getClassName() + " object = realm.where(" + element.getClassName() + ".class).equalTo(\"" + primaryKey.getName() + "\", primaryKeyValue).findFirst();\n");
        builder.append("\t\trealm.close();\n");
        builder.append("\t\treturn object;\n");
        builder.append("\t}\n");
        builder.append("\n");

        // Method to get a list of existing objects
        String wrapperClassType = primaryKey.getWrapperClassType();
        builder.append("\tpublic static List<" + element.getClassName() + "> find(List<" + wrapperClassType + "> primaryKeyValues){\n");
        builder.append("\t\tArrayList<" + element.getClassName() + "> result = new ArrayList<>();\n");
        builder.append("\t\tif (primaryKeyValues == null || primaryKeyValues.size() == 0) {\n");
        builder.append("\t\t\treturn result;\n");
        builder.append("\t\t}\n\n");
        builder.append("\t\t" + wrapperClassType + "[] primaryKeysArray = primaryKeyValues.toArray(new " + wrapperClassType + "[primaryKeyValues.size()]);\n");
        builder.append("\t\tRealm realm = Realm.getDefaultInstance();\n");
        builder.append("\t\tresult.addAll(realm.where(" + element.getClassName() + ".class).in(\"" + primaryKey.getName() + "\", primaryKeysArray).findAll());\n");
        builder.append("\t\trealm.close();\n");
        builder.append("\t\treturn result;\n");
        builder.append("\t}\n");

        // Close class
        builder.append("}\n");

        writeFile(className, element.getPackage(), builder.toString());
    }

    private String getFinderClassName(ClassInfo info) {
        return info.getClassName() + "Finder";
    }

    private String getFinderClassQualifiedName(ClassInfo info) {
        return info.getPackage() + "." + getFinderClassName(info);
    }

    // Keys
    private void writeKeysClass(ClassInfo element) {
        String className = getKeysClassName(element);

        // Class header
        StringBuilder builder = getClassHeader(className, element.getPackage(), null);

        // Method to get the existing object
        for (FieldInfo field: element.getFields()) {
            if (field.isIgnored() || field.isStatic()) {
                continue;
            }
            String formattedName = field.getName().replaceAll("([^_A-Z])([A-Z])", "$1_$2").toUpperCase();
            builder.append("\tpublic final static String " + formattedName + " = \"" + field.getName() + "\";\n");
        }

        // Close class
        builder.append("\n}\n");

        writeFile(className, element.getPackage(), builder.toString());
    }

    private String getKeysClassName(ClassInfo info) {
        return info.getClassName() + "Keys";
    }

    private String getKeysClassQualifiedName(ClassInfo info) {
        return info.getPackage() + "." + getKeysClassName(info);
    }

    // Helpers
    private StringBuilder getClassHeader(String className, String packageName, ArrayList<String> imports) {
        StringBuilder builder =  new StringBuilder()
                .append("package " + packageName + ";\n\n");

        if (imports != null) {
            for (String imp : imports) {
                String importString = "import " + imp + ";\n";
                builder.append(importString);
            }

            if (imports.size() > 0) {
                builder.append("\n");
            }
        }

        builder.append("public class " + className + " {\n\n");
        return builder;
    }

    private void writeFile(String className, String packageName, String fileContent) {
        try {
            String fullClassName = packageName + "." + className;
            JavaFileObject source = processingEnv.getFiler().createSourceFile(fullClassName);

            Writer writer = source.openWriter();
            writer.write(fileContent);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }
    }

    private void addComment(Object object, StringBuilder builder) {
        builder.append("// " + object + "\n");
    }
}