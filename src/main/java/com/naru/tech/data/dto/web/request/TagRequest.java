package com.naru.tech.data.dto.web.request;

import java.util.List;

public record TagRequest(
        List<String> tagNames
) {
}
