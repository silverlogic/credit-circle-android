package com.tsl.money2020.model.objects.error;

import java.lang.reflect.Field;

/**
 * Created by Kevin Lavi on 3/20/17.
 */

public class SocialError {

    //only difference is that email is a string not a list
    public String email;
    public static final String NO_EMAIL_PROVIDED = "no_email_provided";

    public String getEmail() {
        return email;
    }

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
