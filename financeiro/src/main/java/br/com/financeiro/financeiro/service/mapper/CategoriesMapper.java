package br.com.financeiro.financeiro.service.mapper;

import br.com.financeiro.financeiro.domain.Categories;
import br.com.financeiro.financeiro.record.CategoriesRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoriesMapper extends EntityMapper<CategoriesRecord, Categories> { }
