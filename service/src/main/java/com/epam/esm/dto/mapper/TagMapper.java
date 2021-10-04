package com.epam.esm.dto.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    private TagMapper(){}

    public static TagDto toTagDto(Tag tag) {
        String name = tag.getName();
        return new TagDto(name);
    }

    public static Tag fromTagDto(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        return tag;
    }
}
