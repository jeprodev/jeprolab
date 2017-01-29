package com.jeprolab.assets.tools.events;

import com.jeprolab.JeproLab;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.softnetConsult.utils.exceptions.Bug;

/**
 *
 * Created by jeprodev on 09/01/2016.
 */
public class JeproLabEvent<E extends JeproLabEventType> {
    public static final boolean JEPROLAB_SAFE_EVENT_CAST = true;

    private JeproLabEventProducer producer;

    private Class<? extends E> domain;

    private E type;

    private boolean is_validated;

    public boolean info_boolean = false;
    public long info_long;
    public double info_double;
    public Object info_object;

    private List<JeproLabEventAnnotation> annotations;
    private List<JeproLabEventAnnotation> exposedAnnotations;

    public JeproLabEvent(JeproLabEventProducer prod, Class<? extends E> dom, E type){
        if(prod == null){
            throw new NullPointerException(JeproLab.getBundle().getString("JEPROLAB_CANNOT_CONSTRUCT_EVENT_FOR_NULL_PRODUCER_MESSAGE"));
        }

        if(dom == null){
            throw new NullPointerException(JeproLab.getBundle().getString("JEPROLAB_CANNOT_CONSTRUCT_EVENT_FOR_NULL_DOMAIN_MESSAGE"));
        }

        if(type == null){
            throw new NullPointerException(JeproLab.getBundle().getString("JEPROLAB_CANNOT_CONSTRUCT_EVENT_FOR_NULL_TYPE_MESSAGE"));
        }

        this.producer = prod;
        this.domain = dom;
        this.type = type;

        this.is_validated = false;

        this.info_boolean = false;
        this.info_long = 0L;
        this.info_double = Double.NaN;
        this.info_object = null;

        this.annotations = null;
        this.exposedAnnotations = null;
    }

    public JeproLabEvent(JeproLabEventProducer prod, Class<? extends E> dom, E ty, boolean info){
        this(prod, dom, ty);
        this.info_boolean = info;
    }

    public JeproLabEvent(JeproLabEventProducer prod, Class<? extends E> dom, E type, long info){
        this(prod, dom, type);
        this.info_long = info;
    }

    public JeproLabEvent(JeproLabEventProducer prod, Class<? extends E> dom, E type,  double info){
        this(prod, dom, type);
        this.info_double = info;
    }

    public JeproLabEvent(JeproLabEventProducer prod, Class<? extends E> dom, E type, Object info){
        this(prod, dom, type);
        this.info_object = info;
    }

    public JeproLabEvent(JeproLabEventProducer prod, Class<? extends E> dom, E type, boolean infoBool, long infoL, double infoD, Object infoObj){
        this(prod, dom, type);
        this.info_boolean = infoBool;
        this.info_long = infoL;
        this.info_double = infoD;
        this.info_object = infoObj;
    }

    public JeproLabEventProducer getProducer(){
        return producer;
    }

    public Class<? extends E> getDomain(){
        return domain;
    }

    public E getType(){
        return type;
    }

    public boolean isValidated(){
        return is_validated;
    }

    protected void setValidated(boolean validated){
        is_validated = validated;
    }

    public boolean getInfoBoolean() {
        return info_boolean;
    }

    public long getInfoLong() {
        return info_long;
    }

    public double getInfoDouble() {
        return info_double;
    }

    public Object getInfoObject() {
        return info_object;
    }

    @SuppressWarnings("unchecked")
    public <T extends JeproLabEventType> JeproLabEvent<T> cast(Class<T> domain) {

        if (JEPROLAB_SAFE_EVENT_CAST) {

            if (null == domain)
                throw new NullPointerException("Cannot cast event to a null target domain");

            if (!getDomain().isAssignableFrom(domain)) {
                throw new ClassCastException("Cannot cast this event of type "
                    + this.getClass().getName() + "<" + getDomain().getName() + "> "
                    + "to Event<" + domain.getName() + ">");
            }
        }

        return (JeproLabEvent<T>) this;
    }



    public boolean validated() {
        return is_validated;
    }


    public void addAnnotation(JeproLabEventListener listener, Object annotationInfo) {
        if (null == listener)
            throw new NullPointerException("A null EventListener cannot add an EventAnnotation");
        if (null == annotations)
            annotations = new ArrayList<JeproLabEventAnnotation>();
        annotations.add(new JeproLabEventAnnotation(listener, annotationInfo));
        exposedAnnotations = null;
    }

    public JeproLabValidation.Requirement getValidationRequirement() {

        Class<? extends JeproLabEventType> typeClass = type.getClass();
        JeproLabValidation validation = typeClass.getAnnotation(JeproLabValidation.class);
        if (null != validation)
            return validation.value();

        // If the event type is an enum, the annotation may have been attached not to the
        // class itself, but to the "field", i.e. the enum constant:
        if (typeClass.isEnum()) {

            try {
                Field declaringField = typeClass.getField(type.toString());
                validation = declaringField.getAnnotation(JeproLabValidation.class);
            } catch(NoSuchFieldException e) {
                throw new Bug("This should never happen!", e);
            }

            if (null != validation)
                return validation.value();
        }

        return JeproLabValidation.Requirement.MAY_BE_VALIDATE;
    }

    public List<JeproLabEventAnnotation> getAnnotations() {
        if (null == exposedAnnotations) {

            if (null == annotations)
                return Collections.emptyList();

            exposedAnnotations = new ArrayList<>();
            exposedAnnotations.addAll(annotations);
            exposedAnnotations = Collections.unmodifiableList(exposedAnnotations);
        }
        return exposedAnnotations;
    }

    public List<JeproLabEventAnnotation> getAnnotations(JeproLabEventListener byListener) {

        if (null == annotations)
            return Collections.emptyList();

        if (null == byListener)
            return getAnnotations();

        List<JeproLabEventAnnotation> listenerAnnotations = new ArrayList<>();
        for (JeproLabEventAnnotation annotation : annotations) {
            if (annotation.getListener() == byListener)
                listenerAnnotations.add(annotation);
        }
        return Collections.unmodifiableList(listenerAnnotations);
    }

    @Override
    public String toString() {
        String s = super.toString() + "{";
        s += "type=(" + getType().toString() + ");";
        s += "producer=(" + getProducer().toString() + ");";
        s += "info=(";
        s += "bool:" + getInfoBoolean() + ";";
        s += "long:" + getInfoLong() + ";";
        s += "double:" + getInfoDouble() + ";";
        s += "object:" + getInfoObject() + ";";
        s += ");";
        s += "validated=(" + (validated() ? "Y" : "N") + ");";
        s += "valid-reqr=(" + getValidationRequirement() + ");";
        s += "annotations=(" + (null == annotations ? 0 : annotations.size()) + ");";
        s += "}";
        return s;
    }
}
