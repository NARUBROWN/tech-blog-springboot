package com.scout.tech.data.dto.request;

import java.util.List;

public record TagRequest(
        List<String> tagNames
) {
}
