package com.epam.esm.dto.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagServiceMapper {

    private TagServiceMapper() {
    }

    public TagDto toTagDto(Tag tag) {
        Long id = tag.getId();
        String name = tag.getName();
        return new TagDto(id, name);
    }

    public Tag fromTagDto(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        return tag;
    }
}
