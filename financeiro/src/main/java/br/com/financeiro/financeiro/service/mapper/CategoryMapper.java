package br.com.financeiro.financeiro.service.mapper;

import br.com.financeiro.financeiro.domain.Category;
import br.com.financeiro.financeiro.record.CategoryRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryRecord, Category> { }
