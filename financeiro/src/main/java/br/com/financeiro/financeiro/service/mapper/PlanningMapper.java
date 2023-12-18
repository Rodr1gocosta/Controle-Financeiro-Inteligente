package br.com.financeiro.financeiro.service.mapper;

import br.com.financeiro.financeiro.domain.Categories;
import br.com.financeiro.financeiro.domain.Category;
import br.com.financeiro.financeiro.domain.Planning;
import br.com.financeiro.financeiro.record.CategoriesRecord;
import br.com.financeiro.financeiro.record.CategoryDefaultRecord;
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
    @Mapping(target = "category", source = "category")
    Categories toCategoriesEntity(CategoriesRecord categoriesRecord);

//    @Named("toCategoryEntity")
//    Category toCategoryEntity(CategoryDefaultRecord categoryDefaultRecord);



    @Override
    @Mapping(target = "categoriesRecords", source = "categories", qualifiedByName = "toCategoriesRecordDto")
    PlanningRecord toDto(Planning planning);

    @Named("toCategoriesRecordDto")
    @Mapping(target = "category", source = "category")
    CategoriesRecord toCategoriesRecordDto(Categories categories);

//    @Named("toCategoryDto")
//    CategoryDefaultRecord toCategoryRecordDto(Category category);

}
