package com.sliit.smartcampus.config;

import java.lang.reflect.Field;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
public class MongoIdCallback implements BeforeConvertCallback<Object> {

    @Override
    public Object onBeforeConvert(Object entity, String collection) {
        ReflectionUtils.doWithFields(entity.getClass(), field -> setIdIfMissing(entity, field));
        return entity;
    }

    private void setIdIfMissing(Object entity, Field field) {
        if (!field.isAnnotationPresent(Id.class)) {
            return;
        }

        ReflectionUtils.makeAccessible(field);
        Object current = ReflectionUtils.getField(field, entity);
        if (current != null) {
            return;
        }

        if (UUID.class.equals(field.getType())) {
            ReflectionUtils.setField(field, entity, UUID.randomUUID());
            return;
        }

        if (String.class.equals(field.getType())) {
            ReflectionUtils.setField(field, entity, UUID.randomUUID().toString());
        }
    }
}
