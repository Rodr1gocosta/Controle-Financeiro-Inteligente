package br.com.financeiro.financeiro.service.mapper;

import br.com.financeiro.financeiro.domain.Categories;
import br.com.financeiro.financeiro.domain.Category;
import br.com.financeiro.financeiro.domain.Planning;
import br.com.financeiro.financeiro.record.CategoriesRecord;
import br.com.financeiro.financeiro.record.CategoryRecord;
import br.com.financeiro.financeiro.record.PlanningRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PlanningMapper extends EntityMapper<PlanningRecord, Planning> {

    @Override
    @Mapping(target = "categories", source = "categoriesRecords", qualifiedByName = "toCategoriesEntity")
    Planning toEntity(PlanningRecord planningRecord);

    @Named("toCategoriesEntity")
    @Mapping(target = "category", source = "categoryRecord", qualifiedByName = "toCategoryEntity")
    Categories toCategoriesEntity(CategoriesRecord categoriesRecord);

    @Named("toCategoryEntity")
    Category toCategoryEntity(CategoryRecord categoryRecord);
}
