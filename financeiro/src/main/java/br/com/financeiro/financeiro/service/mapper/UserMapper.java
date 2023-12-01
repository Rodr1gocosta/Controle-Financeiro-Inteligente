package br.com.financeiro.financeiro.service.mapper;

import br.com.financeiro.financeiro.domain.User;
import br.com.financeiro.financeiro.record.UserRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserRecord, User>{ }
