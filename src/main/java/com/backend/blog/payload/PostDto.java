package com.backend.blog.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PostDto {
    private long id;
    private String title;
    private String description;
    private String content;
}
