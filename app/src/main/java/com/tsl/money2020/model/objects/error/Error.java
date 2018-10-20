package com.tsl.money2020.model.objects.error;

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
    public List<String> first_name;
    public List<String> last_name;
    public List<String> location;
    public List<String> paypal_email;
    public List<String> gender;
    public List<String> marital_status;
    public List<String> children;
    public List<String> birthday;
    public List<String> avatar;
    public List<String> provider;
    public List<String> non_field_errors;
    public List<String> new_email;
    public List<String> token;

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