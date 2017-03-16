package io.tsl.realm_sugar;

import javax.lang.model.element.Element;

/**
 * Created by Manu on 10/1/17.
 */

class InvalidElementException extends Exception {
    Element element;

    public InvalidElementException(String s, Element element) {
        super(s);
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}
