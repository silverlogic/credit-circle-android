package com.tsl.baseapp.model.objects.error;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Kevin Lavi on 9/20/16.
 */

public class Error {
    public List<String> current_password;
    public List<String> email;
    public List<String> username;
    public List<String> password;

    public final String getErrorString(){
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(newLine);

        //determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        //print field names paired with their values
        for ( Field field : fields  ) {
            if (field.getName().equals("serialVersionUID")) {
                continue;
            }
            try {
                //requires access to private field:
                if (field.get(this) != null && field.get(this) != (Integer)0){
                    result.append(field.get(this) );
                    result.append(newLine);
                }
            } catch ( IllegalAccessException ex ) {
                System.out.println(ex);
            }
        }
        String str = result.toString();
        str = str.replaceAll("\\[", "").replaceAll("\\]","");

        return str;
    }
}