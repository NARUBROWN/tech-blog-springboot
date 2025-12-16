package com.naru.tech.data.dto.web.response;

import java.util.List;

public record TagResponse(
        List<String> tagNames
) {
}
