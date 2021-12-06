package com.epam.esm.controller.hateoas.model;

import com.epam.esm.controller.hateoas.HateoasProvider;
import com.epam.esm.dto.IdDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

public class HateoasModel <T extends IdDto> extends RepresentationModel<HateoasModel<T>> {

    private T data;

    public HateoasModel(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public <T extends IdDto> HateoasModel<T> build(HateoasProvider<T> hateoasProvider, T object, Long numberOfRecords) {
        HateoasModel<T> hateoasModel = new HateoasModel<>(object);
        List<Link> links = hateoasProvider.provide(object, numberOfRecords);
        hateoasModel.add(links);

        return hateoasModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HateoasModel<?> that = (HateoasModel<?>) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return "HateoasModel{" +
                "data=" + data +
                '}';
    }
}
