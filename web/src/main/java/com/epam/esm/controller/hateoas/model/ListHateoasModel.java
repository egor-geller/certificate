package com.epam.esm.controller.hateoas.model;

import com.epam.esm.controller.hateoas.HateoasProvider;
import com.epam.esm.dto.IdDto;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

public class ListHateoasModel<T extends IdDto> extends RepresentationModel<ListHateoasModel<T>> {
    private List<T> data;

    public ListHateoasModel(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public static <T extends IdDto> ListHateoasModel<T> build(HateoasProvider<List<T>> hateoasProvider,
                                                                        List<T> list) {
        ListHateoasModel<T> hateoasModel = new ListHateoasModel<>(list);
        List<Link> links = hateoasProvider.provide(list);
        hateoasModel.add(links);

        return hateoasModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListHateoasModel<?> that = (ListHateoasModel<?>) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return "ListHateoasModel{" +
                "data=" + data +
                '}';
    }
}
