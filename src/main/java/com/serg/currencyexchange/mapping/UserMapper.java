package com.serg.currencyexchange.mapping;

import com.serg.currencyexchange.dto.SignUpRequestDto;
import com.serg.currencyexchange.dto.SignUpResponseDto;
import com.serg.currencyexchange.dto.UserDto;
import com.serg.currencyexchange.model.Role;
import com.serg.currencyexchange.model.User;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapSignUpRequestDtoToUser(SignUpRequestDto dto);

    SignUpResponseDto mapUserToSignUpResponseDto(User user);

    Set<String> mapRolesToStrings(Set<Role> roles);

    default String mapRoleToString(Role role) {
        return role.getName();
    }

    UserDto mapUserToUserDto(User user);
}
