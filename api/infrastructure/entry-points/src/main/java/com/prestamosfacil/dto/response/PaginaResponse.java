package com.prestamosfacil.dto.response;

import java.util.List;

public record PaginaResponse<T>(List<T> contenido, long totalElementos, int totalPaginas, int paginaActual) {
}
