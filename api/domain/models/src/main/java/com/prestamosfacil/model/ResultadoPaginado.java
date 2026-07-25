package com.prestamosfacil.domain.model.pagination;

import java.util.List;

public record ResultadoPaginado<T>(List<T> contenido, long totalElementos, int totalPaginas, int paginaActual) {
}
